package ch.alv.batches.commons.test;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Component
public class SpringBatchTestHelper {

    @Value("classpath:org/springframework/batch/core/schema-drop-postgresql.sql")
    private org.springframework.core.io.Resource dropSpringBatchSchemaScript;

    @Value("classpath:org/springframework/batch/core/schema-postgresql.sql")
    private org.springframework.core.io.Resource createSpringBatchSchemaScript;

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private DataSource dataSource;

    private JobParametersIncrementer idIncrementer = new RunIdIncrementer();
    private JobParameters jobParameters = new JobParameters();

    public void initializeSpringBatchPostgresqlSchema() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(dropSpringBatchSchemaScript);
        populator.addScript(createSpringBatchSchemaScript);
        DatabasePopulatorUtils.execute(populator, dataSource);
    }

    public ExitStatus runJob(Job job) throws
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        jobParameters = idIncrementer.getNext(jobParameters);
        JobExecution exec = jobLauncher.run(job, jobParameters);
        return exec.getExitStatus();
    }
}
