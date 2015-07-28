package ch.alv.batches.connect.reader;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.UrlResource;

import java.io.*;
import java.nio.file.Files;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
public class FtpFileReader<T> extends StaxEventItemReader<T> implements ItemReader<T> {

    private static final String TMP_FILE_PREFIX = "tmp_";

    private final String host;
    private final int port;
    private final String userName;
    private final String password;
    private final String remotePath;
    private final String fileName;

    private final String fileSuffix;
    private final String coreFileName;

    private FTPClient ftpClient;

    public FtpFileReader(String host, int port, String userName, String password, String remotePath, String fileName) {
        super();
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.remotePath = remotePath;
        this.fileName = fileName;
        String[] fileNameParts = fileName.split("\\.");
        fileSuffix = fileNameParts[fileNameParts.length-1];
        coreFileName = fileName.substring(0, fileName.length() - fileSuffix.length() - 1);
    }

    @Override
    public T read() throws Exception {
        connect();
        File file = downloadFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        setResource(new UrlResource("file://" + file.getAbsolutePath()));
        disconnect();
        return super.read();
    }

    private void connect() throws Exception {
        ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftpClient.connect(host, port);
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftpClient.login(userName, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
    }

    private File downloadFile() throws IOException {
        File file = Files.createTempFile(TMP_FILE_PREFIX, "_" + coreFileName + "." + fileSuffix).toFile();
        OutputStream outputStream = new FileOutputStream(file);
        InputStream inputStream = ftpClient.retrieveFileStream(remotePath);
        int read;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        return file;
    }

    private void disconnect() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException f) {
                // do nothing as file is already downloaded from FTP server
            }
        }
    }
}
