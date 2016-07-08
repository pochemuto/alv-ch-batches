package ch.alv.batches.boom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan("ch.alv.batches.commons.config, "
        + "ch.alv.batches.commons.web, "
//        + "ch.alv.batches.company.to.master, "
        + "ch.alv.batches.boom, "
        + "ch.alv.batches.partnerjob.to.master"
)
public class BoomApplication {

    @Primary
    @Bean
    public DataSource defaultSpringDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setName("alv-in-memory").build();
        return db;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BoomApplication.class);
        app.run(args);
    }

}

