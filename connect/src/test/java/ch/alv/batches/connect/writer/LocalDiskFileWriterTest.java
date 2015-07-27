package ch.alv.batches.connect.writer;

import ch.alv.batches.connect.reader.HttpFileReader;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link ch.alv.batches.connect.reader.HttpFileReader} class
 *
 * @since 1.0.0
 */
public class LocalDiskFileWriterTest {

    private static final String FILE_NAME = "feed";
    private static final String FILE_SUFFIX = ".xml";

    private LocalDiskFileWriter writer;

    @Before
    public void init() throws IOException {
        writer = new LocalDiskFileWriter("/temp");
    }

    @Test
    public void testRead() throws Exception {

        try {
            File jobsFile = new File("tmp.xml");
            FileUtils.copyInputStreamToFile(HttpFileReader.class.getResourceAsStream("/" + FILE_NAME + FILE_SUFFIX), jobsFile);
            List<File> files = new ArrayList<>();
            files.add(jobsFile);
            writer.write(files);


            File freshFile = new File("/temp/tmp.xml");
            Assert.assertTrue(freshFile.exists());
            Assert.assertTrue(FileUtils.contentEqualsIgnoreEOL(freshFile, jobsFile, "UTF-8"));
            Assert.assertTrue(freshFile.getName().equals(jobsFile.getName()));
            jobsFile.deleteOnExit();
            freshFile.deleteOnExit();
        } catch (IOException e) {
            // if no assertion fail, everything's fine...
        }

    }

}
