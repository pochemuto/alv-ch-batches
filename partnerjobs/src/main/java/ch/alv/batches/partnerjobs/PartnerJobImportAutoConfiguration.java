package ch.alv.batches.partnerjobs;

import ch.alv.batches.connect.reader.HttpFileReader;
import ch.alv.batches.connect.writer.LocalDiskFileWriter;
import ch.alv.batches.partnerjobs.partner.ProspectiveXmlJob;
import ch.alv.batches.partnerjobs.partner.ProspectiveXmlJobReader;
import ch.alv.batches.partnerjobs.partner.ProspectiveXmlToAdminJobConverter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;

/**
 * AutoConfig of the adminJobs-import-starter
 *
 * @since 1.0.0
 */
@Configuration
@ComponentScan
@EnableBatchProcessing
public class PartnerJobImportAutoConfiguration {

    @Value("${ch.alv.batch.prospective.url}")
    private String prospectiveHttpUrl;

    @Value("${ch.alv.batch.prospective.localFileName:prospectiveData.xml}")
    private String prospectiveDownloadedFileName;

    @Value("${ch.alv.batch.workDirectory:tmp}")
    private String localDiskFolder;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource(name = "prospectiveHttpFileReader")
    private HttpFileReader prospectiveFileReader;

    @Resource
    private LocalDiskFileWriter localDiskFileWriter;

    @Resource(name = "prospectiveXmlReader")
    public ProspectiveXmlJobReader prospectiveXmlReader;

    @Resource(name = "adminJobDataOptimizingProcessor")
    public ProspectiveXmlToAdminJobConverter AdminJobDataOptimizingProcessor;

    @Resource(name = "adminJobJdbcItemWriter")
    private PartnerJobJdbcItemWriter adminJobJdbcItemWriter;

    @Bean
    public Job importAdminJobsJob(JobBuilderFactory jobs, JobExecutionListener listener) {
        return jobs.get("importAdminJobsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(prospectiveFileDownloadStep())
                .next(prospectiveXmlToDbImportStep())
                .end()
                .build();
    }

    @Bean(name = "prospectiveFileDownloadStep")
    public Step prospectiveFileDownloadStep() {
        return stepBuilderFactory.get("prospectiveFileDownloadStep")
                .<File, File>chunk(10)
                .reader(prospectiveFileReader)
                .writer(localDiskFileWriter)
                .build();
    }

    @Bean(name = "prospectiveXmlToDbImportStep")
    public Step prospectiveXmlToDbImportStep() {
        return stepBuilderFactory.get("prospectiveXmlToDbImportStep")
                .<ProspectiveXmlJob, PartnerJob>chunk(10)
                .reader(prospectiveXmlReader)
                .processor(AdminJobDataOptimizingProcessor)
                .writer(adminJobJdbcItemWriter)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalDiskFileWriter localDiskFileWriter() {
        return new LocalDiskFileWriter(localDiskFolder);
    }

    @Bean
    public HttpFileReader prospectiveHttpFileReader() {
        return new HttpFileReader(prospectiveHttpUrl, prospectiveDownloadedFileName);
    }

    @Bean(name = "prospectiveXmlJobReader")
    public ItemReader<ProspectiveXmlJob> prospectiveXmlJobReader() {
        StaxEventItemReader staxEventItemReader = new StaxEventItemReader<>();
        staxEventItemReader.setFragmentRootElementName("inserate");
        staxEventItemReader.setResource(new FileSystemResource(localDiskFolder + "/" + prospectiveDownloadedFileName));
        staxEventItemReader.setUnmarshaller(prospectiveXmlJobUnmarshaller());
        return staxEventItemReader;
    }

    @Bean(name = "prospectiveXmlJobUnmarshaller")
    public Unmarshaller prospectiveXmlJobUnmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("ch.alv.batches.partnerjobs.partner");
        return jaxb2Marshaller;
    }

    @Bean(name = "prospectiveXmlToAdminJobConverter")
    public ItemProcessor<ProspectiveXmlJob, PartnerJob> prospectiveXmlToAdminJobConverter() {

        return new ProspectiveXmlToAdminJobConverter();
    }

    @Bean(name = "adminJobJdbcWriter")
    public ItemWriter<PartnerJob> adminJobJdbcWriter(DataSource dataSource) {
        return new PartnerJobJdbcItemWriter(dataSource);
    }
}
