package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskCompanyAddress {

    private String street;

    private String zip;

    private String location;

    private String country;

    public JobdeskCompanyAddress() {
    }

    public JobdeskCompanyAddress(String street, String zip, String location, String country) {
        this.street = street;
        this.zip = zip;
        this.location = location;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
