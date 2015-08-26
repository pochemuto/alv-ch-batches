package ch.alv.batches.master.to.jobdesk.model;

/**
 * Collects application related data
 */
public class JobdeskJobApplication {

    private boolean applicationWritten;
    private boolean applicationElectronical;
    private boolean applicationPhone;
    private boolean applicationPersonal;

    private String phoneNumber;
    private String electronicalAddress;

    public JobdeskJobApplication() {
    }

    public JobdeskJobApplication(boolean applicationWritten, boolean applicationElectronical, boolean applicationPhone, boolean applicationPersonal, String phoneNumber, String electronicalAddress) {
        this.applicationWritten = applicationWritten;
        this.applicationElectronical = applicationElectronical;
        this.applicationPhone = applicationPhone;
        this.applicationPersonal = applicationPersonal;
        this.phoneNumber = phoneNumber;
        this.electronicalAddress = electronicalAddress;
    }

    public boolean isApplicationWritten() {
        return applicationWritten;
    }

    public void setApplicationWritten(boolean applicationWritten) {
        this.applicationWritten = applicationWritten;
    }

    public boolean isApplicationElectronical() {
        return applicationElectronical;
    }

    public void setApplicationElectronical(boolean applicationElectronical) {
        this.applicationElectronical = applicationElectronical;
    }

    public boolean isApplicationPhone() {
        return applicationPhone;
    }

    public void setApplicationPhone(boolean applicationPhone) {
        this.applicationPhone = applicationPhone;
    }

    public boolean isApplicationPersonal() {
        return applicationPersonal;
    }

    public void setApplicationPersonal(boolean applicationPersonal) {
        this.applicationPersonal = applicationPersonal;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getElectronicalAddress() {
        return electronicalAddress;
    }

    public void setElectronicalAddress(String electronicalAddress) {
        this.electronicalAddress = electronicalAddress;
    }
}
