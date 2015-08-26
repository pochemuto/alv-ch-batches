package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskLocation {

    private String zip;

    private JobdeskLocationCoordinate coords;

    public JobdeskLocation() {
    }

    public JobdeskLocation(String zip, JobdeskLocationCoordinate coords) {
        this.zip = zip;
        this.coords = coords;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public JobdeskLocationCoordinate getCoords() {
        return coords;
    }

    public void setCoords(JobdeskLocationCoordinate coords) {
        this.coords = coords;
    }
}
