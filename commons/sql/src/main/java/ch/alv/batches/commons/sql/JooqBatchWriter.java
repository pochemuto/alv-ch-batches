package ch.alv.batches.commons.sql;

import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.springframework.batch.item.ItemWriter;

import java.sql.Connection;
import java.util.List;

public class JooqBatchWriter implements ItemWriter<UpdatableRecord<?>> {

    protected Connection dbConnection = null;
    protected DSLContext jooq = null;

    public JooqBatchWriter(Connection connection) {
        dbConnection = connection;
        jooq = DSL.using(dbConnection);
    }

    @Override
    public void write(List<? extends UpdatableRecord<?>> items) throws Exception {
        jooq.batchStore(items).execute();
        if (!dbConnection.getAutoCommit()) {
            dbConnection.commit();
        }
    }
}
