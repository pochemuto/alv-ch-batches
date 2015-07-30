package ch.alv.batches.companydata.configuration;

import ch.alv.batches.companydata.Company;
import ch.alv.batches.companydata.SqlStatements;
import ch.alv.batches.companydata.reader.CompanyRowMapper;
import ch.alv.batches.companydata.reader.DummyItemPreparedStatementSetter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides all beans required to detect obsolete companies in the staging table and to propagate the new
 * state to the AVG_FIRMEN table.
 *
 * @since 1.0.0
 */
@Configuration
public class MarkToDeleteCompaniesStepConfiguration {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Resource
    private DataSource dataSource;

    @Resource
    private StepBuilderFactory stepBuilderFactory;


    @Bean(name = "markToDeleteCompaniesStep")
    public Step markToDeleteCompaniesStep() throws MalformedURLException, URISyntaxException {
        return stepBuilderFactory.get("markToDeleteCompaniesStep")
                .<Company, Company>chunk(10)
                .reader(toDeleteCompaniesSelector())
                .writer(toDeleteCompaniesUpdater())
                .build();
    }

    @Bean(name = "toDeleteCompaniesSelector")
    public ItemReader<Company> toDeleteCompaniesSelector() {
        JdbcCursorItemReader<Company> reader = new JdbcCursorItemReader<>();
        reader.setSql(SqlStatements.SELECT_COMPANIES_FOR_TODELETE_MARKERS);
        reader.setDataSource(dataSource);
        reader.setRowMapper(new CompanyRowMapper());
        return reader;
    }

    @Bean(name = "toDeleteCompaniesUpdater")
    public ItemWriter<Company> toDeleteCompaniesUpdater() {
        JdbcBatchItemWriter<Company> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter(new DummyItemPreparedStatementSetter<>());
        writer.setSql(String.format(SqlStatements.UPDATE_TODELETE_COMPANY_TEMPLATE, DATE_FORMAT.format(new Date())));
        writer.setDataSource(dataSource);
        return writer;
    }
}
