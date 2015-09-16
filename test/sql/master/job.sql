
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
                            LOCATION_ID INTEGER,
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
                            ONLINE_SINCE_DATE DATE,
                            SOURCE TEXT,
                            ISCO_LEVEL_1 INTEGER,
                            ISCO_LEVEL_2 INTEGER,
                            ISCO_LEVEL_3 INTEGER,
                            ISCO_LEVEL_4 INTEGER);

CREATE TABLE LOCATION (     ID INTEGER PRIMARY KEY,
                            ZIP TEXT,
                            ZIP_ADDITIONAL_NUMBER TEXT,
                            NAME TEXT,
                            MUNICIPALITY_NAME TEXT,
                            CANTON VARCHAR(50),
                            LAT DOUBLE PRECISION,
                            LON DOUBLE PRECISION,
                            BFS_NUMBER INTEGER,
                            AVAM_SEARCH_REGION VARCHAR(4));
-- JOB_ID 2 meanings!
-- MULTI-Field primary key instead ?
CREATE TABLE JOB_LOCATION ( ID INTEGER PRIMARY KEY,
                            JOB_ID INTEGER,
                            LOCATION_ID INTEGER);

ALTER TABLE JOB_LOCATION
ADD CONSTRAINT JOB_LOCATION_JOB_ID_FK
FOREIGN KEY (JOB_ID)
REFERENCES JOB
ON DELETE CASCADE;

ALTER TABLE JOB_LOCATION
ADD CONSTRAINT JOB_LOCATION_LOCATION_ID_FK
FOREIGN KEY (LOCATION_ID)
REFERENCES LOCATION
ON DELETE CASCADE;

CREATE TABLE JOB_LANGUAGE ( ID INTEGER PRIMARY KEY,
                            JOB_ID INTEGER,
                            LANGUAGE_ID INTEGER,
                            SKILL_SPOKEN SMALLINT,
                            SKILL_WRITTEN SMALLINT,
  CONSTRAINT JOB_LANGUAGE_UNIQUENESS_CONSTRAINT UNIQUE (JOB_ID, LANGUAGE_ID));

ALTER TABLE JOB_LANGUAGE
ADD CONSTRAINT JOB_LANGUAGE_JOB_ID_FK
FOREIGN KEY (JOB_ID)
REFERENCES JOB
ON DELETE CASCADE;

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
