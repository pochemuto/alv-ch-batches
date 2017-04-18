package ch.alv.batches.company.to.master.config;

import ch.alv.batches.commons.spring.integration.AdvancedFtpSessionFactoryConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "ch.alv.company.ftp", ignoreUnknownFields = false)
public class CompanyAvgFtpConfig extends AdvancedFtpSessionFactoryConfig {

    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
