package ch.alv.batches.master.to.jobdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JobdeskIscoCode {

    @JsonIgnore
    private String isco08Id = null;

    public JobdeskIscoCode() {
    }

    public JobdeskIscoCode(String isco08Id) {
        this.setIsco08Id(isco08Id);
    }

    public String getIsco08Id() {
        return isco08Id;
    }

    private void setIsco08Id(String isco08Id) {
        if (isco08Id.matches("\\d{4}")) {
            this.isco08Id = isco08Id;
        } else {
            // set to empty string to differ from "null" (i.e. uninitialized)
            this.isco08Id = ""; // or invalid? or throw exception?
        }
    }

    // String type is used for backwards compatibility with former API
    // TODO: Switch to Integer type after Pilot Phase

    public String getMajorGroup() {
        return this.getGroupLevel1();
    }

    @JsonIgnore
    public String getGroupLevel1() {
        return isco08Id.substring(0);
    }

    public String getGroupLevel2() {
        return isco08Id.substring(0, 1);
    }

    public String getGroupLevel3() {
        return isco08Id.substring(0, 2);
    }

    public String getGroupLevel4() {
        return this.getIsco08Id();
    }

    // Setters below are needed for reading ES
    // FIXME this DTO should actually be quite dumb and not infer data (this should be elsewhere...)

    public void setMajorGroup(String isco08Id) {
        // NOOP
    }

    public void setGroupLevel2(String isco08Id) {
        // NOOP
    }

    public void setGroupLevel3(String isco08Id) {
        // NOOP
    }

    public void setGroupLevel4(String isco08Id) {
        this.setIsco08Id(isco08Id);
    }
}
