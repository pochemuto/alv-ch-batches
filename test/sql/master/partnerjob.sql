
BEGIN;

CREATE TABLE OSTE_PARTNER ( ID VARCHAR(100) PRIMARY KEY,
                            QUELLE VARCHAR(10) DEFAULT 'admin' NOT NULL,
                            BEZEICHNUNG VARCHAR(2000),
                            BESCHREIBUNG TEXT,
                            BERUFSGRUPPE INTEGER,
                            UNT_NAME VARCHAR(255),
                            ARBEITSORT_PLZ VARCHAR(255),
                            PENSUM_VON INTEGER,
                            PENSUM_BIS INTEGER,
                            URL_DETAIL TEXT,
                            ANMELDE_DATUM DATE,
                            SPRACHE CHAR(2)
                          );
COMMIT;
