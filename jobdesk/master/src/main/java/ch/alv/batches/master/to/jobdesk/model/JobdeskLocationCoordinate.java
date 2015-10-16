package ch.alv.batches.master.to.jobdesk.model;

public class JobdeskLocationCoordinate {

    private double lat;
    private double lon;

    public JobdeskLocationCoordinate() {
        // FIXME values?
    }

    public JobdeskLocationCoordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "N:" + this.lat + ",E:" + this.lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
