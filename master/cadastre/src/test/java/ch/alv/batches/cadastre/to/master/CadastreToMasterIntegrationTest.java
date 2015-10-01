package ch.alv.batches.cadastre.to.master;

import ch.alv.batches.cadastre.to.master.jooq.tables.records.LocationRecord;
import ch.alv.batches.commons.test.SimpleTestApplication;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static ch.alv.batches.cadastre.to.master.jooq.tables.Location.LOCATION;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestApplication.class)
@IntegrationTest
public class CadastreToMasterIntegrationTest {
    
    @Resource
    private DSLContext jooq;

    @Resource
    private Job importLocationJobs;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Test
    public void runTest() throws Exception {
        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importLocationJobs));

        List<LocationRecord> locations = jooq.fetch(LOCATION);
        Assert.assertEquals(4168, locations.size());

        Optional<LocationRecord> l1 = locations.stream().filter(x -> x.getName().equals("Zäziwil")).findFirst();

        Assert.assertEquals("BE", l1.get().getCanton());
        Assert.assertEquals(7.661, l1.get().getLon(), 0);
        Assert.assertEquals(46.902, l1.get().getLat(), 0);
    }

}
