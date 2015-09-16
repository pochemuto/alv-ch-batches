package ch.alv.batches.commons.test;

import ch.alv.batches.commons.test.elasticsearch.EmbeddedElasticsearchNode;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;

/**
 * Note that it is not possible to restart an embedded ES node once it has been stopped.
 */
public class TestApplicationWithEmbeddedElasticsearchNode extends SimpleTestApplication {

    @Bean
    public EmbeddedElasticsearchNode embeddedElasticsearchNode() {
        return new EmbeddedElasticsearchNode();
    }

    @Bean
    Client getElasticsearchClient() {
        return embeddedElasticsearchNode().getClient();
    }

}