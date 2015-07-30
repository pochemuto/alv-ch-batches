package ch.alv.batches.companydata;

/**
 * Created by stibe on 30.07.15.
 */
public class SqlStatements {

    private static final String INSERT_TEMPLATE = "INSERT INTO %s (ID, " +
            "BETID, " +
            "EMAIL, " +
            "KANTON, " +
            "NAME, " +
            "NAME2, " +
            "ORT, " +
            "PLZ, " +
            "STRASSE, " +
            "TELEFONNUMMER, " +
            "TODELETE) " +
            "VALUES (" +
            ":id, " +
            ":companyId, " +
            ":email, " +
            ":canton, " +
            ":name, " +
            ":name2, " +
            ":city, " +
            ":zip, " +
            ":street, " +
            ":phone, " +
            ":toDelete)";

    public static final String INSERT_STAGED_COMPANY = String.format(INSERT_TEMPLATE, "STAGING_AVG_FIRMEN_IMPORT");

    public static final String SELECT_COMPANIES_FOR_UPDATES = "SELECT * FROM STAGING_AVG_FIRMEN_IMPORT WHERE ID in (SELECT ID FROM AVG_FIRMEN)";

    public static final String SELECT_COMPANIES_FOR_CREATION = "SELECT * FROM STAGING_AVG_FIRMEN_IMPORT WHERE ID NOT IN (SELECT ID FROM AVG_FIRMEN)";

    public static final String SELECT_COMPANIES_FOR_TODELETE_MARKERS = "SELECT * FROM AVG_FIRMEN WHERE ID NOT IN (SELECT ID STAGING_AVG_FIRMEN_IMPORT) AND TODELETE IS NULL";

    public static final String INSERT_COMPANY = String.format(INSERT_TEMPLATE, "STAGING_AVG_FIRMEN_IMPORT");

    public static final String UPDATE_COMPANY = "UPDATE TABLE AVG_FIRMEN SET " +
            "BETID = :companyId, " +
            "EMAIL = :email, " +
            "KANTON = :canton, " +
            "NAME = :name, " +
            "NAME2 = :name2, " +
            "ORT = :city, " +
            "PLZ = :zip, " +
            "STRASSE= :street, " +
            "TELEFONNUMMER = :phone, " +
            "TODELETE = :toDelete)";

    public static final String UPDATE_TODELETE_COMPANY_TEMPLATE = "UPDATE TABLE AVG_FIRMEN SET TODELETE = '%s'";
}
