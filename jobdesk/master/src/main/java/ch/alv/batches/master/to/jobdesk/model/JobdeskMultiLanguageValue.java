package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskMultiLanguageValue {

    private String de;

    private String fr;

    private String it;

    private String en;

    public JobdeskMultiLanguageValue() {
    }

    public JobdeskMultiLanguageValue(String defaultValue) {
        setDe(defaultValue);
        setFr(defaultValue);
        setIt(defaultValue);
        setEn(defaultValue);
    }

    public JobdeskMultiLanguageValue(String de, String fr, String it, String en) {
        setDe(de);
        setFr(fr);
        setIt(it);
        setEn(en);
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
