package ch.alv.batches.partnerjobs;

import javax.persistence.*;
import java.util.Date;

/**
 * JPA entity for OSTE_ADMIN table entries.
 *
 * @since 1.0.0
 */
@Entity
@Table(name = "OSTE_ADMIN")
public class PartnerJob {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "BEZEICHNUNG")
    private String title;

    @Column(name = "BESCHREIBUNG", length = 2000)
    private String description;

    @Column(name = "BERUFSGRUPPE")
    private int jobGroup;

    @Column(name = "UNT_NAME")
    private String companyName;

    @Column(name = "ARBEITSORT_PLZ")
    private String jobLocation;

    @Column(name = "PENSUM_VON")
    private int quotaFrom;

    @Column(name = "PENSUM_BIS")
    private int quotaTo;

    @Column(name = "URL_DETAIL")
    private String urlDetail;

    @Column(name = "ANMELDE_DATUM")
    @Temporal(TemporalType.DATE)
    private Date onlineSince;

    @Column(name = "SPRACHE")
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
