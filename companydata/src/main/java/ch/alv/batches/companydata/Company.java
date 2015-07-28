package ch.alv.batches.companydata;

import java.util.Date;

/**
 * Abstract, internal representation of a company
 * delivered by one of our master data providers.
 *
 * @since 1.0.0
 */
public class Company {

    private int id;

    private String companyId;

    private String email;

    private String canton;

    private String name;

    private String name2;

    private String city;

    private String zip;

    private String street;

    private String phone;

    private Date toDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getToDelete() {
        return toDelete;
    }

    public void setToDelete(Date toDelete) {
        this.toDelete = toDelete;
    }
}
