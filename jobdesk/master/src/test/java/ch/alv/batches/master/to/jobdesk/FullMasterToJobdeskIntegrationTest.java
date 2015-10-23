package ch.alv.batches.master.to.jobdesk;

import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import javax.annotation.Resource;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FullMasterToJobdeskIntegrationTest extends MasterToJobdeskIntegrationTest {

    @Resource
    private Job jobdeskFullReloadJob;

    @Override
    protected void initJobdeskElasticsearch() {
        // ElasticSearch indices and aliases are directly managed by the FullImport Batch Job
        this.elasticsearchIndexName = masterToJobdeskSettings.getElasticSearchIndexName();
    }

    // FIXME address @BeforeClass static / Spring DI conflicts, to be able to split this monolithic Test Suite
    @Test
    public void doTests() throws
    //public void importAllLocations() throws
            IOException,
            InterruptedException,
            NoSuchJobException,
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            JobParametersNotFoundException {

//        //
//        // Ensure that the Testing ES Index is initially empty
//        //
//        GetResponse response = elasticsearchClient.prepareGet(
//                elasticsearchIndexName, LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, "3110"
//        ).execute().actionGet();
//        Assert.assertNull(response.getSourceAsString());

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(jobdeskFullReloadJob));
        waitUntilElasticSearchBulkIsFinished(VacanciesToJobdeskConfiguration.ELASTICSEARCH_TYPE, 13591);
        waitUntilElasticSearchBulkIsFinished(LocationsToJobdeskConfiguration.ELASTICSEARCH_TYPE, 4168);

        final AliasesExistResponse checkExistingAlias = elasticsearchClient.admin().indices()
                .aliasesExist(new GetAliasesRequest(masterToJobdeskSettings.getElasticSearchIndexName()))
                .actionGet();
        assertTrue(checkExistingAlias.exists());
        // TODO wait until alias is created... + timeout safety
        // TODO assert about aliases, etc

        // TODO repeat to check existing will be replaced (and old one removed)
    }

}
