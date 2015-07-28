package ch.alv.batches.partnerjobs;

import java.util.Date;

/**
 * Abstract, internal representation of a job delivered by one of our partners.
 *
 * @since 1.0.0
 */
public class PartnerJob {

    private String id;

    private String title;

    private String description;

    private int jobGroup;

    private String companyName;

    private String jobLocation;

    private int quotaFrom;

    private int quotaTo;

    private String urlDetail;

    private Date onlineSince;

    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(int jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public int getQuotaFrom() {
        return quotaFrom;
    }

    public void setQuotaFrom(int quotaFrom) {
        this.quotaFrom = quotaFrom;
    }

    public int getQuotaTo() {
        return quotaTo;
    }

    public void setQuotaTo(int quotaTo) {
        this.quotaTo = quotaTo;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public void setUrlDetail(String urlDetail) {
        this.urlDetail = urlDetail;
    }

    public Date getOnlineSince() {
        return onlineSince;
    }

    public void setOnlineSince(Date onlineSince) {
        this.onlineSince = onlineSince;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
