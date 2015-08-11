
BEGIN;

CREATE TABLE OSTE_ADMIN (ID VARCHAR(255) PRIMARY KEY,
                         BEZEICHNUNG VARCHAR(2000),
                         BESCHREIBUNG VARCHAR(2000),
                         BERUFSGRUPPE INTEGER,
                         UNT_NAME VARCHAR(255),
                         ARBEITSORT_PLZ VARCHAR(10),
                         PENSUM_VON SMALLINT,
                         PENSUM_BIS SMALLINT,
                         URL_DETAIL TEXT,
                         ANMELDE_DATUM DATE,
                         SPRACHE CHAR(2));

COMMIT;
