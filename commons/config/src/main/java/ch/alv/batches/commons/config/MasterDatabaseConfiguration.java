package ch.alv.batches.commons.config;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

// Workaround to avoid that the default BatchConfigurer refuses
// to run in a multi-datasource context.
// "the default BatchConfigurer the context must contain no more thanone DataSource, found 2"
// see http://stackoverflow.com/a/29165232 (hint for googling: "thanone" typo is indeed in the exception message)
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
@Configuration
public class MasterDatabaseConfiguration {

    @Bean(name = "alvchMasterDataSource")
    @ConfigurationProperties(prefix="ch.alv.master.database")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "alvchMasterJooq")
    public DSLContext masterJooq() throws SQLException {
        return DSL.using(masterDataSource().getConnection());
    }
}