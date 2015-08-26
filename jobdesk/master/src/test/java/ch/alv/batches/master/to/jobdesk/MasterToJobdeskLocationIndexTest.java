package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.commons.test.SpringBatchTestHelper;
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
import org.springframework.batch.core.Job;
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

    @Resource(name = MasterToJobdeskConfiguration.JOB_NAME_LOCATIONS_CREATE_FULL_INDEX)
    private Job fullLocationIndexJob;

    @Resource
    SpringBatchTestHelper springBatchHelper;

    @Resource
    private Client client;

    @Resource
    private JobOperator operator;

    @Before
    public void init() {
        try {
            client.admin().indices().deleteMapping(new DeleteMappingRequest("jobdesk").types("locations")).actionGet();
            client.admin().indices().preparePutMapping("jobdesk").setSource("\"jobs\": {\n" +
                    "      \"properties\": {\n" +
                    "        \"fingerprint\": {\n" +
                    "          \"type\": \"string\"\n" +
                    "        },\n" +
                    "        \"identifier\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"avam\": {\n" +
                    "              \"type\":\"string\"\n" +
                    "            },\n" +
                    "            \"egov\": {\n" +
                    "              \"type\":\"string\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"url\": {\n" +
                    "          \"type\":\"string\"\n" +
                    "        },\n" +
                    "        \"title\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"de\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"fr\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"it\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"en\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"description\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"de\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"fr\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"it\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"en\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"isco\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"majorGroup\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"groupLevel2\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"groupLevel3\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"groupLevel4\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"locations\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\":{\n" +
                    "            \"location\": {\n" +
                    "              \"type\":\"nested\",\n" +
                    "              \"properties\": {\n" +
                    "                \"coords\": {\n" +
                    "                  \"type\": \"geo_point\"\n" +
                    "                },\n" +
                    "                \"zip\": {\n" +
                    "                  \"type\": \"integer\"\n" +
                    "                }\n" +
                    "              }\n" +
                    "            },\n" +
                    "            \"remarks\": {\n" +
                    "              \"type\": \"object\",\n" +
                    "              \"properties\": {\n" +
                    "                \"de\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"fr\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"it\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"en\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                }\n" +
                    "              }\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"fulltime\": {\n" +
                    "          \"type\": \"boolean\"\n" +
                    "        },\n" +
                    "        \"external\": {\n" +
                    "          \"type\": \"boolean\"\n" +
                    "        },\n" +
                    "        \"source\": {\n" +
                    "          \"type\": \"string\"\n" +
                    "        },\n" +
                    "        \"onlineSince\": {\n" +
                    "          \"type\": \"integer\"\n" +
                    "        },\n" +
                    "        \"quotaFrom\": {\n" +
                    "          \"type\": \"short\"\n" +
                    "        },\n" +
                    "        \"quotaTo\": {\n" +
                    "          \"type\": \"short\"\n" +
                    "        },\n" +
                    "        \"availableNow\": {\n" +
                    "          \"type\":\"boolean\"\n" +
                    "        },\n" +
                    "        \"permanentJob\": {\n" +
                    "          \"type\":\"boolean\"\n" +
                    "        },\n" +
                    "        \"startDate\": {\n" +
                    "          \"type\": \"string\"\n" +
                    "        },\n" +
                    "        \"endDate\": {\n" +
                    "          \"type\": \"string\"\n" +
                    "        },\n" +
                    "        \"languages\": {\n" +
                    "          \"properties\": {\n" +
                    "            \"languageCode\": {\n" +
                    "              \"type\": \"short\"\n" +
                    "            },\n" +
                    "            \"writtenCode\": {\n" +
                    "              \"type\": \"short\"\n" +
                    "            },\n" +
                    "            \"spokenCode\": {\n" +
                    "              \"type\": \"short\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"application\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"written\": {\n" +
                    "              \"type\": \"boolean\"\n" +
                    "            },\n" +
                    "            \"electronical\": {\n" +
                    "              \"type\": \"boolean\"\n" +
                    "            },\n" +
                    "            \"electronicalAddress\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"phone\": {\n" +
                    "              \"type\": \"boolean\"\n" +
                    "            },\n" +
                    "            \"phoneNumber\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"personal\": {\n" +
                    "              \"type\": \"boolean\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"company\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"name\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"address\": {\n" +
                    "              \"type\":\"object\",\n" +
                    "              \"properties\": {\n" +
                    "                \"street\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"streetAppendix\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"zip\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"location\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                },\n" +
                    "                \"country\": {\n" +
                    "                  \"type\": \"string\"\n" +
                    "                }\n" +
                    "              }\n" +
                    "            },\n" +
                    "            \"phone\": {\n" +
                    "              \"type\":\"string\"\n" +
                    "            },\n" +
                    "            \"eMail\": {\n" +
                    "              \"type\":\"string\"\n" +
                    "            },\n" +
                    "            \"url\": {\n" +
                    "              \"type\":\"string\"\n" +
                    "            }\n" +
                    "          },\n" +
                    "          \"poAddress\": {\n" +
                    "            \"type\":\"object\",\n" +
                    "            \"properties\": {\n" +
                    "              \"poBox\": {\n" +
                    "                \"type\": \"string\"\n" +
                    "              },\n" +
                    "              \"zip\": {\n" +
                    "                \"type\": \"string\"\n" +
                    "              },\n" +
                    "              \"location\": {\n" +
                    "                \"type\": \"string\"\n" +
                    "              },\n" +
                    "              \"country\": {\n" +
                    "                \"type\": \"string\"\n" +
                    "              }\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"contact\": {\n" +
                    "          \"type\":\"object\",\n" +
                    "          \"properties\": {\n" +
                    "            \"gender\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"firstName\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"lastName\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"phone\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            },\n" +
                    "            \"eMail\": {\n" +
                    "              \"type\": \"string\"\n" +
                    "            }\n" +
                    "          }\n" +
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
