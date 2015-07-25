package ch.alv.batches.partnerjobs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String onlineSince;

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

    public String getOnlineSince() {
        return onlineSince;
    }

    public void setOnlineSince(String onlineSince) {
        this.onlineSince = onlineSince;
    }
}
