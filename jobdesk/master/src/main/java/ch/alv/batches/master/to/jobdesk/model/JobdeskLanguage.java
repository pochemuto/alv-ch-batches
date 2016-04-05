package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskLanguage {

    private int languageCode;

    private Short spokenCode;

    private Short writtenCode;

    public JobdeskLanguage() {
    }

    public JobdeskLanguage(int languageCode, Short spokenCode, Short writtenCode) {
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

    public Short getSpokenCode() {
        return spokenCode;
    }

    public void setSpokenCode(Short spokenCode) {
        this.spokenCode = spokenCode;
    }

    public Short getWrittenCode() {
        return writtenCode;
    }

    public void setWrittenCode(Short writtenCode) {
        this.writtenCode = writtenCode;
    }
}
