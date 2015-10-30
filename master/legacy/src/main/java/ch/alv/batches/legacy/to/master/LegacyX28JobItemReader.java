package ch.alv.batches.legacy.to.master;

import ch.alv.batches.legacy.to.master.jooq.tables.Job;
import ch.alv.batches.legacy.to.master.jooq.tables.JobLanguage;
import ch.alv.batches.legacy.to.master.jooq.tables.records.JobLanguageRecord;
import ch.alv.batches.legacy.to.master.jooq.tables.records.JobRecord;
import org.jooq.UpdatableRecord;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static ch.alv.batches.legacy.to.master.jooq.Tables.JOB_LOCATION;

public class LegacyX28JobItemReader extends JdbcCursorItemReader<Collection<? extends UpdatableRecord<?>>> {

    BeanPropertyRowMapper<JobRecord> jobMapper = new BeanPropertyRowMapper(JobRecord.class);

    public LegacyX28JobItemReader(String selectQuery, int fetchSize, DataSource dataSource) {
        this.setSql(selectQuery);
        this.setFetchSize(fetchSize);
        this.setDataSource(dataSource);
        // FIXME this.setRowMapper();
    }

    /**
     * Assert that mandatory properties are set.
     *
     * @throws IllegalArgumentException if either data source or SQL properties
     * not set.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // FIXME skip super class checks that always would fail
        // TODO create fake objects ???
    }

    @Override
    protected Collection<? extends UpdatableRecord<?>> readCursor(ResultSet rs, int currentRow) throws SQLException {

        JobRecord job = jobMapper.mapRow(rs, currentRow);
        List<UpdatableRecord<?>> records = new LinkedList<>();
        records.add(job);

        for (int i = 1; i <= 5; i++ ) {
            final String columnPrefix = "SK" + i + "_";
            final String columnLanguageCode = columnPrefix + "SPRACHE_CODE";
            final String columnSpokenCode = columnPrefix + "MUENDLICH_CODE";
            final String columnWrittenCode = columnPrefix + "SCHRIFTLICH_CODE";

            if (rs.getString(columnLanguageCode) != null) {
                JobLanguageRecord jl = new JobLanguageRecord();

                jl.setLanguageId(Integer.parseInt(rs.getString(columnLanguageCode)));
                jl.setSkillSpoken(Short.parseShort(rs.getString(columnSpokenCode)));
                jl.setSkillWritten(Short.parseShort(rs.getString(columnWrittenCode)));

                records.add(jl);
            } else {
                break;
            }
        }

        return records;
    }
}
