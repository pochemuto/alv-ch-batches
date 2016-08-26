package ch.alv.batches.partnerjob.to.master.batch;

import java.text.SimpleDateFormat;

/**
 * This class provides some constants and methods used to format the data to
 * be inserted into the OSTE_PARTNER table.
 */
public final class PartnerJobImport {

    private PartnerJobImport() { }

    public static final int DESC_MAX_LENGTH = 10000;
    public static final String DESC_TRUNCATE_SUFFIX = " [...]";

    // This column is currently and unfortunately stored as text, not as datatime, in the SQL database.
    public static final SimpleDateFormat DB_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'-00.00.00.000000'");

    public static String abbreviateDescription(String description) {
        String result = description.trim();
        if (result.length() > DESC_MAX_LENGTH) {
            result = result.substring(0, DESC_MAX_LENGTH - DESC_TRUNCATE_SUFFIX.length()) + DESC_TRUNCATE_SUFFIX;
        }
        return result;
    }
}
