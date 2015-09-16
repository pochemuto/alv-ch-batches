package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskISCOCode {

    private Integer majorGroup;

    private Integer groupLevel2;

    private Integer groupLevel3;

    private Integer groupLevel4;

    public JobdeskISCOCode() {
    }

    public JobdeskISCOCode(Integer majorGroup, Integer groupLevel2, Integer groupLevel3, Integer groupLevel4) {
        this.majorGroup = majorGroup;
        this.groupLevel2 = groupLevel2;
        this.groupLevel3 = groupLevel3;
        this.groupLevel4 = groupLevel4;
    }

    public Integer getMajorGroup() {
        return majorGroup;
    }

    public void setMajorGroup(Integer majorGroup) {
        this.majorGroup = majorGroup;
    }

    public Integer getGroupLevel2() {
        return groupLevel2;
    }

    public void setGroupLevel2(Integer groupLevel2) {
        this.groupLevel2 = groupLevel2;
    }

    public Integer getGroupLevel3() {
        return groupLevel3;
    }

    public void setGroupLevel3(Integer groupLevel3) {
        this.groupLevel3 = groupLevel3;
    }

    public Integer getGroupLevel4() {
        return groupLevel4;
    }

    public void setGroupLevel4(Integer groupLevel4) {
        this.groupLevel4 = groupLevel4;
    }
}
