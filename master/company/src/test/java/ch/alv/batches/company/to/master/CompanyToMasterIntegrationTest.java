package ch.alv.batches.company.to.master;

import ch.alv.batches.commons.sql.SqlDataTypesHelper;
import ch.alv.batches.commons.test.SpringBatchTestHelper;
import ch.alv.batches.jooq.tables.records.AvgFirmenRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.jooq.DSLContext;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.alv.batches.jooq.tables.AvgFirmen.AVG_FIRMEN;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CompanyToMasterTestApplication.class)
public class CompanyToMasterIntegrationTest {

    private static final String DOWNLOAD_FILENAME = "/AVAMPSTS.xml";
    private static final String IMPORT_1 = "/AVAMPSTS-1.xml";
    private static final String IMPORT_2 = "/AVAMPSTS-2.xml";
    private static final String IMPORT_3 = "/AVAMPSTS-3.xml";

    @Resource
    private Job importAvgCompaniesJob;

    @Resource
    SpringBatchTestHelper springBatchHelper;

    @Resource
    private DSLContext jooq;

    private static FakeFtpServer fakeFtpServer = null;
    private List<AvgFirmenRecord> checkCompanies = null;

    @BeforeClass
    public static void initStaticObjects() throws IOException {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(8002);  // use any free port

        FileSystem fileSystem = new UnixFakeFileSystem();
        String content1 = FileUtils.readFileToString(
                new File("src/test/resources" + IMPORT_1));
        fileSystem.add(new FileEntry(IMPORT_1, content1));
        String content2 = FileUtils.readFileToString(
                new File("src/test/resources" + IMPORT_2));
        fileSystem.add(new FileEntry(IMPORT_2, content2));
        String content3 = FileUtils.readFileToString(
                new File("src/test/resources" + IMPORT_3));
        fileSystem.add(new FileEntry(IMPORT_3, content3));

        fakeFtpServer.setFileSystem(fileSystem);

        fakeFtpServer.addUserAccount(new UserAccount("ftpdev", "ftpdev", "/"));
        fakeFtpServer.start();
    }

    @Before
    public void initTestObjects() throws ParseException, SQLException {

        springBatchHelper.initializeSpringBatchPostgresqlSchema();

        // Test context must start with an empty target table
        jooq.truncate(AVG_FIRMEN).execute();

        checkCompanies = new ArrayList<>();
        checkCompanies.add(initAvgFirmenRecord1());
        checkCompanies.add(initAvgFirmenRecord2());
        checkCompanies.add(initAvgFirmenRecord3());
        checkCompanies.add(initAvgFirmenRecord4());
        checkCompanies.add(initAvgFirmenRecord5());
    }

    private AvgFirmenRecord initAvgFirmenRecord1() {
        AvgFirmenRecord company = new AvgFirmenRecord();
        company.setId(5);
        company.setEmail("info@aplan.ch");
        company.setName("A-Plan GmbH");
        company.setOrt("Bad Zurzach");
        company.setPlz("5330");
        company.setStrasse("Amtshausplatz 1");
        company.setTelefonnummer("0562506767");
        return company;
    }

    private AvgFirmenRecord initAvgFirmenRecord2() {
        AvgFirmenRecord company = new AvgFirmenRecord();
        company.setId(6);
        company.setEmail("info@afpersonal.ch");
        company.setName("AF Personal AG");
        company.setOrt("Bremgarten AG");
        company.setPlz("5620");
        company.setStrasse("Zürcherstrasse 29");
        company.setTelefonnummer("0566317491");
        return company;
    }

    private AvgFirmenRecord initAvgFirmenRecord3() {
        AvgFirmenRecord company = new AvgFirmenRecord();
        company.setId(8);
        company.setEmail("andreas.somogyi@aspmarketing.ch");
        company.setName("ASP Marketing");
        company.setOrt("Seengen");
        company.setPlz("5707");
        company.setStrasse("Kesslergasse 222");
        return company;
    }

    private AvgFirmenRecord updatedAvgFirmenRecord3() {
        AvgFirmenRecord company = new AvgFirmenRecord();
        company.setId(8);
        company.setEmail("andreas.somogyi@aspmarketing.ch");
        company.setName("ASP Marketing AG");
        company.setOrt("Seengen");
        company.setPlz("5707");
        company.setStrasse("Kesslergasse 22");
        company.setTelefonnummer("0627775308");
        return company;
    }

    private AvgFirmenRecord initAvgFirmenRecord4() {
        AvgFirmenRecord company = new AvgFirmenRecord();
        company.setId(10);
        company.setEmail("aarau@adecco.ch");
        company.setName("Adecco human resources AG");
        company.setOrt("Aarau 1 Fächer");
        company.setPlz("5001");
        company.setStrasse("Bahnhofstrasse 10");
        company.setTelefonnummer("0628347040");
        return company;
    }

    private AvgFirmenRecord initAvgFirmenRecord5() {
        AvgFirmenRecord company = new AvgFirmenRecord();
        company.setId(11);
        company.setEmail("baden@adecco.ch");
        company.setName("Adecco human resources AG");
        company.setOrt("Baden");
        company.setPlz("5400");
        company.setStrasse("Mellingerstrasse 6");
        company.setTelefonnummer("0562002040");
        return company;
    }

    @Test
    public void runTest() throws
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            SQLException {

        Assert.assertNotNull(importAvgCompaniesJob);

        //
        // First Import
        //

        fakeFtpServer.getFileSystem().rename(IMPORT_1, DOWNLOAD_FILENAME);

        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importAvgCompaniesJob));

        List<AvgFirmenRecord> fetchedCompanies = jooq.fetch(AVG_FIRMEN)
                .sortAsc(AVG_FIRMEN.ID);

        Assert.assertEquals(6522, fetchedCompanies.size());

        while (!fetchedCompanies.isEmpty() && !checkCompanies.isEmpty()) {
            AvgFirmenRecord result = fetchedCompanies.remove(0);
            AvgFirmenRecord check = checkCompanies.remove(0);

           Assert.assertEquals(0, result.compareTo(check));
        }

        while (!fetchedCompanies.isEmpty()) {
            AvgFirmenRecord result = fetchedCompanies.remove(0);
            if (StringUtils.isNotEmpty(result.getName2())) {
                Assert.assertEquals("GS", result.getName2());
                Assert.assertNotNull(result.getBetid());
            }
        }

        fakeFtpServer.getFileSystem().rename(DOWNLOAD_FILENAME, IMPORT_1);
        fakeFtpServer.getFileSystem().rename(IMPORT_2, DOWNLOAD_FILENAME);

        //
        // Second Import
        //

        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importAvgCompaniesJob));

        Map<Integer, AvgFirmenRecord> fetchedUpdatedCompanies = jooq.fetch(AVG_FIRMEN).intoMap(AVG_FIRMEN.ID);

        Assert.assertEquals(LocalDate.now().toDate(), fetchedUpdatedCompanies.get(5).getTodelete());
        Assert.assertEquals(LocalDate.now().toDate(), fetchedUpdatedCompanies.get(6).getTodelete());
        Assert.assertEquals(0, fetchedUpdatedCompanies.get(8).compareTo(updatedAvgFirmenRecord3()));
        Assert.assertTrue(fetchedUpdatedCompanies.containsKey(82188));
        Assert.assertTrue(fetchedUpdatedCompanies.containsKey(82191));
        Assert.assertTrue(fetchedUpdatedCompanies.containsKey(82193));

        Assert.assertEquals(6525, fetchedUpdatedCompanies.size());

        //
        // Third Import
        //

        // Simulate that previous import was executed yesterday
        Date yesterday = SqlDataTypesHelper.fromJavaUtilDate(LocalDate.now().minusDays(1).toDate());
        fetchedUpdatedCompanies.get(5).setTodelete(yesterday);
        fetchedUpdatedCompanies.get(5).store();
        fetchedUpdatedCompanies.get(6).setTodelete(yesterday);
        fetchedUpdatedCompanies.get(6).store();

        fakeFtpServer.getFileSystem().rename(DOWNLOAD_FILENAME, IMPORT_2);
        fakeFtpServer.getFileSystem().rename(IMPORT_3, DOWNLOAD_FILENAME);

        Assert.assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importAvgCompaniesJob));

        // Ensure that todelete timestamp is not changed over time
        AvgFirmenRecord r5 = jooq.selectFrom(AVG_FIRMEN).where(AVG_FIRMEN.ID.equal(5)).fetchOne();
        Assert.assertEquals(yesterday, r5.getTodelete());

        // Ensure that deleted companies cannot be revived, nor updated
        AvgFirmenRecord r6 = jooq.selectFrom(AVG_FIRMEN).where(AVG_FIRMEN.ID.equal(6)).fetchOne();
        Assert.assertEquals(yesterday, r6.getTodelete());
        Assert.assertEquals("0566317491", r6.getTelefonnummer());
        Assert.assertEquals("AF Personal AG", r6.getName());
        Assert.assertEquals("Bremgarten AG", r6.getOrt());
    }

    @After
    public void cleanDatabase() {
        jooq.truncate(AVG_FIRMEN).execute();
    }

    @AfterClass
    public static void cleanup() {
        fakeFtpServer.stop();
    }

}
