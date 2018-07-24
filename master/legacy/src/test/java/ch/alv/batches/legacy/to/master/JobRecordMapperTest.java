package ch.alv.batches.legacy.to.master;

import static ch.alv.batches.legacy.to.master.JobRecordMapper.LANGUAGE_CODE_COLUMN_FORMAT;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ch.alv.batches.legacy.to.master.jooq.tables.records.JobLanguageRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobRecordMapperTest {

	private JobRecordMapper mapper = new JobRecordMapper();
	private String iso6391 = "es";
	@Mock
	private ResultSet rs;

	@Before
	public void setUp() throws Exception {
		when(rs.getString(format(LANGUAGE_CODE_COLUMN_FORMAT, 0))).thenReturn(iso6391);
	}

	@Test
	public void shouldMapRecord() {
		JobLanguageRecord record = mapper.mapRecord(rs, 0);

		assertThat(record, notNullValue());
		assertThat(record.getLanguageId(), equalTo(Iso6391LanguageCode.toLanguageCode(iso6391)));
	}

	@Test
	public void shouldNotMapRecordIfLanguageCodeColumnIdxNotExist() {
		JobLanguageRecord record = mapper.mapRecord(rs, 99);

		assertThat(record, nullValue());
	}

	@Test
	public void shouldNotMapRecordIfRetrieveOfLanguageCodeThrowException() throws SQLException {
		when(rs.getString(format(LANGUAGE_CODE_COLUMN_FORMAT, 0))).thenThrow(new SQLException());

		JobLanguageRecord record = mapper.mapRecord(rs, 0);

		assertThat(record, nullValue());
	}

	@Test
	public void shouldMapRecordWithEmptyLanguageIdIfRetrievedLanguageCodeIsNotValidIso6391() throws SQLException {
		when(rs.getString(format(LANGUAGE_CODE_COLUMN_FORMAT, 0))).thenReturn("hh");

		JobLanguageRecord record = mapper.mapRecord(rs, 0);

		assertThat(record, notNullValue());
		assertThat(record.getLanguageId(), nullValue());
	}

	@Test
	public void shouldMapRecordsReturnOneRecordIfOneLanguageCodeCanBeRetrieved() {
		List<JobLanguageRecord> records = mapper.mapRecords(rs);

		assertThat(records, hasSize(1));
	}
}