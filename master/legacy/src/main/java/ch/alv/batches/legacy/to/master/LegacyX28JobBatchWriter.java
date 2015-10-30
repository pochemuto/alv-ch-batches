package ch.alv.batches.legacy.to.master;

import ch.alv.batches.legacy.to.master.jooq.tables.Job;
import ch.alv.batches.legacy.to.master.jooq.tables.records.JobLanguageRecord;
import ch.alv.batches.legacy.to.master.jooq.tables.records.JobRecord;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static ch.alv.batches.legacy.to.master.jooq.Tables.*;
import static ch.alv.batches.legacy.to.master.jooq.Sequences.*;


// FIXME refactor with jooqBatchWriter common code!!!!
// FIXME allow a mode without ANY COMMIT!!!??? 
public class LegacyX28JobBatchWriter implements ItemWriter<List<UpdatableRecord<?>>> {

    final static Logger logger = LoggerFactory.getLogger(LegacyX28JobBatchWriter.class);

    protected Connection connection = null;
    protected DSLContext jooq = null;

    public LegacyX28JobBatchWriter(DSLContext jooq) {
        this.jooq = jooq;
        this.connection = jooq.configuration().connectionProvider().acquire();
    }

    public void setAutoCommit(boolean enableAutoCommit) throws SQLException {
        this.connection.setAutoCommit(enableAutoCommit);
    }

    @Override
    public void write(List<? extends List<UpdatableRecord<?>>> items) throws SQLException {
        try {
            List<JobLanguageRecord> jobLanguages = new LinkedList<>();
            List<JobRecord> jobsWithoutLanguageRequirements = new LinkedList<>();

            items.forEach(i -> {

                JobRecord job = (JobRecord)i.remove(0);

                if (i.isEmpty()) {
                    // no linked entities, this job can be stored in the batch process
                    jobsWithoutLanguageRequirements.add(job);
                } else{
                    // we store the job to get its ID from sequence, and will batch insert the language requirements
                    JobRecord x = jooq.newRecord(JOB);
                    x.from(job);
                    x.store();

                    i.forEach(c -> {
                        JobLanguageRecord l = (JobLanguageRecord) c;
                        l.setJobId(x.getId());
                        jobLanguages.add(l);
                    });
                }
            });

            jooq.batchStore(jobLanguages).execute();
            jooq.batchStore(jobsWithoutLanguageRequirements).execute();

            logger.info("batched jobs: " + jobsWithoutLanguageRequirements.size() + " (out of " + items.size() + ") and " + jobLanguages.size() + " batched language requirements");

            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            if (e.getCause() instanceof SQLException) {
                Exception nextException = ((SQLException) e.getCause()).getNextException();
                String nextExceptionMessage = "There was no next exception chained to " + e.getCause().getMessage();
                if (nextException != null) {
                    nextExceptionMessage = "Next Exception: " + nextException.getMessage();
                }
                logger.error(nextExceptionMessage);
            }
            throw e;
        }
    }

}
