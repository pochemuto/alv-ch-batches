
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
                            LOCATION_REMARKS_DE VARCHAR(255),
                            LOCATION_REMARKS_FR VARCHAR(255),
                            LOCATION_REMARKS_IT VARCHAR(255),
                            LOCATION_REMARKS_EN VARCHAR(255),
                            APPLICATION_WRITTEN BIT,
                            APPLICATION_ELECTRONICAL BIT,
                            APPLICATION_PHONE BIT,
                            APPLICATION_PERSONAL BIT,

                            );

CREATE TABLE JOB_LOCATION ( ID INTEGER PRIMARY KEY,
                            JOB_ID INTEGER,
                            LOCATION_ID INTEGER);

CREATE TABLE LOCATION (     ID INTEGER PRIMARY KEY,
                            ZIP TEXT,
                            LAT LONG,
                            LON LONG,
                            NAME_DE VARCHAR(255),
                            NAME_FR VARCHAR(255),
                            NAME_IT VARCHAR(255),
                            NAME_EN VARCHAR(255),
                            CANTON VARCHAR(50) );

COMMIT;
