package ch.alv.batches.commons.sql;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.sql.Connection;
import java.util.List;

public class JooqBatchWriter implements ItemWriter<UpdatableRecord<?>> {

    final static Logger logger = LoggerFactory.getLogger(JooqBatchWriter.class);

    protected Connection connection = null;
    protected DSLContext jooq = null;

    public JooqBatchWriter(DSLContext jooq) {
        this.jooq = jooq;
        this.connection = jooq.configuration().connectionProvider().acquire();
    }

    @Override
    public void write(List<? extends UpdatableRecord<?>> items) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Store " + items.size() + " records.");
        }
        jooq.batchStore(items).execute();
        if (!connection.getAutoCommit()) {
            connection.commit();
        }
    }
}
