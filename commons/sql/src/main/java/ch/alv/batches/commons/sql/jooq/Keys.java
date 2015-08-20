/**
 * This class is generated by jOOQ
 */
package ch.alv.batches.commons.sql.jooq;


import ch.alv.batches.commons.sql.jooq.tables.TestJooq;
import ch.alv.batches.commons.sql.jooq.tables.records.TestJooqRecord;

import javax.annotation.Generated;

import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>public</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------


	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<TestJooqRecord> TEST_JOOQ_PKEY = UniqueKeys0.TEST_JOOQ_PKEY;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------


	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<TestJooqRecord> TEST_JOOQ_PKEY = createUniqueKey(TestJooq.TEST_JOOQ, TestJooq.TEST_JOOQ.ID);
	}
}
