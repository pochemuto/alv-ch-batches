package ch.alv.batches.companydata;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CompanyImportTestApplication.class)
public class FtpAvgFirmenStaxEventItemReaderIntegrationTest {

    private static final String FILE_NAME = "AVAMPSTS";
    private static final String FILE_SUFFIX = ".xml";

    @Resource
    private Job importAdminJobsJob;

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private DataSource dataSource;

    private List<Company> companies;

    private static File dataFile = new File("local_data.xml");

    private static FakeFtpServer fakeFtpServer;

    @BeforeClass
    public static void init() throws IOException {
        FileUtils.copyInputStreamToFile(FtpAvgFirmenStaxEventItemReaderIntegrationTest.class.getResourceAsStream("/" + FILE_NAME + FILE_SUFFIX), dataFile);
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(8002);  // use any free port

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/AVAMPSTS.xml", FileUtils.readFileToString(dataFile)));
        fakeFtpServer.setFileSystem(fileSystem);

        UserAccount userAccount = new UserAccount("ftpdev", "ftpdev", "/");
        fakeFtpServer.addUserAccount(userAccount);
        fakeFtpServer.start();
    }

    @Before
    public void initTableAndJobObjects() throws ParseException, SQLException {

        Connection con = null;
        PreparedStatement pstmt;
        con = dataSource.getConnection();
        con.setAutoCommit(true);

        try {
            pstmt = con.prepareStatement("SELECT 1 FROM STAGING_AVG_FIRMEN_IMPORT LIMIT 1");
            pstmt.execute();
        } catch (Exception e){
            pstmt = con.prepareStatement("CREATE TABLE STAGING_AVG_FIRMEN_IMPORT (" +
                    "ID INTEGER, " +
                    "BETID VARCHAR(255), " +
                    "EMAIL VARCHAR(255), " +
                    "KANTON VARCHAR(50), " +
                    "NAME VARCHAR(255), " +
                    "NAME2 VARCHAR(255), " +
                    "ORT VARCHAR(255), " +
                    "PLZ VARCHAR(6), " +
                    "STRASSE VARCHAR(255), " +
                    "TELEFONNUMMER VARCHAR(50), " +
                    "TODELETE DATE)");
            pstmt.execute();
        }

        try {
            pstmt = con.prepareStatement("SELECT 1 FROM AVG_FIRMEN LIMIT 1");
            pstmt.execute();
        } catch (Exception e){
            pstmt = con.prepareStatement("CREATE TABLE AVG_FIRMEN (" +
                    "ID INTEGER, " +
                    "BETID VARCHAR(255), " +
                    "EMAIL VARCHAR(255), " +
                    "KANTON VARCHAR(50), " +
                    "NAME VARCHAR(255), " +
                    "NAME2 VARCHAR(255), " +
                    "ORT VARCHAR(255), " +
                    "PLZ VARCHAR(6), " +
                    "STRASSE VARCHAR(255), " +
                    "TELEFONNUMMER VARCHAR(50), " +
                    "TODELETE DATE)");
            pstmt.execute();
        } finally {
            con.close();
        }


        companies = new ArrayList<>();
        companies.add(initCompany1());
        companies.add(initCompany2());
        companies.add(initCompany3());
        companies.add(initCompany4());
        companies.add(initCompany5());
    }

    private Company initCompany1() {

        Company company = new Company();
        company.setId(5);
        company.setCompanyId("5");
        company.setEmail("info@aplan.ch");
        company.setName("A-Plan GmbH");
        company.setCity("Bad Zurzach");
        company.setZip("5330");
        company.setStreet("Amtshausplatz 1");
        company.setPhone("0562506767");
        return company;
    }

    private Company initCompany2() {
        Company company = new Company();
        company.setId(6);
        company.setCompanyId("6");
        company.setEmail("info@afpersonal.ch");
        company.setName("AF Personal AG");
        company.setCity("Bremgarten AG");
        company.setZip("5620");
        company.setStreet("Zürcherstrasse 29");
        company.setPhone("0566317491");
        return company;
    }

    private Company initCompany3() {
        Company company = new Company();
        company.setId(8);
        company.setCompanyId("8");
        company.setEmail("andreas.somogyi@aspmarketing.ch");
        company.setName("ASP Marketing AG");
        company.setCity("Seengen");
        company.setZip("5707");
        company.setStreet("Kesslergasse 22");
        company.setPhone("0627775308");
        return company;
    }

    private Company initCompany4() {
        Company company = new Company();
        company.setId(10);
        company.setCompanyId("10");
        company.setEmail("aarau@adecco.ch");
        company.setName("Adecco human resources AG");
        company.setCity("Aarau 1 Fächer");
        company.setZip("5001");
        company.setStreet("Bahnhofstrasse 10");
        company.setPhone("0628347040");
        return company;
    }

    private Company initCompany5() {
        Company company = new Company();
        company.setId(11);
        company.setCompanyId("11");
        company.setEmail("baden@adecco.ch");
        company.setName("Adecco human resources AG");
        company.setCity("Baden");
        company.setZip("5400");
        company.setStreet("Mellingerstrasse 6");
        company.setPhone("0562002040");
        return company;
    }

    @Test
    public void runTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException, SQLException {
        Assert.assertNotNull(importAdminJobsJob);
        JobExecution exec = jobLauncher.run(importAdminJobsJob, new JobParameters());
        Assert.assertEquals("COMPLETED", exec.getExitStatus().getExitCode());

        Connection con;
        PreparedStatement pstmt;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(true);
            pstmt = con.prepareStatement("SELECT * FROM FIRMEN_IMPORT ORDER BY ID");
            ResultSet result = pstmt.executeQuery();
            int counter = 0;
            while(result.next() && counter < 5) {
                Company currentCompany = companies.get(counter);
                Assert.assertEquals(currentCompany.getId(), result.getInt("ID"));
                Assert.assertEquals(currentCompany.getEmail(), result.getString("EMAIL"));
                Assert.assertEquals(currentCompany.getCanton(), result.getString("KANTON"));
                Assert.assertEquals(currentCompany.getName(), result.getString("NAME"));
                Assert.assertEquals(currentCompany.getName2(), result.getString("NAME2"));
                Assert.assertEquals(currentCompany.getCity(), result.getString("ORT"));
                Assert.assertEquals(currentCompany.getZip(), result.getString("PLZ"));
                Assert.assertEquals(currentCompany.getStreet(), result.getString("STRASSE"));
                Assert.assertEquals(currentCompany.getPhone(), result.getString("TELEFONNUMMER"));
                if(StringUtils.isEmpty(result.getString("NAME2"))) {
                    Assert.assertNull(result.getString("BETID"));
                }
                counter++;
            }

            while (result.next()) {
                if(StringUtils.isNotEmpty(result.getString("NAME2"))) {
                    Assert.assertEquals("GS", result.getString("NAME2"));
                    Assert.assertNotNull(result.getString("BETID"));
                    break;
                }
            }

            result.last();
            Assert.assertEquals(6526, result.getRow());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void cleanup() {
        fakeFtpServer.stop();
        dataFile.deleteOnExit();
    }

}
