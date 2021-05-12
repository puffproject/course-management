INSERT INTO COURSE (ID, CODE, LEVEL, TERM, ACADEMIC_YEAR)
VALUES
(1, 'COMPSCI 1JC3', 1, 'FALL', 2019),
(2, 'COMPSCI 1MD3', 1, 'WINTER', 2019),
(3, 'COMPSCI 1XA3', 1, 'WINTER', 2019),

(4, 'COMPSCI 1JC3', 1, 'FALL', 2020),
(5, 'COMPSCI 1MD3', 1, 'FALL', 2020),
(6, 'COMPSCI 1DM3', 1, 'WINTER', 2020),
(7, 'COMPSCI 1XC3', 1, 'WINTER', 2020);

INSERT INTO COURSE_ATTR (ID, COURSE_ID, ATTR_NAME, ATTR_VALUE)
VALUES
(1, 1, 'professorName', 'William M. Farmer'),
(2, 1, 'title', 'Introduction to Computational Thinking'),
(3, 1, 'description', 'Monads, Inverse functions, Lambda algebra, Clock arithmetic, Computational keys.'),
(4, 1, 'sections', 'C01, C02'),
(5, 1, 'weight', '3 units'),
(6, 1, 'prerequisites', 'COMPSCI 2CO3, COMPSCI 2FA3'),

(7, 2, 'professorName', 'George Fizherbert'),
(8, 2, 'title', 'Introduction to Programming'),

(9, 3, 'professorName', 'Curtis D''Alves'),
(10, 3, 'title', 'Practice and Expertise');