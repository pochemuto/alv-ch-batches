/**
 * This class is generated by jOOQ
 */
package ch.alv.batches.commons.sql.jooq.tables.records;


import ch.alv.batches.commons.sql.jooq.tables.TestJooq;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;


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
public class TestJooqRecord extends UpdatableRecordImpl<TestJooqRecord> implements Record2<BigDecimal, String> {

    private static final long serialVersionUID = 1582178190;

    /**
     * Setter for <code>JOBROOM.TEST_JOOQ.ID</code>.
     */
    public void setId(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>JOBROOM.TEST_JOOQ.ID</code>.
     */
    public BigDecimal getId() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>JOBROOM.TEST_JOOQ.VAL1</code>.
     */
    public void setVal1(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>JOBROOM.TEST_JOOQ.VAL1</code>.
     */
    public String getVal1() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<BigDecimal> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<BigDecimal, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<BigDecimal, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return TestJooq.TEST_JOOQ.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return TestJooq.TEST_JOOQ.VAL1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getVal1();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestJooqRecord value1(BigDecimal value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestJooqRecord value2(String value) {
        setVal1(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestJooqRecord values(BigDecimal value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TestJooqRecord
     */
    public TestJooqRecord() {
        super(TestJooq.TEST_JOOQ);
    }

    /**
     * Create a detached, initialised TestJooqRecord
     */
    public TestJooqRecord(BigDecimal id, String val1) {
        super(TestJooq.TEST_JOOQ);

        set(0, id);
        set(1, val1);
    }
}
