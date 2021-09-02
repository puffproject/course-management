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

INSERT INTO VOTE (ID, SOURCE_TYPE, SOURCE_ITEM_ID, AUTHOR_ID, ACTION)
VALUES
(1, 'COMMENT', 1, 'X01', 'UPVOTE'),
(2, 'COMMENT', 1, 'X02', 'DOWNVOTE'),
(3, 'COMMENT', 1, 'X03', 'UPVOTE'),

(4, 'COMMENT', 2, 'X01', 'UPVOTE'),
(5, 'COMMENT', 2, 'X02', 'UPVOTE'),

(6, 'CASE', 1000, 'X01', 'DOWNVOTE'),
(7, 'CASE', 1000, 'X03', 'DOWNVOTE'),

(8, 'SUITE', 2, 'X01', 'UPVOTE'),
(9, 'SUITE', 2, 'X02', 'UPVOTE'),
(10, 'SUITE', 2, 'X03', 'UPVOTE'),
(11, 'SUITE', 2, 'X04', 'DOWNVOTE');

INSERT INTO COMMENT (ID, SOURCE_TYPE, SOURCE_ITEM_ID, AUTHOR_ID, CONTENT, UPVOTE_COUNT)
VALUES
(1, 'CASE', 1000, 'X01', 'This isn''t that great of a test case!', -2),
(2, 'CASE', 1000, 'X02', 'How about being positive? :smile:', 0),

(3, 'CASE', 1001, 'X03', 'Should we have done this instead? `x = 3`?', 1);