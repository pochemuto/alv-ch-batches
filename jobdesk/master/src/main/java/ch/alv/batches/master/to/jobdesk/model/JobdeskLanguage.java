package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskLanguage {

    private int languageCode;

    private Integer spokenCode;

    private Integer writtenCode;

    public JobdeskLanguage() {
    }

    public JobdeskLanguage(int languageCode, Integer spokenCode, Integer writtenCode) {
        this.languageCode = languageCode;
        this.spokenCode = spokenCode;
        this.writtenCode = writtenCode;
    }

    public int getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(int languageCode) {
        this.languageCode = languageCode;
    }

    public Integer getSpokenCode() {
        return spokenCode;
    }

    public void setSpokenCode(Integer spokenCode) {
        this.spokenCode = spokenCode;
    }

    public Integer getWrittenCode() {
        return writtenCode;
    }

    public void setWrittenCode(Integer writtenCode) {
        this.writtenCode = writtenCode;
    }
}
