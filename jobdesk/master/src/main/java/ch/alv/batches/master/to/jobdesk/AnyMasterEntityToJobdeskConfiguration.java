package ch.alv.batches.master.to.jobdesk;

import org.elasticsearch.client.Client;
import org.jooq.DSLContext;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class AnyMasterEntityToJobdeskConfiguration {

    @Resource
    MasterToJobdeskSettings masterToJobdeskSettings;

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

    protected String getTargetIndex() {
        return masterToJobdeskSettings.getElasticSearchImportAlias();
    }

}
