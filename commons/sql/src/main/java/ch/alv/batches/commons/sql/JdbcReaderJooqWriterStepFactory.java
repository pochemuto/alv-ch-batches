package ch.alv.batches.commons.sql;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import javax.sql.DataSource;

/**
 * TODO Explain why not implemented as Spring Bean Singleton (i.e. multiple JDBC sources, etc.)
 */
public class JdbcReaderJooqWriterStepFactory {

    private DataSource inputDataSource;
    private DSLContext outputJooq;
    private StepBuilderFactory steps;

    // TODO switch to Spring IoC ? Not sure if it makes sense / is easily doable
    public JdbcReaderJooqWriterStepFactory(DataSource inputDataSource, DSLContext outputJooq, StepBuilderFactory steps) {
        this.inputDataSource = inputDataSource;
        this.outputJooq = outputJooq;
        this.steps = steps;
    }

    /**
     *
     * @param chunkSize
     * @param selectQuery
     * @param targetClassFullName
     * @return
     * @throws ClassNotFoundException
     */
    public Step buildStep(String stepName, Integer chunkSize, String selectQuery, String targetClassFullName)
            throws ClassNotFoundException {
        Class mappedClass = Class.forName(targetClassFullName);

        return steps.get(stepName)
                .<UpdatableRecord<?>, UpdatableRecord<?>> chunk(chunkSize)
                .reader(JdbcCursorItemReaderFactory
                        .buildJdbcCursorIteamReader(mappedClass, selectQuery, chunkSize, inputDataSource))
                .writer(new JooqBatchWriter(outputJooq))
                .build();
    }
}
