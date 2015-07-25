package ch.alv.batches.partnerjobs;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import javax.sql.DataSource;

/**
 * A custom JdbcBatchItemWriter for adminJobs.
 *
 * @since: 1.0.0
 */
public class PartnerJobJdbcItemWriter extends JdbcBatchItemWriter<PartnerJob> {

    public PartnerJobJdbcItemWriter(DataSource dataSource) {
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        setSql("INSERT INTO people (ID, " +
                    "BEZEICHNUNG, " +
                    "BESCHREIBUNG, " +
                    "BERUFSGRUPPE, " +
                    "UNT_NAME, " +
                    "ARBEITSORT_PLZ, " +
                    "PENSUM_VON, " +
                    "PENSUM_BIS, " +
                    "URL_DETAIL, " +
                    "ANMELDE_DATUM) " +
                "VALUES (" +
                    ":id, " +
                    ":title, " +
                ":description, " +
                ":jobGroup, " +
                ":companyName, " +
                ":jobLocation, " +
                ":quotaFrom, " +
                ":quotaTo, " +
                ":urlDetail, " +
                ":onlineSince)");
        setDataSource(dataSource);
    }

}
