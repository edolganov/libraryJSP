package com.examples.libraryJSP.model;

import com.examples.libraryJSP.beans.Book;
import com.examples.libraryJSP.beans.Category;
import com.examples.libraryJSP.beans.User;
import com.examples.libraryJSP.model.peers.BookPeer;
import com.examples.libraryJSP.model.peers.BorrowedBooksPeer;
import com.examples.libraryJSP.model.peers.CategoryPeer;
import com.examples.libraryJSP.model.peers.UserPeer;
import com.examples.libraryJSP.utils.ConstantManager;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * Created by Аяз on 21.06.2016.
 */
public class DataManager {
    private String dbDriver = "com.mysql.cj.jdbc.Driver";
    private String dbNewURL = "jdbc:mysql://localhost:3306";  // Create a new DB
    private String sqlCreateDatabase = "CREATE SCHEMA library DEFAULT CHARACTER SET utf8";
    private String dbURL = "jdbc:mysql://localhost:3306/library?autoReconnect=true" +
            "&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false" +
            "&serverTimezone=UTC" +
            "&allowMultiQueries=true"; // Connect to an existing DB//&useLegacyDatetimeCode=false";
    private String dbUserName = "root";
    private String dbPassword = "root";

    public void createDB() throws SQLException {
        // TODO: download sql from resource  (описание - sql.1 - script execute и др.)
        //Connection connection = DriverManager.getConnection(getDbNewURL(), getDbUserName(), getDbPassword());
        //Statement statement = connection.createStatement();
        //statement.executeUpdate(sqlCreateDatabase);
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(getDbURL(), getDbUserName(),
                    getDbPassword());
        } catch (SQLException e) {
            System.out.println("Could not connect to DB: " + e.getMessage());
        }
        return connection;
    }

    public void putConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    // TODO: correct, clear
    //---------- User operations ----------
    public boolean loginUser(String login, String pass, User user) {
        return UserPeer.authenticateUser(this, login, pass, user);
    }

    public ArrayList<User> getUserSearchResults(String keyword) {
        return UserPeer.searchUsers(this, keyword);
    }

    public User getUserById(int userId) {
        return UserPeer.getUserById(this, userId);
    }

    public User getUserByLogin(String login) {
        return UserPeer.getUserByLogin(this, login);
    }

    public boolean updateUserData(int userId, User newUserData, InputStream avatar) {
        return UserPeer.updateUserData(this, userId, newUserData, avatar);
    }

    public boolean applyUserRoles(int userId, User user) {
        return UserPeer.applyUserRoles(this, userId, user);
    }

    public void getUserRoles(User user) {
        UserPeer.getUserRoles(this, user);
    }

    public synchronized boolean addNewUser(User newUserData, InputStream avatar) {
        boolean addUserSucceed = false;
        if (newUserData != null && newUserData.getLogin() != null && !newUserData.getLogin().equals(""))
            if (getUserByLogin(newUserData.getLogin()) == null) {
                addUserSucceed = UserPeer.addNewUser(this, newUserData, avatar);
            }
        return addUserSucceed;
    }

    public boolean deleteUser(User user) {
        return user == null ? false : UserPeer.deleteUser(this, user);
    }

    public InputStream getAvatarByUserId(int userId) {
        return UserPeer.getAvatarByUserId(this, userId);
    }

    public boolean setAvatarByUserId(int userId, InputStream avatar) {
        return UserPeer.setAvatarByUserId(this, userId, avatar);
    }

    //---------- Category operations ----------
    public String getCategoryName(String categoryID) {
        Category category = CategoryPeer.getCategoryById(this, categoryID);
        return (category == null) ? null : category.getName();
    }

    public HashMap<String, String> getCategories() {
        return CategoryPeer.getAllCategories(this);
    }

    //---------- Book operations ----------
    public ArrayList<Book> getBookSearchResults(String keyword) {
        return BookPeer.searchBooks(this, keyword);
    }

    public ArrayList<Book> getBooksInCategory(String categoryId) {
        return BookPeer.getBooksByCategory(this, categoryId);
    }

    public Book getBookById(String bookId) {
        return BookPeer.getBookById(this, bookId);
    }

    public boolean bookAccessForIssue(Book book) {
        boolean bookAccessForIssue = false;
        if (book != null) {
            if (book.isActive() && !BorrowedBooksPeer.bookBorrowed(this, book)) {
                bookAccessForIssue = true;
            }
        }
        return bookAccessForIssue;
    }

    public boolean bookBorrowed(Book book) {
        boolean bookBorrowed = false;
        if (book != null) {
            if (BorrowedBooksPeer.bookBorrowed(this, book)) {
                bookBorrowed = true;
            }
        }
        return bookBorrowed;
    }

    // Issues and returns book (operations in table borrowed_books)
    public synchronized boolean issueBook(Book book, User serveUser, User librarian, boolean issue) {
        boolean issueReturnSucceed = false;
        if (issue) {
            Calendar currentDate = new GregorianCalendar();
            Calendar expireDate = new GregorianCalendar();
            expireDate.add(Calendar.DAY_OF_YEAR, ConstantManager.ALLOWED_BORROW_DAYS);
            java.sql.Date currentSqlDate = new java.sql.Date(currentDate.getTimeInMillis());
            java.sql.Date expireSqlDate = new java.sql.Date(expireDate.getTimeInMillis());
            issueReturnSucceed = (book != null && serveUser != null) ?
                    BorrowedBooksPeer.issueBook(this, book, serveUser, librarian, currentSqlDate, expireSqlDate, issue) : false;
        } else {
            issueReturnSucceed = (book != null) ?
                    BorrowedBooksPeer.issueBook(this, book, serveUser, librarian, null, null, issue) : false;
        }
        return issueReturnSucceed;
    }

    public int numberUserBorrowedBooks(User user) {
        return user == null ? -1 : BorrowedBooksPeer.numberUserBorrowedBooks(this, user);
    }

    public ArrayList<LinkedHashMap<String, String>> borrowedBooksList(User user, boolean expired) {
        return BorrowedBooksPeer.borrowedBooks(this, user, expired);
    }

    public synchronized boolean addNewBook(Book NewBookData, byte[] about, InputStream cover) {
        boolean addBookSucceed = false;
        if (NewBookData != null && NewBookData.getId() != null && !NewBookData.getId().equals(""))
            if (getBookById(NewBookData.getId()) == null) {
                addBookSucceed = BookPeer.addNewBook(this, NewBookData, about, cover);
            }
        return addBookSucceed;
    }

    public InputStream getCoverByBookId(String bookId) {
        return BookPeer.getCoverByBookId(this, bookId);
    }

    public boolean deleteBook(Book book) {
        return book == null ? false : BookPeer.deleteBook(this, book);
    }

    public boolean updateBookData(String bookId, Book newBookData, InputStream cover) {
        return BookPeer.updateBookData(this, bookId, newBookData, cover);
    }

    public boolean setCoverByBookId(String bookId, InputStream cover) {
        return BookPeer.setCoverByBookId(this, bookId, cover);
    }


    // ------ Setters ------
    public void setDbNewURL(String dbNewURL) {
        this.dbNewURL = dbNewURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    // ------ Getters ------
    public String getDbNewURL() {
        return dbNewURL;
    }

    public String getDbURL() {
        return dbURL;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDriver() {
        return dbDriver;
    }
}
