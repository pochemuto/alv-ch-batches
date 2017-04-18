package ch.alv.batches.commons.spring.integration;

import org.apache.commons.net.ftp.FTP;

/**
 * Spring Java Config Structure for AdvancedFtpSessionFactory instance
 */
public class AdvancedFtpSessionFactoryConfig {

    private String host = "";
    private int port = FTP.DEFAULT_PORT;
    private int clientMode = 0;
    private int fileType = FTP.BINARY_FILE_TYPE;
    private String username = "anonymous";
    private String password = "anonymous";
    private boolean remoteVerificationEnabled = true;

    public static void applyConfiguration(AdvancedFtpSessionFactory factory, AdvancedFtpSessionFactoryConfig config) {
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());
        factory.setClientMode(config.getClientMode());
        factory.setClientMode(config.getClientMode());
        factory.setFileType(config.getFileType());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        factory.setRemoteVerificationEnabled(config.isRemoteVerificationEnabled());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getClientMode() {
        return clientMode;
    }

    public void setClientMode(int clientMode) {
        this.clientMode = clientMode;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemoteVerificationEnabled() {
        return remoteVerificationEnabled;
    }

    public void setRemoteVerificationEnabled(boolean remoteVerificationEnabled) {
        this.remoteVerificationEnabled = remoteVerificationEnabled;
    }

}
