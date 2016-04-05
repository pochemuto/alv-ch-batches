
BEGIN;

-- FIXME unique/not-null constraints,...
CREATE TABLE JOB (          ID SERIAL PRIMARY KEY,
                            JOB_ID TEXT,
                            JOB_ID_AVAM TEXT,
                            JOB_ID_EGOV TEXT,
                            URL TEXT,
                            TITLE_DE TEXT,
                            TITLE_FR TEXT,
                            TITLE_IT TEXT,
                            TITLE_EN TEXT,
                            DESCRIPTION_DE TEXT,
                            DESCRIPTION_FR TEXT,
                            DESCRIPTION_IT TEXT,
                            DESCRIPTION_EN TEXT,
                            START_DATE DATE,
                            END_DATE DATE,
                            QUOTA_FROM INTEGER,
                            QUOTA_TO INTEGER,
                            LOCATION_REMARKS_DE TEXT,
                            LOCATION_REMARKS_FR TEXT,
                            LOCATION_REMARKS_IT TEXT,
                            LOCATION_REMARKS_EN TEXT,
                            WORKPLACE_ZIP TEXT,
                            APPLICATION_WRITTEN BOOLEAN,
                            APPLICATION_ELECTRONICAL BOOLEAN,
                            APPLICATION_PHONE BOOLEAN,
                            APPLICATION_PERSONAL BOOLEAN,
                            COMPANY_NAME TEXT,
                            COMPANY_ADDRESS TEXT,
                            COMPANY_COUNTRY TEXT,
                            COMPANY_ZIP TEXT,
                            COMPANY_CITY TEXT,
                            COMPANY_PO_NUMBER TEXT,
                            COMPANY_PO_ZIP TEXT,
                            COMPANY_PO_CITY TEXT,
                            COMPANY_PHONE TEXT,
                            COMPANY_EMAIL TEXT,
                            COMPANY_URL TEXT,
                            CONTACT_GENDER SMALLINT,
                            CONTACT_FIRST_NAME TEXT,
                            CONTACT_LAST_NAME TEXT,
                            CONTACT_PHONE TEXT,
                            CONTACT_EMAIL TEXT,
                            PUBLICATION_DATE DATE NOT NULL,
                            SOURCE TEXT NOT NULL CHECK (SOURCE IN ('RAV', 'X28')), --FIXME switch to an enum/custom type?
                            ISCO08_ID CHAR(4) NOT NULL CHECK (ISCO08_ID ~ '^\d{4}$')
             );

CREATE TABLE LOCATION (     ID SERIAL PRIMARY KEY,
                            ZIP TEXT NOT NULL,
                            ZIP_ADDITIONAL_NUMBER INTEGER NOT NULL,
                            NAME TEXT NOT NULL,
                            MUNICIPALITY_NAME TEXT NOT NULL,
                            CANTON VARCHAR(50) NOT NULL,
                            LAT DOUBLE PRECISION NOT NULL,
                            LON DOUBLE PRECISION NOT NULL,
                            BFS_NUMBER INTEGER,
                            AVAM_SEARCH_REGION VARCHAR(4),

                            CONSTRAINT LOCATION_CONSTRAINT_UNIQUE_1 UNIQUE(LAT, LON)
--                            CONSTRAINT LOCATION_CONSTRAINT_UNIQUE_2 UNIQUE(ZIP, ZIP_ADDITIONAL_NUMBER)  --> not working, e.g. PLZ 6719,6618
);
-- FIXME JOB_ID 2 meanings!
CREATE TABLE JOB_LOCATION ( JOB_ID INTEGER NOT NULL,
                            LOCATION_ID INTEGER NOT NULL,
                            PRIMARY KEY(JOB_ID, LOCATION_ID),
                            CONSTRAINT JOB_LOCATION_JOB_ID_FK FOREIGN KEY (JOB_ID) REFERENCES JOB ON DELETE CASCADE,
                            CONSTRAINT JOB_LOCATION_LOCATION_ID_FK FOREIGN KEY (LOCATION_ID) REFERENCES LOCATION ON DELETE CASCADE
                          );

-- FIXME JOB_ID 2 meanings!
CREATE TABLE JOB_LANGUAGE ( JOB_ID INTEGER NOT NULL,
                            LANGUAGE_ID INTEGER NOT NULL,
                            SKILL_SPOKEN SMALLINT, -- TODO should be enum 1..4, or restricted with a check constraint
                            SKILL_WRITTEN SMALLINT,
                            PRIMARY KEY(JOB_ID, LANGUAGE_ID),
                            CONSTRAINT JOB_LANGUAGE_JOB_ID_FK FOREIGN KEY (JOB_ID) REFERENCES JOB ON DELETE CASCADE
                          );

-- check naming
CREATE TABLE CODE_BFS_ISCO08 ( ID INTEGER PRIMARY KEY,
                               BFS_CODE INTEGER,
                               ISCO_08_CODE INTEGER,
                               ISCO_08_GROUP_1 INTEGER,
                               ISCO_08_GROUP_2 INTEGER,
                               ISCO_08_GROUP_3 INTEGER,
                               PROFESSION_ID TEXT);

CREATE TABLE SEARCH_REGIONS ( ID INTEGER PRIMARY KEY,
                              BFS_CODE INTEGER,
                              ZIP INTEGER,
                              REGION TEXT,
                              CANTON TEXT);

COMMIT;
