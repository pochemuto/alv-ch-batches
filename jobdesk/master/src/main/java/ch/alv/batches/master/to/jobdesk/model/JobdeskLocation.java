package ch.alv.batches.master.to.jobdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO evaluate if this should inherit from / compose with JobdeskLocationGeoPoint class
public class JobdeskLocation {

    private String name;

    private String zip;

    @JsonProperty("additionalNumber")
    private Integer zipAdditionalNumber;

    private String municipalityName;

    private String canton;

    @JsonIgnore // FIXME remove it? Maybe a copy-paste error?
    private int municipalityNumber;

    @JsonIgnore
    private String region;

    @JsonProperty("geoLocation")
    private JobdeskLocationCoordinate coords;

    public JobdeskLocation() {
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

    public Integer getZipAdditionalNumber() {
        return zipAdditionalNumber;
    }

    public void setZipAdditionalNumber(Integer zipAdditionalNumber) {
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
