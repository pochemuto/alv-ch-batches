package ch.alv.batches.connect.reader;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Unit tests for the {@link HttpFileReader} class
 *
 * @since 1.0.0
 */
public class HttpFIleReaderTest {

    private static final String FILE_NAME = "feed";
    private static final String FILE_SUFFIX = ".xml";

    private HttpFileReader reader;

    @Before
    public void init() {
        reader = new HttpFileReader("http://localhost/feed.xml", "feed_tmp.xml");
    }

    @Test
    public void testRead() throws Exception {

        try {
            File jobsFile = new File("tmp.xml");
            FileUtils.copyInputStreamToFile(HttpFileReader.class.getResourceAsStream("/" + FILE_NAME + FILE_SUFFIX), jobsFile);

                stubFor(get(urlEqualTo("http://localhost/feed.xml"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/xml")
                                .withBody(FileUtils.readFileToString(jobsFile))));
            File file = reader.read();
            Assert.assertNotNull(file);
            Assert.assertTrue(FileUtils.contentEqualsIgnoreEOL(file, jobsFile, "UTF-8"));
            Assert.assertTrue(file.getName().startsWith("tmp_" + FILE_NAME));
            Assert.assertTrue(file.getName().endsWith(FILE_SUFFIX));
            jobsFile.deleteOnExit();
            file.deleteOnExit();
        } catch (IOException e) {
            // if no assertion fail, everything's fine...
        }

    }

}
