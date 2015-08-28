package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.master.to.jobdesk.model.JobdeskLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by stibe on 17.08.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MasterToJobdeskTestApplication.class)
public class MasterToJobdeskLocationIndexTest {

    @Resource
    private Client client;

    @Resource
    private JobOperator operator;

    @Before
    public void init() {
        try {
            client.admin().indices().deleteMapping(new DeleteMappingRequest("jobdesk").types("locations")).actionGet();
            client.admin().indices().preparePutMapping("jobdesk").setSource("\"locations\":{\n" +
                    "      \"properties\": {\n" +
                    "        \"name\": {\n" +
                    "          \"type\":\"string\"\n" +
                    "        },\n" +
                    "        \"zip\": {\n" +
                    "          \"type\":\"string\"\n" +
                    "        },\n" +
                    "        \"zipAdditionalNumber\": {\n" +
                    "          \"type\":\"string\"\n" +
                    "        },\n" +
                    "        \"municipalityName\": {\n" +
                    "          \"type\":\"string\"\n" +
                    "        },\n" +
                    "        \"canton\": {\n" +
                    "          \"type\":\"string\"\n" +
                    "        },\n" +
                    "        \"coords\": {\n" +
                    "          \"type\": \"geo_point\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }").execute();
        } catch (Exception e) {

        }

    }

    @Test
    public void doSomething() throws IOException, InterruptedException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException, JobParametersNotFoundException {

        GetResponse response = client.prepareGet("jobdesk", "locations", "3110")
                .execute()
                .actionGet();


        Assert.assertNull(response.getSourceAsString());

        operator.startNextInstance(MasterToJobdeskConfiguration.JOB_NAME_LOCATIONS_CREATE_FULL_INDEX);
        response = client.prepareGet("jobdesk", "locations", "3110")
                .execute()
                .actionGet();

        ObjectReader ow = new ObjectMapper().reader(JobdeskLocation.class);
        JobdeskLocation location = ow.readValue(response.getSourceAsString());


        // todo useful checks, but discuss data handling before

        Assert.assertEquals("3110", location.getZip());
        Assert.assertEquals(7.555, location.getCoords().getLat(), 0.0);
        Assert.assertEquals(46.876, location.getCoords().getLon(), 0.0);

        response = client.prepareGet("jobdesk", "locations", "3158")
                .execute()
                .actionGet();

        location = ow.readValue(response.getSourceAsString());
        Assert.assertEquals("3158", location.getZip());
        Assert.assertEquals(7.329, location.getCoords().getLat(), 0.0);
        Assert.assertEquals(46.766, location.getCoords().getLon(), 0.0);

    }

}
