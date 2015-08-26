package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskCompanyPostboxAddress {

    private String poBox;

    private String zip;

    private String location;

    public JobdeskCompanyPostboxAddress() {
    }

    public JobdeskCompanyPostboxAddress(String poBox, String zip, String location) {
        this.poBox = poBox;
        this.zip = zip;
        this.location = location;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
