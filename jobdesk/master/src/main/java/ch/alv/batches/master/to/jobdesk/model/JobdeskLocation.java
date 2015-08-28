package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskLocation {

    private String name;

    private String zip;

    private String zipAdditionalNumber;

    private String municipalityName;

    private String canton;

    private int municipalityNumber;

    private String region;

    private JobdeskLocationCoordinate coords;

    public JobdeskLocation() {
    }

    public JobdeskLocation(String zip, JobdeskLocationCoordinate coords) {
        this.zip = zip;
        this.coords = coords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZipAdditionalNumber() {
        return zipAdditionalNumber;
    }

    public void setZipAdditionalNumber(String zipAdditionalNumber) {
        this.zipAdditionalNumber = zipAdditionalNumber;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public int getMunicipalityNumber() {
        return municipalityNumber;
    }

    public void setMunicipalityNumber(int municipalityNumber) {
        this.municipalityNumber = municipalityNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public JobdeskLocationCoordinate getCoords() {
        return coords;
    }

    public void setCoords(JobdeskLocationCoordinate coords) {
        this.coords = coords;
    }
}
