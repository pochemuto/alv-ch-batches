package ch.alv.batches.master.to.jobdesk.model;

public class JobdeskLocationGeoPoint {

    private Integer zip; // FIXME is it really the good key?

    private JobdeskLocationCoordinate geoLocation;

    public JobdeskLocationGeoPoint() {
    }

    /**
     * "package protected" constructor
     * @param zip
     * @param lat
     * @param lon
     */
    JobdeskLocationGeoPoint(Integer zip, Double lat, Double lon) {
        this.setZip(zip);
        this.setGeoLocation(new JobdeskLocationCoordinate(lat, lon));
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public JobdeskLocationCoordinate getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(JobdeskLocationCoordinate geoLocation) {
        this.geoLocation = geoLocation;
    }
}
