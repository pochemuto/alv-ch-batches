package ch.alv.batches.commons.spring.integration;

import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

import java.io.IOException;

/**
 * This Factory extends the Spring Integration's DefaultFtpSessionFactory in order to provide
 * some advanced configuration capabilities.
 *
 * For the moment the additional Apache FTPClient settings are supported:
 *
 *  - configure the Remote Host Verification flag, which is enabled by default in Apache FTPClient.
 *    This factory disables remote verification by default, but this can be modified via setRemoteVerification() setter.
 */
public class AdvancedFtpSessionFactory extends DefaultFtpSessionFactory {

    private boolean remoteVerificationEnabled = false;

    public boolean isRemoteVerificationEnabled() {
        return remoteVerificationEnabled;
    }

    public void setRemoteVerificationEnabled(boolean remoteVerification) {
        this.remoteVerificationEnabled = remoteVerification;
    }

    /**
     * Will handle additional initialization after client.connect() method was invoked,
     * but before any action on the client has been taken
     */
    protected void postProcessClientAfterConnect(org.apache.commons.net.ftp.FTPClient ftpClient) throws IOException {

        //
        // Original Motivation for this extension:
        // The RemoteVerification must be disabled when using an FTP Proxy that dispatches
        // the data connection across different hosts.
        //
        ftpClient.setRemoteVerificationEnabled(this.isRemoteVerificationEnabled());

    }
}
