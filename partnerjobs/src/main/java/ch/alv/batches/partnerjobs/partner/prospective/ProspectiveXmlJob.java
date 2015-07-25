package ch.alv.batches.partnerjobs.partner.prospective;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * JPA entity for OSTE_ADMIN table entries.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "record")
public class ProspectiveXmlJob {

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

    @XmlAttribute(name = "refId")
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
}
