package ch.alv.batches.commons.sql;

import java.util.Date;

public class SqlDataTypesHelper {

    public static java.sql.Date now() {
        return toSqlDate(new java.util.Date());
    }

    public static java.sql.Date toSqlDate(Date d) {
        return new java.sql.Date(d.getTime());
    }

}
