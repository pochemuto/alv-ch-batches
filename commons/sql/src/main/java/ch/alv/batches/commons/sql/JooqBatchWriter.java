package ch.alv.batches.commons.sql;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// FIXME allow a mode without ANY COMMIT!!!??? 
public class JooqBatchWriter implements ItemWriter<UpdatableRecord<?>> {

    final static Logger logger = LoggerFactory.getLogger(JooqBatchWriter.class);

    protected Connection connection = null;
    protected DSLContext jooq = null;

    public JooqBatchWriter(DSLContext jooq) {
        this.jooq = jooq;
        this.connection = jooq.configuration().connectionProvider().acquire();
    }

    public void setAutoCommit(boolean enableAutoCommit) throws SQLException {
        this.connection.setAutoCommit(enableAutoCommit);
    }

    @Override
    public void write(List<? extends UpdatableRecord<?>> items) throws SQLException {
        try {
            logger.info("Store " + items.size() + " records.");
            jooq.batchStore(items).execute();
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
