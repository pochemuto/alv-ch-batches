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
 * Provides all beans required to detect existing companies in the staging table and to propagate the new
 * state to the AVG_FIRMEN table.
 *
 * @since 1.0.0
 */
@Configuration
public class UpdateExistingCompaniesStepConfiguration {

    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Resource
    private DataSource dataSource;

    @Bean(name = "updateExistingCompaniesStep")
    public Step updateExistingCompaniesStep() throws MalformedURLException, URISyntaxException {
        return stepBuilderFactory.get("updateExistingCompaniesStep")
                .<Company, Company>chunk(10)
                .reader(existingCompaniesSelector())
                .writer(companyUpdater())
                .build();
    }

    @Bean(name = "existingCompaniesSelector")
    public ItemReader<Company> existingCompaniesSelector() {
        JdbcCursorItemReader<Company> reader = new JdbcCursorItemReader<>();
        reader.setSql(SqlStatements.SELECT_COMPANIES_FOR_UPDATES);
        reader.setRowMapper(new CompanyRowMapper());
        reader.setDataSource(dataSource);
        return reader;
    }

    @Bean(name = "companyUpdater")
    public ItemWriter<Company> companyUpdater() {
        JdbcBatchItemWriter<Company> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(SqlStatements.UPDATE_COMPANY);
        writer.setDataSource(dataSource);
        return writer;
    }


}
