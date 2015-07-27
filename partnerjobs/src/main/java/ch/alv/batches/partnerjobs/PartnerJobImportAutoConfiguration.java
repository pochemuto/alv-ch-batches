package ch.alv.batches.partnerjobs;

import ch.alv.batches.partnerjobs.jaxb.ProspectiveJob;
import ch.alv.batches.partnerjobs.converter.ProspectiveXmlToAdminJobConverter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * AutoConfig of the adminJobs-import-starter
 *
 * @since 1.0.0
 */
@ComponentScan
@EnableBatchProcessing
@EnableAutoConfiguration
@Configuration
public class PartnerJobImportAutoConfiguration  {

    @Value("${ch.alv.batch.prospective.url}")
    private String prospectiveHttpUrl;

    @Value("${ch.alv.batch.prospective.localFileName:prospectiveData.xml}")
    private String prospectiveDownloadedFileName;

    @Value("${ch.alv.batch.downloadDirectory:downloads}")
    private String downloadDirectory;

    @Value("${ch.alv.batch.archiveDirectory:archive}")
    private String archiveDirectory;

    private RelaxedPropertyResolver propertyResolver;

    @Resource
    private DataSource dataSource;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "importAdminJobsJob")
    public Job importAdminJobsJob(JobBuilderFactory jobs) throws IOException, URISyntaxException {
        return jobs.get("importAdminJobsJob")
                .incrementer(new RunIdIncrementer())
                .flow(prospectiveXmlToDbImportStep())
                .end()
                .build();
    }

    @Bean(name = "prospectiveXmlToDbImportStep")
    public Step prospectiveXmlToDbImportStep() throws MalformedURLException, URISyntaxException {
        return stepBuilderFactory.get("prospectiveXmlToDbImportStep")
                .<ProspectiveJob, PartnerJob>chunk(10)
                .reader(prospectiveXmlJobReader())
                .processor(prospectiveXmlToAdminJobConverter())
                .writer(adminJobJdbcWriter())
                .build();
    }

    @Bean(name = "prospectiveXmlJobReader")
    public ItemReader<ProspectiveJob> prospectiveXmlJobReader() throws MalformedURLException, URISyntaxException {
        StaxEventItemReader<ProspectiveJob> staxEventItemReader = new StaxEventItemReader();
        staxEventItemReader.setFragmentRootElementName("inserat");
        staxEventItemReader.setResource(new UrlResource(prospectiveHttpUrl));
        staxEventItemReader.setUnmarshaller(prospectiveXmlJobUnmarshaller());
        return staxEventItemReader;
    }

    @Bean(name = "prospectiveXmlJobUnmarshaller")
    public Unmarshaller prospectiveXmlJobUnmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("ch.alv.batches.partnerjobs.jaxb");
        return jaxb2Marshaller;
    }

    @Bean(name = "prospectiveXmlToAdminJobConverter")
    public ItemProcessor<ProspectiveJob, PartnerJob> prospectiveXmlToAdminJobConverter() {
        return new ProspectiveXmlToAdminJobConverter();
    }

    @Bean(name = "adminJobJdbcWriter")
    public ItemWriter<PartnerJob> adminJobJdbcWriter() {
        JdbcBatchItemWriter<PartnerJob> writer = new JdbcBatchItemWriter();

        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO OSTE_ADMIN (ID, " +
                "BEZEICHNUNG, " +
                "BESCHREIBUNG, " +
                "BERUFSGRUPPE, " +
                "UNT_NAME, " +
                "ARBEITSORT_PLZ, " +
                "PENSUM_VON, " +
                "PENSUM_BIS, " +
                "URL_DETAIL, " +
                "ANMELDE_DATUM," +
                "SPRACHE) " +
                "VALUES (" +
                ":id, " +
                ":title, " +
                ":description, " +
                ":jobGroup, " +
                ":companyName, " +
                ":jobLocation, " +
                ":quotaFrom, " +
                ":quotaTo, " +
                ":urlDetail, " +
                ":onlineSince, " +
                ":language)");
        writer.setDataSource(dataSource);
        return writer;
    }

}
