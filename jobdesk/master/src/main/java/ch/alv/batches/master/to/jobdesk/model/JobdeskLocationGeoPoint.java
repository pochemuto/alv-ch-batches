package ch.alv.batches.master.to.jobdesk.model;

public class JobdeskLocationGeoPoint {

    private Integer zip; // FIXME is it really the good key?

    private JobdeskLocationCoordinate coords;

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
        this.setCoords(new JobdeskLocationCoordinate(lat, lon));
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public JobdeskLocationCoordinate getCoords() {
        return coords;
    }

    public void setCoords(JobdeskLocationCoordinate coords) {
        this.coords = coords;
    }
}
