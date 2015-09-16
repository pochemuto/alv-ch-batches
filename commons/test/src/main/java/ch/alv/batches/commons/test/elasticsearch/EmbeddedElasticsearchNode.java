package ch.alv.batches.commons.test.elasticsearch;


import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;

import java.io.File;
import java.io.IOException;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class EmbeddedElasticsearchNode {

    private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch-data";

    private final String dataDirectory;
    private final String indexType;    // memory, default, simplefs, ...
    // see https://www.elastic.co/guide/en/elasticsearch/reference/current/index-modules-store.html#file-system
    private final boolean httpEnabled;

    private final Node node;

    public EmbeddedElasticsearchNode() {
        this(DEFAULT_DATA_DIRECTORY, "memory", false);
    }

    public EmbeddedElasticsearchNode(String dataDirectory, String indexType, boolean httpEnabled) {
        this.dataDirectory = dataDirectory;
        this.indexType = indexType;
        this.httpEnabled = httpEnabled;

        final ImmutableSettings.Builder elasticsearchSettings = settingsBuilder()
                .put("http.enabled", this.httpEnabled)
                .put("index.store.type", this.indexType)
                .put("path.data", this.dataDirectory);

        node = nodeBuilder()
                .local(true)
                .settings(elasticsearchSettings.build())
                .node();
    }

    public Client getClient() {
        return this.node.client();
    }

    public void shutdown() {
        node.close();
        deleteDataDirectory();
    }

    private void deleteDataDirectory() {
        // FIXME how to prevent from writing indices to filesystem ???
        //if (this.indexType != "memory") {
            try {
                FileUtils.deleteDirectory(new File(dataDirectory));
            } catch (IOException e) {
                throw new RuntimeException("Could not delete data directory of embedded elasticsearch server", e);
            }
        //}
    }
}
