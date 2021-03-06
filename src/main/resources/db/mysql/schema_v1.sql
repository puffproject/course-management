CREATE TABLE IF NOT EXISTS COURSE_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO COURSE_SEQUENCE (NEXT_VAL) VALUES (1000);

CREATE TABLE IF NOT EXISTS COURSE_ATTR_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO COURSE_ATTR_SEQUENCE (NEXT_VAL) VALUES (1000);

CREATE TABLE IF NOT EXISTS ASSIGNMENT_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO ASSIGNMENT_SEQUENCE (NEXT_VAL) VALUES (1000);

CREATE TABLE IF NOT EXISTS ASSIGNMENT_ATTR_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO ASSIGNMENT_ATTR_SEQUENCE (NEXT_VAL) VALUES (1000);

CREATE TABLE IF NOT EXISTS ASSIGNMENT_ENROLLMENT_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO ASSIGNMENT_ENROLLMENT_SEQUENCE (NEXT_VAL) VALUES (1000);

CREATE TABLE IF NOT EXISTS COMMENT_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO COMMENT_SEQUENCE (NEXT_VAL) VALUES (1000);

CREATE TABLE IF NOT EXISTS VOTE_SEQUENCE (NEXT_VAL BIGINT NOT NULL);
INSERT INTO VOTE_SEQUENCE (NEXT_VAL) VALUES (10000);

CREATE TABLE IF NOT EXISTS COURSE (
    ID INT NOT NULL PRIMARY KEY,
    CODE VARCHAR(63) NOT NULL,
    LEVEL INT NOT NULL,
    TERM VARCHAR(63) NOT NULL,
    ACADEMIC_YEAR INT NOT NULL,
    CONSTRAINT U_COURSE_CODE_LEVEL_CODE_ACADEMIC_YEAR UNIQUE (CODE, LEVEL, TERM, ACADEMIC_YEAR)
);

CREATE TABLE IF NOT EXISTS COURSE_ATTR (
    ID INT NOT NULL PRIMARY KEY,
    COURSE_ID INT NOT NULL,
    ATTR_NAME VARCHAR(250) NOT NULL,
    ATTR_VALUE VARCHAR(10000) DEFAULT NULL,
    CONSTRAINT U_COURSE_ATTR_COURSE_ID_ATTR_NAME_UNIQUE UNIQUE (COURSE_ID, ATTR_NAME),
    CONSTRAINT FK_COURSE_ATTR_COURSE_ID_COURSE_ID FOREIGN KEY (COURSE_ID) REFERENCES COURSE(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ASSIGNMENT (
    ID INT NOT NULL PRIMARY KEY,
    COURSE_ID INT NOT NULL,
    NAME VARCHAR(250) NOT NULL,
    DUE_DATE DATE,
    CONSTRAINT U_ASSIGNMENT_COURSE_ID_NAME UNIQUE (COURSE_ID, NAME),
    CONSTRAINT FK_ASSIGNMENT_COURSE_ID_COURSE_ID FOREIGN KEY (COURSE_ID) REFERENCES COURSE(ID)
);

CREATE TABLE IF NOT EXISTS ASSIGNMENT_ATTR (
    ID INT NOT NULL PRIMARY KEY,
    ASSIGNMENT_ID INT NOT NULL,
    ATTR_NAME VARCHAR(250) NOT NULL,
    ATTR_VALUE VARCHAR(10000) DEFAULT NULL,
    CONSTRAINT U_ASSIGNMENT_ATTR_ASSIGNMENT_ID_NAME UNIQUE (ASSIGNMENT_ID, ATTR_NAME),
    CONSTRAINT FK_ASSIGNMENT_ATTR_ASSIGNMENT_ID_ASSIGNMENT_ID FOREIGN KEY (ASSIGNMENT_ID) REFERENCES ASSIGNMENT(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ASSIGNMENT_ENROLLMENT (
    ID INT NOT NULL PRIMARY KEY,
    ASSIGNMENT_ID INT NOT NULL,
    USER_ID VARCHAR(40) NOT NULL,
    PINNED BOOLEAN DEFAULT FALSE,
    CONSTRAINT U_ASSIGNMENT_ID_USER_ID UNIQUE (ASSIGNMENT_ID, USER_ID),
    CONSTRAINT FK_ASSIGNMENT_ENROLLMENT_ASSIGNMENT_ID_ASSIGNMENT_ID FOREIGN KEY (ASSIGNMENT_ID) REFERENCES ASSIGNMENT(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMMENT (
    ID INT NOT NULL PRIMARY KEY,
    CREATED DATETIME DEFAULT CURRENT_TIMESTAMP,
    SOURCE_TYPE VARCHAR(50) NOT NULL,
    SOURCE_ITEM_ID INT NOT NULL,
    AUTHOR_ID VARCHAR(40) NOT NULL,
    CONTENT VARCHAR(10000) DEFAULT NULL,
    LAST_EDITED DATETIME DEFAULT NULL,
    UPVOTE_COUNT INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS VOTE (
    ID INT NOT NULL PRIMARY KEY,
    SOURCE_TYPE VARCHAR(50) NOT NULL,
    SOURCE_ITEM_ID INT NOT NULL,
    AUTHOR_ID VARCHAR(40) NOT NULL,
    ACTION VARCHAR(63) NOT NULL,
    CONSTRAINT U_VOTE_AUTHOR_ID_SOURCE_TYPE_SOURCE_ITEM_ID UNIQUE (AUTHOR_ID, SOURCE_TYPE, SOURCE_ITEM_ID)
);