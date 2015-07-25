package ch.alv.batches.connect.reader;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.*;
import java.nio.file.Files;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
public class FtpFileReader implements ItemReader<File> {

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
    public File read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        connect();
        File file = downloadFile();
        disconnect();
        return file;
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
        File file = Files.createTempFile(coreFileName, "." + fileSuffix).toFile();
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
