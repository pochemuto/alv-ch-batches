/**
 * This class is generated by jOOQ
 */
package ch.alv.batches.commons.sql.jooq.tables;


import ch.alv.batches.commons.sql.jooq.Keys;
import ch.alv.batches.commons.sql.jooq.Public;
import ch.alv.batches.commons.sql.jooq.tables.records.TestJooqRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class TestJooq extends TableImpl<TestJooqRecord> {

	private static final long serialVersionUID = -305809273;

	/**
	 * The reference instance of <code>public.test_jooq</code>
	 */
	public static final TestJooq TEST_JOOQ = new TestJooq();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<TestJooqRecord> getRecordType() {
		return TestJooqRecord.class;
	}

	/**
	 * The column <code>public.test_jooq.id</code>.
	 */
	public final TableField<TestJooqRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>public.test_jooq.val1</code>.
	 */
	public final TableField<TestJooqRecord, String> VAL1 = createField("val1", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * Create a <code>public.test_jooq</code> table reference
	 */
	public TestJooq() {
		this("test_jooq", null);
	}

	/**
	 * Create an aliased <code>public.test_jooq</code> table reference
	 */
	public TestJooq(String alias) {
		this(alias, TEST_JOOQ);
	}

	private TestJooq(String alias, Table<TestJooqRecord> aliased) {
		this(alias, aliased, null);
	}

	private TestJooq(String alias, Table<TestJooqRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<TestJooqRecord> getPrimaryKey() {
		return Keys.TEST_JOOQ_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<TestJooqRecord>> getKeys() {
		return Arrays.<UniqueKey<TestJooqRecord>>asList(Keys.TEST_JOOQ_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestJooq as(String alias) {
		return new TestJooq(alias, this);
	}

	/**
	 * Rename this table
	 */
	public TestJooq rename(String name) {
		return new TestJooq(name, null);
	}
}
