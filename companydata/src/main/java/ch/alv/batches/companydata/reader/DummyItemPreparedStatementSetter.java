package ch.alv.batches.companydata.reader;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A {@link ItemPreparedStatementSetter} implementation that doesn't replace any param.
 *
 */
public class DummyItemPreparedStatementSetter<T> implements ItemPreparedStatementSetter<T> {
    @Override
    public void setValues(Object item, PreparedStatement ps) throws SQLException {
        // the dummy replaces nothing
    }
}
