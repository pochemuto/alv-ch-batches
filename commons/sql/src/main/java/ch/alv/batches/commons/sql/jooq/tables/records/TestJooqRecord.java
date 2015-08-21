/**
 * This class is generated by jOOQ
 */
package ch.alv.batches.commons.sql.jooq.tables.records;


import ch.alv.batches.commons.sql.jooq.tables.TestJooq;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TestJooqRecord extends UpdatableRecordImpl<TestJooqRecord> implements Record2<Integer, String> {

	private static final long serialVersionUID = -1454819942;

	/**
	 * Setter for <code>public.test_jooq.id</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.test_jooq.id</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>public.test_jooq.val1</code>.
	 */
	public void setVal1(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.test_jooq.val1</code>.
	 */
	public String getVal1() {
		return (String) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
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
	public Integer value1() {
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
	public TestJooqRecord value1(Integer value) {
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
	public TestJooqRecord values(Integer value1, String value2) {
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
	public TestJooqRecord(Integer id, String val1) {
		super(TestJooq.TEST_JOOQ);

		setValue(0, id);
		setValue(1, val1);
	}
}
