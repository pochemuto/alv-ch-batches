package ch.alv.batches.master.to.jobdesk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MasterToJobdeskSettings {

    public static final String ALIAS_IMPORT_SUFFIX = "_import";
    public static final String ALIAS_DELETE_SUFFIX = "_todelete";

    public static final String MAPPING_FILE = "elasticsearch-jobdesk.json";

    @Value("${ch.alv.jobdesk.elasticsearch.index:jobdesk}")
    protected String elasticsearchIndexName;

    public String getElasticSearchIndexName() {
        return this.elasticsearchIndexName;
    }

    public String getElasticSearchImportAlias() {
        return this.elasticsearchIndexName + ALIAS_IMPORT_SUFFIX;
    }

    public String getElasticSearchToDeleteAlias() {
        return this.elasticsearchIndexName + ALIAS_DELETE_SUFFIX;
    }

}
