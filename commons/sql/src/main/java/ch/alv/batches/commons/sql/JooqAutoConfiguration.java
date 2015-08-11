package ch.alv.batches.commons.sql;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class JooqAutoConfiguration {

    @Resource
    private DataSource dataSource;

    @Bean
    public DSLContext jooq() throws SQLException {
        return DSL.using(dataSource.getConnection());
    }

}
