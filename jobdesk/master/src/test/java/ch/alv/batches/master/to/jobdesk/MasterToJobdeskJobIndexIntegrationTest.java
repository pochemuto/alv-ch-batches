package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.master.to.jobdesk.model.JobdeskJob;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
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
public class MasterToJobdeskJobIndexIntegrationTest {

    @Resource
    private JobOperator operator;

    @Resource
    private Client client;

    @Before
    public void init() {
        try {
            client.admin().indices().deleteMapping(new DeleteMappingRequest("jobdesk").types("jobs")).actionGet();
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

        client.delete(new DeleteRequest("jobdesk", "jobs", "abc"));
        GetResponse response = client.prepareGet("jobdesk", "jobs", "abc")
                .execute()
                .actionGet();

        ObjectReader ow = new ObjectMapper().reader(JobdeskJob.class);
        Assert.assertNull(response.getSourceAsString());

        operator.startNextInstance(MasterToJobdeskConfiguration.JOB_NAME_JOBS_CREATE_FULL_INDEX);
        response = client.prepareGet("jobdesk", "jobs", "abc")
                .execute()
                .actionGet();

        JobdeskJob job = ow.readValue(response.getSourceAsString());


        // todo useful checks, but discuss data handling before

        Assert.assertEquals("abc", job.getJobId());
        Assert.assertEquals("def", job.getJobIdAvam());
        Assert.assertEquals("ghi", job.getJobIdEgov());
        Assert.assertEquals("testJob", job.getTitle().getDe());
        Assert.assertEquals("testJobFr", job.getTitle().getFr());
        Assert.assertEquals("testJobIt", job.getTitle().getIt());
        Assert.assertEquals("testJobEn", job.getTitle().getEn());


        Assert.assertEquals(1, job.getLocations().getLocation().size());
        Assert.assertEquals("8355", job.getLocations().getLocation().get(0).getZip());

        Assert.assertEquals(2, job.getLanguages().size());
        Assert.assertEquals(20, job.getLanguages().get(0).getLanguageCode());
        Assert.assertEquals(2, job.getLanguages().get(0).getSpokenCode());
        Assert.assertEquals(3, job.getLanguages().get(0).getWrittenCode());

        Assert.assertEquals(22, job.getLanguages().get(1).getLanguageCode());
        Assert.assertEquals(4, job.getLanguages().get(1).getSpokenCode());
        Assert.assertEquals(5, job.getLanguages().get(1).getWrittenCode());

    }

}
