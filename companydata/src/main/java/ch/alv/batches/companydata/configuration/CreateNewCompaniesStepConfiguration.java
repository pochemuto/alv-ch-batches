package ch.alv.batches.companydata.configuration;

import ch.alv.batches.companydata.Company;
import ch.alv.batches.companydata.SqlStatements;
import ch.alv.batches.companydata.reader.CompanyRowMapper;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Provides all beans required to detect new companies in the staging table and to create them in  the AVG_FIRMEN table.
 *
 * @since 1.0.0
 */
@Configuration
public class CreateNewCompaniesStepConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "createNewCompaniesStep")
    public Step createNewCompaniesStep() throws MalformedURLException, URISyntaxException {
        return stepBuilderFactory.get("createNewCompaniesStep")
                .<Company, Company>chunk(10)
                .reader(newCompaniesSelector())
                .writer(companyCreator())
                .build();
    }

    @Bean(name = "newCompaniesSelector")
    public ItemReader<Company> newCompaniesSelector() {
        JdbcCursorItemReader<Company> reader = new JdbcCursorItemReader<>();
        reader.setSql(SqlStatements.SELECT_COMPANIES_FOR_CREATION);
        reader.setRowMapper(new CompanyRowMapper());
        reader.setDataSource(dataSource);
        return reader;
    }

    @Bean(name = "companyCreator")
    public ItemWriter<Company> companyCreator() {
        JdbcBatchItemWriter<Company> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(SqlStatements.INSERT_COMPANY);
        writer.setDataSource(dataSource);
        return writer;
    }

}
