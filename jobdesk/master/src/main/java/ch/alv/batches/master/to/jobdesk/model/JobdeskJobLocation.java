package ch.alv.batches.master.to.jobdesk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects location related data of a jobdesk job
 */
public class JobdeskJobLocation {

    private JobdeskMultiLanguageValue locationRemarks;

    private List<JobdeskLocation> location = new ArrayList<>();

    public JobdeskJobLocation() {
    }

    public JobdeskJobLocation(String remarksText) {
        setLocationRemarks(new JobdeskMultiLanguageValue(remarksText));
    }

    public JobdeskJobLocation(String remarksTextDe, String remarksTextFr, String remarksTextIt, String remarksTextEn) {
        setLocationRemarks(new JobdeskMultiLanguageValue(remarksTextDe, remarksTextFr, remarksTextIt, remarksTextEn));
    }

    public JobdeskMultiLanguageValue getLocationRemarks() {
        return locationRemarks;
    }

    public void setLocationRemarks(JobdeskMultiLanguageValue locationRemarks) {
        this.locationRemarks = locationRemarks;
    }

    public List<JobdeskLocation> getLocation() {
        return location;
    }

    public void setLocation(List<JobdeskLocation> location) {
        this.location = location;
    }
}
