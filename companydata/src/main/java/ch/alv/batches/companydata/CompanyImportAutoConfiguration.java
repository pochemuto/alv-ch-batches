package ch.alv.batches.companydata;

import ch.alv.batches.companydata.converter.AvgFirmaToCompanyConverter;
import ch.alv.batches.companydata.jaxb.AvgFirma;
import ch.alv.batches.connect.reader.FtpFileReader;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * AutoConfig of the Company import job.
 *
 * Currently it contains logic to support the listed data providers:
 * - avg firmen (AVAMPSTS.xml / source is a remote xml file grabbed via ftp, written to table 'AVG_FIRMEN')
 *
 * @since 1.0.0
 */
@ComponentScan
@EnableBatchProcessing
@Configuration
public class CompanyImportAutoConfiguration {

    @Value("${ch.alv.batch.companydata.avgfirmen.url}")
    private String avgFirmenUrl;

    @Resource
    private DataSource dataSource;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "importCompaniesJob")
    public Job importAdminJobsJob(JobBuilderFactory jobs) throws IOException, URISyntaxException {
        return jobs.get("importCompaniesJob")
                .incrementer(new RunIdIncrementer())
                .flow(avgFirmenXmlToDbImport())
                .end()
                .build();
    }

    @Bean(name = "avgFirmenXmlToDbImport")
    public Step avgFirmenXmlToDbImport() throws MalformedURLException, URISyntaxException {
        return stepBuilderFactory.get("avgFirmenXmlToDbImport")
                .<AvgFirma, Company>chunk(10)
                .reader(avgFirmenXmlReader())
                .processor(avgFirmaToCompanyConverter())
                .writer(adminJobJdbcWriter())
                .build();
    }

    @Bean(name = "avgFirmenXmlReader")
    public ItemReader<AvgFirma> avgFirmenXmlReader() throws MalformedURLException, URISyntaxException {
        StaxEventItemReader<AvgFirma> staxEventItemReader = new FtpFileReader<>("localhost", 21, "ftpdev", "ftpdev", "/AVAMPSTS.xml", "AVAMPSTS.xml");
        staxEventItemReader.setFragmentRootElementName("betrieb");
        staxEventItemReader.setResource(new ClassPathResource("AVAMPSTS.xml"));
        staxEventItemReader.setUnmarshaller(avgFirmaUnmarshaller());
        return staxEventItemReader;
    }

    @Bean(name = "avgFirmaUnmarshaller")
    public Unmarshaller avgFirmaUnmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("ch.alv.batches.companydata.jaxb");
        return jaxb2Marshaller;
    }

    @Bean(name = "avgFirmaToCompanyConverter")
    public ItemProcessor<AvgFirma, Company> avgFirmaToCompanyConverter() {
        return new AvgFirmaToCompanyConverter();
    }

    @Bean(name = "companyJdbcWriter")
    public ItemWriter<Company> adminJobJdbcWriter() {
        JdbcBatchItemWriter<Company> writer = new JdbcBatchItemWriter<>();

        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO OSTE_ADMIN (ID, " +
                "BETID, " +
                "EMAIL, " +
                "KANTON, " +
                "NAME, " +
                "NAME2, " +
                "ORT, " +
                "PLZ, " +
                "STRASSE, " +
                "TELEFONNUMMER, " +
                "TODELETE) " +
                "VALUES (" +
                ":id, " +
                ":companyId, " +
                ":email, " +
                ":canton, " +
                ":name, " +
                ":name2, " +
                ":city, " +
                ":zip, " +
                ":street, " +
                ":phone, " +
                ":toDelete)");
        writer.setDataSource(dataSource);
        return writer;
    }

}
