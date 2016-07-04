DROP SCHEMA `library`;

CREATE SCHEMA `library` DEFAULT CHARACTER SET utf8;

USE library;


##############  Users and Permissions ##############

CREATE TABLE `library`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `active` TINYINT NOT NULL,
  `name` VARCHAR(70) NULL,
  `address` VARCHAR(70) NULL,
  `phone` VARCHAR(70) NULL,
  `comments` VARCHAR(70) NULL,
  `avatar` MEDIUMBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC));

CREATE TABLE `library`.`roles` (
  `role_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(70) NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `role_id_UNIQUE` (`role_id` ASC),
  UNIQUE INDEX `name` (`name` ASC));

CREATE TABLE `library`.`activities` (
  `activity_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(70) NULL,
  PRIMARY KEY (`activity_id`),
  UNIQUE INDEX `activity_id_UNIQUE` (`activity_id` ASC),
  UNIQUE INDEX `name` (`name` ASC));

CREATE TABLE library.user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_roles_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT user_roles_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (role_id)
);
CREATE UNIQUE INDEX user_roles_id_uindex ON library.user_roles (user_id);
CREATE INDEX user_id_index ON library.user_roles (user_id);
CREATE INDEX role_id_index ON library.user_roles (role_id);

CREATE TABLE library.role_activities
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  role_id INT NOT NULL,
  activity_id INT NOT NULL,
  CONSTRAINT role_activities_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (role_id),
  CONSTRAINT role_activities_activity_id_fk FOREIGN KEY (activity_id) REFERENCES activities (activity_id)
);
CREATE UNIQUE INDEX role_activities_id_uindex ON library.role_activities (id);
CREATE INDEX role_id_index ON library.role_activities (role_id);
CREATE INDEX activity_id_index ON library.role_activities (activity_id);


##############  Books ##############

CREATE TABLE library.categories
(
  category_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(70)
);
CREATE UNIQUE INDEX category_id_uindex ON library.categories (category_id);

CREATE TABLE library.books (
  book_id VARCHAR(35) NOT NULL,
  author VARCHAR(70) NULL,
  title VARCHAR(140) NULL,
  year INT NULL,
  category_id INT NULL,
  active TINYINT NOT NULL,
  about TEXT NULL DEFAULT NULL,
  cover MEDIUMBLOB NULL DEFAULT NULL,
  PRIMARY KEY (book_id),
  UNIQUE INDEX book_id_UNIQUE (book_id ASC));

USE library;
CREATE TABLE library.borrowed_books
(
  book_id VARCHAR(35) NOT NULL,
  user_id INT NOT NULL,
  librarian_id INT,
  date DATE NOT NULL,
  expire_date DATE NOT NULL,
  PRIMARY KEY (book_id, user_id),
  CONSTRAINT borrowed_books_user_id_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT borrowed_books_librarian_id_fk FOREIGN KEY (librarian_id) REFERENCES users (user_id),
  CONSTRAINT borrowed_books_book_id_fk FOREIGN KEY (book_id) REFERENCES books (book_id)
);
CREATE UNIQUE INDEX borrowed_books_id_uindex ON library.borrowed_books (book_id);
CREATE INDEX borrowed_books_user_id_index ON library.borrowed_books (user_id);

