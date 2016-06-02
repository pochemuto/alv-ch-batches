package ch.alv.batches.commons.test.springbatch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

@Component
public class SpringBatchTestHelper {

    private static String RUN_ID_PARAMETER_KEY = "run.id";

    @Value("classpath:org/springframework/batch/core/schema-drop-postgresql.sql")
    private org.springframework.core.io.Resource dropSpringBatchSchemaScript;

    @Value("classpath:org/springframework/batch/core/schema-postgresql.sql")
    private org.springframework.core.io.Resource createSpringBatchSchemaScript;

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private DataSource dataSource;

    private JobParameters lastExecutedJobParameters = null;
    private JobParametersIncrementer idIncrementer = new RunIdIncrementer();

    // The method below can be used when SpringBatch metadata tables are not stored
    // in a separate In-Memory database, but for instance stored in the main JDBC
    // datasource used to store application data.

//    public void initializeSpringBatchPostgresqlSchema() {
//        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(dropSpringBatchSchemaScript);
//        populator.addScript(createSpringBatchSchemaScript);
//        DatabasePopulatorUtils.execute(populator, dataSource);
//    }

    public ExitStatus runJob(Job job) throws
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        return runJob(job, null);
    }

    public ExitStatus runJob(Job job, Map<String, JobParameter> customJobParameters) throws
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {

        JobParameters parameters = idIncrementer.getNext(lastExecutedJobParameters);

        if (customJobParameters != null) {
            customJobParameters.put(RUN_ID_PARAMETER_KEY, parameters.getParameters().get(RUN_ID_PARAMETER_KEY));
            parameters = new JobParameters(customJobParameters);
        }

        lastExecutedJobParameters = parameters;
        JobExecution exec = jobLauncher.run(job, parameters);
        return exec.getExitStatus();
    }
}
