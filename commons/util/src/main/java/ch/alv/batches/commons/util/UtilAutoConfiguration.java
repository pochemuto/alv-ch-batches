package ch.alv.batches.commons.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilAutoConfiguration {

    @Bean
    public SpringBatchTestHelper springBatchTestHelper() {
        return new SpringBatchTestHelper();
    }

}
