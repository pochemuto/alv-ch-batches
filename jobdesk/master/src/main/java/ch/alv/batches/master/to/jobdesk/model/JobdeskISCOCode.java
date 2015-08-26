package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskISCOCode {

    private int majorGroup;

    private int groupLevel2;

    private int groupLevel3;

    private int groupLevel4;

    public JobdeskISCOCode() {
    }

    public JobdeskISCOCode(int majorGroup, int groupLevel2, int groupLevel3, int groupLevel4) {
        this.majorGroup = majorGroup;
        this.groupLevel2 = groupLevel2;
        this.groupLevel3 = groupLevel3;
        this.groupLevel4 = groupLevel4;
    }

    public int getMajorGroup() {
        return majorGroup;
    }

    public void setMajorGroup(int majorGroup) {
        this.majorGroup = majorGroup;
    }

    public int getGroupLevel2() {
        return groupLevel2;
    }

    public void setGroupLevel2(int groupLevel2) {
        this.groupLevel2 = groupLevel2;
    }

    public int getGroupLevel3() {
        return groupLevel3;
    }

    public void setGroupLevel3(int groupLevel3) {
        this.groupLevel3 = groupLevel3;
    }

    public int getGroupLevel4() {
        return groupLevel4;
    }

    public void setGroupLevel4(int groupLevel4) {
        this.groupLevel4 = groupLevel4;
    }
}
