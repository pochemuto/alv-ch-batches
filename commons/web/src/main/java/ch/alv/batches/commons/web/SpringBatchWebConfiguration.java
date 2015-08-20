package ch.alv.batches.commons.web;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBatchWebConfiguration {

    /**
     * Use this post-processor bean to register all jobs as they are created,
     * as de.codecentric.spring-boot-starter-batch-web relies on the jobRegistry.
     *
     * See also
     * Section 4.6.2 of http://docs.spring.io/spring-batch/reference/html/configureJob.html
     * http://stackoverflow.com/questions/28850051/nosuchjobexception-when-running-a-job-programmatically-in-spring-batch
     * https://github.com/jbbarquero/spring-batch-sample/blob/3968167e2110f27a07975c6339f1f0c1698c49c6/src/main/java/com/malsolo/springframework/batch/sample/BatchConfiguration.java#L92-L99
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
