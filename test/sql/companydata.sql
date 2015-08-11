
BEGIN;

CREATE TABLE STAGING_AVG_FIRMEN_IMPORT (ID INTEGER PRIMARY KEY,
                                        BETID TEXT,
                                        EMAIL TEXT,
                                        KANTON VARCHAR(50),
                                        NAME TEXT,
                                        NAME2 TEXT,
                                        ORT TEXT,
                                        PLZ VARCHAR(6),
                                        STRASSE TEXT,
                                        TELEFONNUMMER VARCHAR(50),
                                        TODELETE DATE);

CREATE TABLE AVG_FIRMEN                 (ID INTEGER PRIMARY KEY,
                                        BETID TEXT,
                                        EMAIL TEXT,
                                        KANTON VARCHAR(50),
                                        NAME TEXT,
                                        NAME2 TEXT,
                                        ORT TEXT,
                                        PLZ VARCHAR(6),
                                        STRASSE TEXT,
                                        TELEFONNUMMER VARCHAR(50),
                                        TODELETE DATE);

COMMIT;
