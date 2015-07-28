package ch.alv.batches.connect.reader;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.File;
import java.io.IOException;

/**
 * Unit tests for the {@link ch.alv.batches.connect.reader.HttpFileReader} class
 *
 * @since 1.0.0
 */
public class FtpFIleReaderTest {

    private static final String FILE_NAME = "feed";
    private static final String FILE_SUFFIX = ".xml";

    private FakeFtpServer fakeFtpServer;

    private File avamFile = new File("tmp.xml");

    private FtpFileReader reader;

    @Before
    public void init() throws IOException {

        FileUtils.copyInputStreamToFile(FtpFIleReaderTest.class.getResourceAsStream("/" + FILE_NAME + FILE_SUFFIX), avamFile);
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(8002);  // use any free port

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/feed.xml", FileUtils.readFileToString(avamFile)));
        fakeFtpServer.setFileSystem(fileSystem);

        UserAccount userAccount = new UserAccount("ftpdev", "ftpdev", "/");
        fakeFtpServer.addUserAccount(userAccount);

        fakeFtpServer.start();

        reader = new FtpFileReader("localhost", 8002, "ftpdev", "ftpdev", "feed.xml", "feed.xml");
    }

/*    @Test
    public void testRead() throws Exception {
        try {
            File file = reader.read();
            Assert.assertNotNull(file);
            Assert.assertTrue(FileUtils.contentEqualsIgnoreEOL(file, avamFile, "UTF-8"));
            Assert.assertTrue(file.getName().startsWith(FILE_NAME));
            Assert.assertTrue(file.getName().endsWith(FILE_SUFFIX));
            avamFile.deleteOnExit();
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
