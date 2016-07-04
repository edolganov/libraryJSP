package com.examples.libraryJSP.model.peers;

import com.examples.libraryJSP.beans.Book;
import com.examples.libraryJSP.beans.User;
import com.examples.libraryJSP.model.DataManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Аяз on 21.06.2016.
 */
public class BorrowedBooksPeer {

    public static boolean bookBorrowed(DataManager dataManager, Book book) {
        boolean bookBorrowed = true;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM borrowed_books WHERE book_id = '"
                        + book.getId() + "'";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                    } else {
                        bookBorrowed = false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return bookBorrowed;
    }

    public static boolean issueBook(DataManager dataManager, Book book,
                                    User serveUser, User librarian, Date currentDate, Date expireDate, boolean issue) {
        boolean issueReturnSucceed = false;
        if (issue) {
            Connection connection = dataManager.getConnection();
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    String sql = "SELECT * FROM borrowed_books WHERE book_id = '"
                            + book.getId() + "'";
                    try (ResultSet rs = statement.executeQuery(sql)) {
                        if (rs.next()) {
                        } else {
                            sql = "insert into borrowed_books" +
                                    " (user_id, book_id, librarian_id, date, expire_date)" +
                                    " values ('" +
                                    serveUser.getUserId() + "', '" + book.getId() + "', '"
                                    + librarian.getUserId() + "', '" + currentDate + "', '" + expireDate + "')";
                            // TODO: transaction ? preparedStatement?
                            if (statement.executeUpdate(sql) > 0) {
                                issueReturnSucceed = true;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dataManager.putConnection(connection);
            }
        } else {
            Connection connection = dataManager.getConnection();
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    String sql = "DELETE FROM borrowed_books WHERE book_id = '"
                            + book.getId() + "'";
                    if (statement.executeUpdate(sql) > 0) {
                        issueReturnSucceed = true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dataManager.putConnection(connection);
            }
        }
        return issueReturnSucceed;
    }

    public static ArrayList<LinkedHashMap<String, String>> borrowedBooks(DataManager dataManager, User user, boolean expired) {
        ArrayList<LinkedHashMap<String, String>> borrowedBooks = new ArrayList<>();

        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM borrowed_books bb";
                sql += " LEFT JOIN users u ON bb.user_id = u.user_id" +
                        " LEFT JOIN books b ON bb.book_id = b.book_id";
                if (user != null) {
                    sql += " WHERE bb.user_id = '"
                            + user.getUserId() + "'";
                    if (expired) {
                        sql += " AND expire_date < '" + (new java.sql.Date(System.currentTimeMillis())) + "''";
                    }
                } else {
                    if (expired) {
                        sql += " WHERE expire_date < '" + (new java.sql.Date(System.currentTimeMillis())) + "'";
                    }
                }
                try (ResultSet rs = statement.executeQuery(sql)) {
                    while (rs.next()) {
                        LinkedHashMap<String, String> entry = new LinkedHashMap<>();
                        entry.put("book_id", rs.getString("book_id"));
                        entry.put("title", rs.getString("title"));
                        entry.put("author", rs.getString("author"));
                        entry.put("year", rs.getString("year"));
                        entry.put("user_id", Integer.toString(rs.getInt("user_id")));
                        entry.put("expire_date", rs.getDate("expire_date").toString());
                        borrowedBooks.add(entry);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return borrowedBooks;
    }

    public static int numberUserBorrowedBooks(DataManager dataManager, User user) {
        int numberUserBorrowedBooks = -1;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT COUNT(*) count FROM borrowed_books WHERE user_id = '"
                        + user.getUserId() + "'";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        numberUserBorrowedBooks = rs.getInt("count");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return numberUserBorrowedBooks;
    }
}
