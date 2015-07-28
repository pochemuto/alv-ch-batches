package ch.alv.batches.companydata;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CompanyImportTestApplication.class)
public class AvgFirmenIntegrationTest {

    private static final String FILE_NAME = "AVAMPSTS";
    private static final String FILE_SUFFIX = ".xml";

    @Resource
    private Job importAdminJobsJob;

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private DataSource dataSource;

    private List<Company> companies;

    private File dataFile = new File("local_data.xml");

    private FakeFtpServer fakeFtpServer;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void initTableAndJobObjects() throws ParseException, SQLException, IOException {
        FileUtils.copyInputStreamToFile(AvgFirmenIntegrationTest.class.getResourceAsStream("/" + FILE_NAME + FILE_SUFFIX), dataFile);
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(8002);  // use any free port

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/AVAMPSTS.xml", FileUtils.readFileToString(dataFile)));
        fakeFtpServer.setFileSystem(fileSystem);

        UserAccount userAccount = new UserAccount("ftpdev", "ftpdev", "/");
        fakeFtpServer.addUserAccount(userAccount);
        fakeFtpServer.start();

        Connection con = null;
        PreparedStatement pstmt;
        con = dataSource.getConnection();
        con.setAutoCommit(true);

        try {
            pstmt = con.prepareStatement("SELECT 1 FROM AVG_FIRMEN LIMIT 1");
            pstmt.execute();
        } catch (Exception e){
            pstmt = con.prepareStatement("CREATE TABLE AVG_FIRMEN (" +
                    "ID VARCHAR(255), " +
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


        return company;
    }

    private Company initCompany2() {
        Company company = new Company();


        return company;
    }

    private Company initCompany3() {
        Company company = new Company();


        return company;
    }

    private Company initCompany4() {
        Company company = new Company();


        return company;
    }

    private Company initCompany5() {
        Company company = new Company();


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
            pstmt = con.prepareStatement("SELECT * FROM AVG_FIRMEN ORDER BY ID");
            ResultSet result = pstmt.executeQuery();
            int counter = 0;
            while(result.next()) {
                Company currentCompany = companies.get(counter);
                Assert.assertEquals(currentCompany.getId(), result.getInt("ID"));
                Assert.assertEquals(currentCompany.getCompanyId(), result.getString("BETID"));
                Assert.assertEquals(currentCompany.getEmail(), result.getString("EMAIL"));
                Assert.assertEquals(currentCompany.getCanton(), result.getString("KANTON"));
                Assert.assertEquals(currentCompany.getName(), result.getString("NAME"));
                Assert.assertEquals(currentCompany.getName2(), result.getString("NAME2"));
                Assert.assertEquals(currentCompany.getCity(), result.getString("ORT"));
                Assert.assertEquals(currentCompany.getZip(), result.getString("PLZ"));
                Assert.assertEquals(currentCompany.getStreet(), result.getString("STRASSE"));
                Assert.assertEquals(currentCompany.getPhone(), result.getDate("TELEFONNUMMER"));
                Assert.assertEquals(currentCompany.getToDelete(), result.getString("TODELETE"));
                counter++;
            }
            Assert.assertEquals(5, counter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
