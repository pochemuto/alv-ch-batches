package ch.alv.batches.legacy.to.master;

import static java.lang.String.format;
import static java.util.stream.IntStream.range;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.alv.batches.legacy.to.master.jooq.tables.records.JobLanguageRecord;

class JobRecordMapper {
	static final String LANGUAGE_CODE_COLUMN_FORMAT = "SK%d_SPRACHE_CODE";
	static final Integer JOB_RECORD_BEGIN_COLUMN = 0;
	static final Integer JOB_RECORD_END_COLUMN = 5;

	List<JobLanguageRecord> mapRecords(ResultSet rs) {
		return mapRecords(rs, JOB_RECORD_BEGIN_COLUMN, JOB_RECORD_END_COLUMN);
	}

	List<JobLanguageRecord> mapRecords(ResultSet rs, int start, int end) {
		return range(start, end)
				.mapToObj(no -> mapRecord(rs, no))
				.filter(Objects::nonNull)
				.filter(record -> record.getLanguageId() != null)
				.collect(Collectors.toList());
	}

	JobLanguageRecord mapRecord(ResultSet rs, int no) {
		try {
			String iso6391 = rs.getString(format(LANGUAGE_CODE_COLUMN_FORMAT, no));
			return iso6391 != null ? recordWithLanguageCode(iso6391) : null;
		} catch (SQLException e) {
			return null;
		}
	}

	private JobLanguageRecord recordWithLanguageCode(String iso6391) {
		JobLanguageRecord record = new JobLanguageRecord();
		record.setLanguageId(Iso6391LanguageCode.toLanguageCode(iso6391));
		return record;
	}
}