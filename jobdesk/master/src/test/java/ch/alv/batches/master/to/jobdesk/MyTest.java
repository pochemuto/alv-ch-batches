package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.test.SpringBatchTestHelper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by stibe on 17.08.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MasterToJobdeskTestApplication.class)
public class MyTest {

    private static final String ID_NOT_FOUND = "<ID NOT FOUND>";
    private static final String INDEX_NAME = "jobdesk";
    private static final String HOST = "localhost";


    private static final Logger log = LoggerFactory.getLogger(MyTest.class);

    @Resource
    private Job importAvgCompaniesJob;

    @Resource
    SpringBatchTestHelper springBatchHelper;

    @Resource
    private DSLContext jooq;


    @Test
    public void doSomething() throws IOException, InterruptedException {
        final Client client = getClient();
        // Create Index and set settings and mappings
        final String indexName = "jobdesk";
        final String documentType = "job";
        final String documentId = "1";
        final String fieldName = "foo";
        final String value = "bar";


        if (!indexExists(client, indexName)) {
            log.error("Could not execute job '{}'. There is no index called '{}' on host '{}'.", MasterToJobdeskConfiguration.JOB_NAME, INDEX_NAME, HOST);
            return;
        }

        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);

        // MAPPING GOES HERE

        /*final XContentBuilder mappingBuilder = jsonBuilder().startObject().startObject(documentType)
                .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                .endObject();
        System.out.println(mappingBuilder.string());
        createIndexRequestBuilder.addMapping(documentType, mappingBuilder);

        // MAPPING DONE
        createIndexRequestBuilder.execute().actionGet();

        // Add documents
        final IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType, documentId);
        // build json object
        final XContentBuilder contentBuilder = jsonBuilder().startObject().prettyPrint();
        contentBuilder.field(fieldName, value);

        indexRequestBuilder.setSource(contentBuilder);
        indexRequestBuilder.execute().actionGet();

        // Get document
        System.out.println(getValue(client, indexName, documentType, documentId, fieldName));

        int idx = 0;
        while (true) {
            Thread.sleep(1000L);
            idx++;
            System.out.println(idx * 10 + " seconds passed");
            final String name = getValue(client, indexName, documentType, documentId, fieldName);
            if (ID_NOT_FOUND.equals(name)) {
                break;
            } else {
                // Try again
                System.out.println(name);
            }
        }
        System.out.println("Document was garbage collected");*/
    }

    private boolean indexExists(Client client, String indexName) {
        final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
        return res.isExists();
    }


    private Client getClient() {
        final ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        return transportClient;
    }


    protected static String getValue(final Client client, final String indexName, final String documentType,
                                     final String documentId, final String fieldName) {
        final GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, documentType, documentId);
        getRequestBuilder.setFields(new String[] { fieldName });
        final GetResponse response2 = getRequestBuilder.execute().actionGet();
        if (response2.isExists()) {
            final String name = response2.getField(fieldName).getValue().toString();
            return name;
        } else {
            return ID_NOT_FOUND;
        }
    }


}
