package ch.alv.batches.commons.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBatchTestConfiguration {

    @Bean
    public ch.alv.batches.commons.test.SpringBatchTestHelper springBatchTestHelper() {
        return new ch.alv.batches.commons.test.SpringBatchTestHelper();
    }

}
