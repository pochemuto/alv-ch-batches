package ch.alv.batches.master.to.jobdesk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects locations related data of a jobdesk job
 */
public class JobdeskJobLocation {

    private JobdeskMultiLanguageValue remarks;

    private List<JobdeskLocationGeoPoint> locations = new ArrayList<>();

    private List<JobdeskLocationCoordinate> geoPoints = new ArrayList<>();

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

    public List<JobdeskLocationCoordinate> getGeoPoints() { return geoPoints; }

    public void setGeoPoints(List<JobdeskLocationCoordinate> geoPoints) { this.geoPoints = geoPoints; }

    public void addLocation(Integer zip, Double lat, Double lon) {
        this.locations.add(new JobdeskLocationGeoPoint(zip, lat, lon));
        this.geoPoints.add(new JobdeskLocationCoordinate(lat, lon));
    }
}
