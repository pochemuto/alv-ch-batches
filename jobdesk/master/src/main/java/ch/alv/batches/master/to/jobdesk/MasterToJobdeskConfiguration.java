package ch.alv.batches.master.to.jobdesk;

import org.elasticsearch.client.Client;
import org.jooq.DSLContext;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class MasterToJobdeskConfiguration {

    @Value("${ch.alv.jobdesk.elasticsearch.index:jobdesk}")
    protected String elasticsearchIndexName;

    @Resource
    protected Client elasticsearchClient;

    @Resource
    protected DSLContext jooq;

    @Resource
    protected DataSource alvchMasterDataSource;

    @Resource
    protected StepBuilderFactory steps;

    @Resource
    protected JobBuilderFactory jobs;

}
