package ch.alv.batches.companydata.configuration;

import ch.alv.batches.companydata.Company;
import ch.alv.batches.companydata.SqlStatements;
import ch.alv.batches.companydata.reader.FtpAvgFirmenStaxEventItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * Provides all beans required to download the avgFirmen file via FTP and to import its data into a
 * data staging table.
 *
 * @since 1.0.0
 */
@Configuration
public class ImportAvgFirmenXmlToStagingTableStepConfiguration {

    private static final String SCAN_PACKAGES = "ch.alv.batches.companydata.jaxb";

    private static final String[] JAXB_FRAGMENT_ROOT_NAMES = new String[] { "Betrieb" };

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private DataSource dataSource;

    @Value("${ch.alv.batch.companydata.avgfirmen.url}")
    private String avgFirmenUrl;

    @Bean(name = "importXmlToStagingTableStep")
    public Step importXmlToStagingTableStep() throws MalformedURLException, URISyntaxException {
        return stepBuilderFactory.get("importXmlToStagingTableStep")
                .<Company, Company>chunk(10)
                .reader(avgFirmenXmlReader())
                .writer(stagingCompanyWriter())
                .build();
    }

    @Bean(name = "avgFirmenXmlReader")
    public ItemReader<Company> avgFirmenXmlReader() throws MalformedURLException, URISyntaxException {
        FtpAvgFirmenStaxEventItemReader staxEventItemReader = new FtpAvgFirmenStaxEventItemReader();
        staxEventItemReader.setFragmentRootElementNames(JAXB_FRAGMENT_ROOT_NAMES);
        staxEventItemReader.setResource(new EncodedResource(new UrlResource(avgFirmenUrl), Charset.forName("UTF8")).getResource());
        staxEventItemReader.setUnmarshaller(avgFirmenUnmarshaller());
        return staxEventItemReader;
    }

    @Bean(name = "avgFirmenUnmarshaller")
    public Unmarshaller avgFirmenUnmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(SCAN_PACKAGES);
        return jaxb2Marshaller;
    }

    @Bean(name = "stagingCompanyWriter")
    public ItemWriter<Company> stagingCompanyWriter() {
        JdbcBatchItemWriter<Company> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(SqlStatements.INSERT_STAGED_COMPANY);
        writer.setDataSource(dataSource);
        return writer;
    }

}
