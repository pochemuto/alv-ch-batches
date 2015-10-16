package ch.alv.batches.master.to.jobdesk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects locations related data of a jobdesk job
 */
public class JobdeskJobLocation {

    private JobdeskMultiLanguageValue remarks;

    @JsonProperty("location") // FIXME: remove when API spec will be changed
    private List<JobdeskLocationGeoPoint> locations = new ArrayList<>();

    public JobdeskJobLocation() {
    }

    public JobdeskJobLocation(String remarksText) {
        setRemarks(new JobdeskMultiLanguageValue(remarksText));
    }

    public JobdeskJobLocation(String remarksTextDe, String remarksTextFr, String remarksTextIt, String remarksTextEn) {
        setRemarks(new JobdeskMultiLanguageValue(remarksTextDe, remarksTextFr, remarksTextIt, remarksTextEn));
    }

    public JobdeskMultiLanguageValue getRemarks() {
        return remarks;
    }

    public void setRemarks(JobdeskMultiLanguageValue remarks) {
        this.remarks = remarks;
    }

    public List<JobdeskLocationGeoPoint> getLocations() {
        return locations;
    }

    public void setLocations(List<JobdeskLocationGeoPoint> locations) {
        this.locations = locations;
    }

    public void addLocation(Integer zip, Double lat, Double lon) {
        this.locations.add(new JobdeskLocationGeoPoint(zip, lat, lon));
    }
}
