/**
 * This class is generated by jOOQ
 */
package ch.alv.batches.commons.sql.jooq;


import ch.alv.batches.commons.sql.jooq.tables.TestJooq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Jobroom extends SchemaImpl {

    private static final long serialVersionUID = -847756524;

    /**
     * The reference instance of <code>JOBROOM</code>
     */
    public static final Jobroom JOBROOM = new Jobroom();

    /**
     * The table <code>JOBROOM.TEST_JOOQ</code>.
     */
    public final TestJooq TEST_JOOQ = ch.alv.batches.commons.sql.jooq.tables.TestJooq.TEST_JOOQ;

    /**
     * No further instances allowed
     */
    private Jobroom() {
        super("JOBROOM", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            TestJooq.TEST_JOOQ);
    }
}