package ch.alv.batches.master.to.jobdesk.model;

/**
 * Created by stibe on 23.08.15.
 */
public class JobdeskLanguage {

    private int languageCode;

    private int spokenCode;

    private int writtenCode;

    public JobdeskLanguage() {
    }

    public JobdeskLanguage(int languageCode, int spokenCode, int writtenCode) {
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

    public int getSpokenCode() {
        return spokenCode;
    }

    public void setSpokenCode(int spokenCode) {
        this.spokenCode = spokenCode;
    }

    public int getWrittenCode() {
        return writtenCode;
    }

    public void setWrittenCode(int writtenCode) {
        this.writtenCode = writtenCode;
    }
}
