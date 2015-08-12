
BEGIN;

CREATE TABLE JOB (          ID INTEGER PRIMARY KEY,
                            JOB_ID VARCHAR(50),
                            FINGERPRINT TEXT,
                            JOB_ID_AVAM VARCHAR(50),
                            JOB_ID_EGOV VARCHAR(50),
                            URL VARCHAR(255),
                            TITLE_DE VARCHAR(255),
                            TITLE_FR VARCHAR(255),
                            TITLE_IT VARCHAR(255),
                            TITLE_EN VARCHAR(255),
                            DESCRIPTION_DE TEXT,
                            DESCRIPTION_FR TEXT,
                            DESCRIPTION_IT TEXT,
                            DESCRIPTION_EN TEXT,
                            START_DATE DATE,
                            END_DATE DATE,
                            AVAILABLE_NOW BIT,
                            PERMANENT_JOB BIT,
                            QUOTA_FROM INTEGER,
                            QUOTA_TO INTEGER,
                            FULLTIME BIT,
                            LOCATION_REMARKS_DE VARCHAR(255),
                            LOCATION_REMARKS_FR VARCHAR(255),
                            LOCATION_REMARKS_IT VARCHAR(255),
                            LOCATION_REMARKS_EN VARCHAR(255),
                            APPLICATION_WRITTEN BIT,
                            APPLICATION_ELECTRONICAL BIT,
                            -- APPLICATION_ELECTRONICAL_ADDRESS VARCHAR(255),
                            APPLICATION_PHONE BIT,
                            -- APPLICATION_PHONE_NUMBER VARCHAR(50),
                            APPLICATION_PERSONAL BIT,
                            COMPANY_NAME VARCHAR(255),
                            COMPANY_ADDRESS VARCHAR(255),
                            -- COMPANY_COUNTRY VARCHAR(2),
                            COMPANY_LOCATION_ID INTEGER,
                            COMPANY_PO_NUMBER VARCHAR(50),
                            COMPANY_PO_LOCATION_ID INTEGER,
                            COMPANY_PHONE INTEGER,
                            COMPANY_EMAIL VARCHAR(255),
                            COMPANY_URL VARCHAR(255),
                            -- CONTACT_GENDER BIT, -- use code or string?
                            CONTACT_FIRST_NAME VARCHAR(255),
                            CONTACT_LAST_NAME VARCHAR(255),
                            CONTACT_PHONE VARCHAR(50),
                            CONTACT_EMAIL VARCHAR(255),
                            ONLINE_SINCE DATE,
                            SOURCE VARCHAR(50),
                            EXTERNAL BIT,
                            ISCO_LEVEL_1 INTEGER,
                            ISCO_LEVEL_2 INTEGER,
                            ISCO_LEVEL_3 INTEGER,
                            ISCO_LEVEL_4 INTEGER);

CREATE TABLE JOB_LOCATION ( ID INTEGER PRIMARY KEY,
                            JOB_ID INTEGER,
                            LOCATION_ID INTEGER);

CREATE TABLE LOCATION (     ID INTEGER PRIMARY KEY,
                            ZIP VARCHAR(10), -- what type do we use here?
                            ZIP_ADDITIONAL_NUMBER VARCHAR(10), -- what type do we use here?
                            NAME VARCHAR(255), -- really no translated values --> check with dat
                            MUNICIPALITY_NAME VARCHAR(255), -- really no translated values --> check with dat
                            CANTON VARCHAR(50),
                            E INTEGER, -- is it used? check datatype
                            N INTEGER, -- is it used? check datatype
                            LAT INTEGER, -- check datatype
                            LON INTEGER, -- check datatype
                            BFS_NUMBER INTEGER,
                            AVAM_SEARCH_REGION VARCHAR(4)); -- check datatype

CREATE TABLE JOB_LANGUAGE ( ID INTEGER PRIMARY KEY,
                            JOB_ID INTEGER,
                            LANGUAGE_ID INTEGER,
                            SKILL_TYPE VARCHAR(50), -- oral or written
                            SKILL_LEVEL VARCHAR(50));


-- check naming
CREATE TABLE AUX_BFS_ISCO08 ( ID INTEGER PRIMARY KEY,
                              BFS_CODE VARCHAR(8),
                              ISCO_08_CODE VARCHAR(4),
                              ISCO_08_GROUP_1 VARCHAR(1),
                              ISCO_08_GROUP_2 VARCHAR(2),
                              ISCO_08_GROUP_3 VARCHAR(3),
                              PROFESSION_ID VARCHAR(20)
);

-- TODO: add foreign key constraints

COMMIT;
