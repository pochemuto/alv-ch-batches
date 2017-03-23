package ch.alv.batches.master.to.jobdesk;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static ch.alv.batches.master.to.jobdesk.MasterToJobdeskSettings.MAPPING_FILE;

@Configuration
public class FullMasterToJobdeskConfiguration {

    public final static String BATCH_JOB_JOBDESK_FULLRELOAD = "jobdeskFullReloadJob";

    @Resource
    MasterToJobdeskSettings masterToJobdeskSettings;

    @Resource
    protected Client elasticsearchClient;

    @Resource
    protected StepBuilderFactory steps;

    @Resource
    protected JobBuilderFactory jobs;

    @Resource
    protected JobRepository jobRepository;

    @Resource
    private Job loadAllLocationsIntoJobdeskJob;

    @Resource
    private Job loadAllVacanciesIntoJobdeskJob;

    private static final DateTimeFormatter datetimeFormatter = DateTimeFormat.forPattern("YYYYMMddHHmmss");

    @Bean(name = BATCH_JOB_JOBDESK_FULLRELOAD)
    public Job getJobdeskFullReloadJob() throws IOException {

        List<Job> jobArray = Arrays.asList(loadAllLocationsIntoJobdeskJob, loadAllVacanciesIntoJobdeskJob);

        SimpleJobBuilder jobBuilder = jobs.get(BATCH_JOB_JOBDESK_FULLRELOAD)
                .incrementer(new RunIdIncrementer())
                //.preventRestart()
                .start(initializeTargetElasticSearchIndex());

        //steps.get()
        jobArray.forEach(j -> jobBuilder.next(
                steps.get(BATCH_JOB_JOBDESK_FULLRELOAD + "-" + j.getName()).job(j).build()));

        // FIXME: should wait until bulk loading has terminated prior to do the switch...
        jobBuilder.next(switchElasticSearchIndex());

        return jobBuilder.build();
    }

    private Step initializeTargetElasticSearchIndex() throws IOException {

        Tasklet t = (contribution, context) -> {

            final String newIndexName = masterToJobdeskSettings.getElasticSearchIndexName()
                    + "_" + datetimeFormatter.print(new DateTime());
            final String importAlias = masterToJobdeskSettings.getElasticSearchImportAlias();

            ClassPathResource classPathResource = new ClassPathResource(MAPPING_FILE);
            final String indexDefinition = IOUtils.toString(classPathResource.getInputStream(), "UTF8");

            // FIXME check result!!!!
            elasticsearchClient.admin().indices().create(
                    new CreateIndexRequest(newIndexName).source(indexDefinition)
            ).actionGet();

            final AliasesExistResponse checkExistingAlias = elasticsearchClient.admin().indices()
                    .aliasesExist(new GetAliasesRequest(importAlias))
                    .actionGet();

            if (checkExistingAlias.exists()) {
                // Remove previous "import" alias
                final IndicesAliasesResponse indicesAliasesResponse = elasticsearchClient.admin().indices().aliases(
                        new IndicesAliasesRequest().addAliasAction(
                                IndicesAliasesRequest.AliasActions.remove().index("*").alias(importAlias)
                        )
                ).actionGet();
            }

            // Create new "import" alias
            final IndicesAliasesResponse indicesAliasesResponse = elasticsearchClient.admin().indices().aliases(
                    new IndicesAliasesRequest().addAliasAction(
                            IndicesAliasesRequest.AliasActions.add().index(newIndexName).alias(importAlias)
                    )
            ).actionGet();


            // FIXME check request results !!!!
            return RepeatStatus.FINISHED;
        };

        return steps.get(BATCH_JOB_JOBDESK_FULLRELOAD + "-initializeTargetElasticSearchIndex").tasklet(t).build();
    }

    private Step switchElasticSearchIndex() {

        Tasklet t = (contribution, context) -> {

            final String activeAlias = masterToJobdeskSettings.getElasticSearchIndexName();
            final String importAlias = masterToJobdeskSettings.getElasticSearchImportAlias();
            final String toBeDeletedAlias = masterToJobdeskSettings.getElasticSearchToDeleteAlias();

            final AliasesExistResponse checkExistingIndex = elasticsearchClient.admin().indices()
                    .aliasesExist(new GetAliasesRequest(activeAlias))
                    .actionGet();

            if (checkExistingIndex.exists()) {

                //
                // Mark previous index as "to be deleted" via a new alias
                //
                final IndicesAliasesResponse indicesAliasesResponse = elasticsearchClient.admin().indices().aliases(
                        new IndicesAliasesRequest()
                                .addAliasAction(IndicesAliasesRequest.AliasActions.add().index(activeAlias).alias(toBeDeletedAlias))
                                .addAliasAction(IndicesAliasesRequest.AliasActions.remove().index("*").alias(activeAlias))
                                .addAliasAction(IndicesAliasesRequest.AliasActions.add().index(importAlias).alias(activeAlias))
                ).actionGet();

                //
                // Change the "current" alias to point to the new index ^ ^
                //

                //
                // Remove the previous index by its "to be deleted" alias
                //
                DeleteIndexResponse checkDeletePreviousIndex = elasticsearchClient.admin().indices().delete(
                        new DeleteIndexRequest(toBeDeletedAlias))
                        .actionGet();

            } else {
                // Simply create the new alias
                final IndicesAliasesResponse indicesAliasesResponse = elasticsearchClient.admin().indices().aliases(
                        new IndicesAliasesRequest()
                                .addAliasAction(IndicesAliasesRequest.AliasActions.add().index(importAlias).alias(activeAlias))
                ).actionGet();
            }


            // FIXME check request results !!!!
            return RepeatStatus.FINISHED;

        };

        return steps.get(BATCH_JOB_JOBDESK_FULLRELOAD + "-switchElasticSearchIndex").tasklet(t).build();

    }
}
