USE library;
INSERT INTO categories (
    category_id
  , category_name
  )
  VALUES
    (1,'Web Development')
  , (2,'SF')
  , (3,'Action Novels')
  ;
INSERT INTO books (
    book_id
  , title
  , author
  , year
  , category_id
  , active
  )
  VALUES
    (1,'Web Standards','Leslie Sikos',2000,1,TRUE)
  , (2,'Getting Started with CSS','David Powers',2001,1,TRUE)
  , (3,'The Complete Robot','Isaac Asimov',2000,2,TRUE)
  , (4,'Foundation','Isaac ASimov',2001,2,TRUE)
  , (5,'Area 7','Matthew Reilly',2000,3,TRUE)
  , (6,'Term Limits','Vince Flynn',2001,3,TRUE)
  ;

SELECT * FROM books

SELECT * FROM activities WHERE activity_id IN
(SELECT activity_id FROM role_activities
WHERE role_id IN (SELECT role_id FROM user_roles WHERE user_id = 1))