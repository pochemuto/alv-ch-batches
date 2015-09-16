package ch.alv.batches.commons.sql;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

public class JdbcCursorItemReaderFactory {

    public static <T> ItemReader<T> buildJdbcCursorIteamReader(Class<T> mappedClass, String selectQuery, Integer chunkSize, DataSource dataSource) {

        JdbcCursorItemReader reader = new JdbcCursorItemReader();

        reader.setRowMapper(new BeanPropertyRowMapper(mappedClass));
        reader.setFetchSize(chunkSize);
        reader.setSql(selectQuery);
        reader.setDataSource(dataSource);

        return reader;

    }

}
