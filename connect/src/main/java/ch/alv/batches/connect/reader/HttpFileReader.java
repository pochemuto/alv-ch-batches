package ch.alv.batches.connect.reader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
public class HttpFileReader implements ItemReader<File> {

    private static final Logger LOG = LoggerFactory.getLogger(HttpFileReader.class);

    private static final String TMP_FILE_PREFIX = "tmp_";

    private final String url;

    private final String fileName;

    private final String fileSuffix;
    private final String coreFileName;

    public HttpFileReader(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
        String[] fileNameParts = fileName.split("\\.");
        fileSuffix = fileNameParts[fileNameParts.length-1];
        coreFileName = fileName.substring(0, fileName.length() - fileSuffix.length() - 1);
    }

    @Override
    public File read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return fetchFile();
    }

    private File fetchFile() throws IOException {
        LOG.info("Trying to catch data from url '{}'", url);
        CloseableHttpClient httpclient = HttpClients.createSystem();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        File file = File.createTempFile(TMP_FILE_PREFIX + coreFileName, "." + fileSuffix);
        final FileOutputStream fis = new FileOutputStream(file);
        FileCopyUtils.copy(response1.getEntity().getContent(), fis);
        fis.flush();
        httpclient.close();
        LOG.info("Data from url '{}' has been successfully retrieved.", url);
        return file;
    }
}
