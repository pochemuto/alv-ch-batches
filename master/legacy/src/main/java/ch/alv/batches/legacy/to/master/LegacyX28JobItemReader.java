package ch.alv.batches.legacy.to.master;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import ch.alv.batches.legacy.to.master.jooq.tables.records.JobRecord;
import org.jooq.UpdatableRecord;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class LegacyX28JobItemReader extends JdbcCursorItemReader<Collection<? extends UpdatableRecord<?>>> {

	private BeanPropertyRowMapper<JobRecord> jobMapper = new BeanPropertyRowMapper(JobRecord.class);

	public LegacyX28JobItemReader(String selectQuery, int fetchSize, DataSource dataSource) {
		this.setSql(selectQuery);
		this.setFetchSize(fetchSize);
		this.setDataSource(dataSource);
	}

	@Override
	protected Collection<? extends UpdatableRecord<?>> readCursor(ResultSet rs, int currentRow) throws SQLException {
		List<UpdatableRecord<?>> records = new LinkedList<>();
		records.add(jobMapper.mapRow(rs, currentRow));
		records.addAll(new JobRecordMapper().mapRecords(rs));
		return records;
	}
}
