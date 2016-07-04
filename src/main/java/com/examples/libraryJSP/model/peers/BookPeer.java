package com.examples.libraryJSP.model.peers;

import com.examples.libraryJSP.beans.Book;
import com.examples.libraryJSP.beans.User;
import com.examples.libraryJSP.model.DataManager;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Аяз on 21.06.2016.
 */
public class BookPeer {

    public static ArrayList<Book> searchBooks(DataManager dataManager, String keyword) {
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM books";
                if (keyword != null && !keyword.trim().equals("")) {
                    sql += " WHERE title LIKE '%" + keyword.trim() + "%'" +
                    " OR author LIKE '%" + keyword.trim() + "%'";
                }
                try (ResultSet rs = statement.executeQuery(sql)) {
                    while (rs.next()) {
                        Book book = new Book();
                        book.setId(rs.getString("book_id"));
                        book.setAuthor(rs.getString("author"));
                        book.setTitle(rs.getString("title"));
                        book.setYear(rs.getInt("year"));
                        book.setActive(rs.getBoolean("active"));
                        book.setCategory_id(rs.getInt("category_id"));

                        books.add(book);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return books;
    }

    public static ArrayList<Book> getBooksByCategory(DataManager dataManager, String categoryId) {
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM books WHERE category_id = " + categoryId;
                try (ResultSet rs = statement.executeQuery(sql)) {
                    while (rs.next()) {
                        Book book = new Book();
                        book.setId(rs.getString("book_id"));
                        book.setAuthor(rs.getString("author"));
                        book.setTitle(rs.getString("title"));
                        book.setYear(rs.getInt("year"));
                        book.setActive(rs.getBoolean("active"));
                        book.setCategory_id(rs.getInt("category_id"));

                        books.add(book);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return books;
    }

    public static Book getBookById(DataManager dataManager, String bookId) {
        Book book = null;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM books WHERE book_id = '" + bookId + "'";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        book = new Book();
                        book.setId(rs.getString("book_id"));
                        book.setAuthor(rs.getString("author"));
                        book.setTitle(rs.getString("title"));
                        book.setYear(rs.getInt("year"));
                        book.setActive(rs.getBoolean("active"));
                        book.setCategory_id(rs.getInt("category_id"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return book;
    }

    public static boolean addNewBook(DataManager dataManager, Book newBookData, byte[] about,  InputStream cover) {
        boolean addBookSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            String sql = "insert into library.books" +
                    " (book_id, author, title, year, category_id, active, about, cover)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // TODO: sql
                preparedStatement.setString(1, newBookData.getId());
                preparedStatement.setString(2, newBookData.getAuthor());
                preparedStatement.setString(3, newBookData.getTitle());
                preparedStatement.setInt(4, newBookData.getYear());
                preparedStatement.setInt(5, newBookData.getCategory_id());
                preparedStatement.setBoolean(6, newBookData.isActive());
                preparedStatement.setBytes(7, newBookData.getAbout());
                preparedStatement.setBlob(8, cover);
                if (preparedStatement.executeUpdate() > 0) {
                    addBookSucceed = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return addBookSucceed;
    }

    public static InputStream getCoverByBookId(DataManager dataManager, String bookId) {
        InputStream cover = null;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT cover FROM books WHERE book_id = '" + bookId + "'";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        cover = rs.getBinaryStream("cover");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return cover;

    }

    public static boolean deleteBook(DataManager dataManager, Book book) {
        boolean deleteSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try {
                // TODO: уровни изолированности и др.
                connection.setAutoCommit(false);
                try (Statement statement = connection.createStatement()) {
                    // TODO: sql check borrowed books
                    String sql = "SELECT book_id FROM borrowed_books WHERE book_id = '" +
                            book.getId() + "'";
                    try (ResultSet rs = statement.executeQuery(sql)) {
                        if (rs.next()) {
                        } else {
                            // TODO: sql delete user
                            sql = "DELETE FROM books WHERE book_id = '" +
                                    book.getId() + "'";
                            if (statement.executeUpdate(sql) > 0) {
                                deleteSucceed = true;
                                connection.commit();
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } finally {
                // connection.setAutoCommit(true);
            }
            dataManager.putConnection(connection);
        }
        return deleteSucceed;
    }

    public static boolean updateBookData(DataManager dataManager, String bookId, Book newBookData, InputStream cover) {
        boolean updateSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            // TODO: sql
            String sql = "UPDATE library.books" +
                    " SET author = ?, title= ?, year= ?, category_id= ?," +
                    " active= ?, about= ?, cover = ?" +
                    " WHERE book_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newBookData.getAuthor());
                preparedStatement.setString(2, newBookData.getTitle());
                preparedStatement.setInt(3, newBookData.getYear());
                preparedStatement.setInt(4, newBookData.getCategory_id());
                preparedStatement.setBoolean(5, newBookData.isActive());
                preparedStatement.setBytes(6, newBookData.getAbout());
                preparedStatement.setBlob(7, cover);
                preparedStatement.setString(8, bookId);
                // TODO: transaction ? preparedStatement?
                if (preparedStatement.executeUpdate() > 0) {
                    updateSucceed = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return updateSucceed;
    }

    public static boolean setCoverByBookId(DataManager dataManager, String bookId, InputStream cover) {
        boolean setCoverSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            // TODO: sql
            String sql = "UPDATE library.books" +
                    " SET cover = ? WHERE book_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setBlob(1, cover);
                preparedStatement.setString(2, bookId);
                if (preparedStatement.executeUpdate() > 0) {
                    setCoverSucceed = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return setCoverSucceed;
    }

}
