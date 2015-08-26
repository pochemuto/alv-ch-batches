package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskJobCompany {

    private String name;
    private JobdeskCompanyAddress address;
    private JobdeskCompanyPostboxAddress poAddress;
    private String phone;
    private String email;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobdeskCompanyAddress getAddress() {
        return address;
    }

    public void setAddress(JobdeskCompanyAddress address) {
        this.address = address;
    }

    public JobdeskCompanyPostboxAddress getPoAddress() {
        return poAddress;
    }

    public void setPoAddress(JobdeskCompanyPostboxAddress poAddress) {
        this.poAddress = poAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
