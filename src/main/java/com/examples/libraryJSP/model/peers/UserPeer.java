package com.examples.libraryJSP.model.peers;

import com.examples.libraryJSP.beans.User;
import com.examples.libraryJSP.beans.permissions.Activities;
import com.examples.libraryJSP.beans.permissions.Roles;
import com.examples.libraryJSP.model.DataManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Аяз on 28.06.2016.
 */
public class UserPeer {

    public static boolean authenticateUser(DataManager dataManager,
                                           String login, String pass, User user) {
        boolean authenticateSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM users WHERE login = '" + login +
                        "' AND password = '" + pass + "'";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        if (user != null) {
                            user.setLogin(rs.getString("login"));
                            user.setUserId(rs.getInt("user_id"));
                            user.setName(rs.getString("name"));
                            user.setActive(rs.getBoolean("active"));
                            user.setAddress(rs.getString("address"));
                            user.setComments(rs.getString("comments"));
                            user.setPhone(rs.getString("phone"));
                        }

                        if (user.isActive()) {
                            authenticateSucceed = true;

                            sql = "SELECT * FROM roles WHERE role_id IN " +
                                    "(SELECT role_id FROM user_roles " +
                                    "WHERE user_id = " + user.getUserId() + ")";
                            try (ResultSet rsRoles = statement.executeQuery(sql)) {
                                while (rsRoles.next()) {
                                    user.addRole(Roles.valueOf(rsRoles.getString("name")));
                                }
                            }

                            sql = "SELECT * FROM activities WHERE activity_id IN " +
                                    "(SELECT activity_id FROM role_activities " +
                                    "WHERE role_id IN (SELECT role_id FROM user_roles WHERE " +
                                    "user_id = " + user.getUserId() + "))";
                            try (ResultSet rsActivities = statement.executeQuery(sql)) {
                                while (rsActivities.next()) {
                                    user.addPermission(Activities.valueOf(rsActivities.getString("name")));
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return authenticateSucceed;
    }

    public static ArrayList<User> searchUsers(DataManager dataManager, String keyword) {
        ArrayList<User> users = new ArrayList<>();
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM users";
                if (keyword != null && !keyword.trim().equals("")) {
                    sql += " WHERE user_id LIKE '%" + keyword.trim() + "%'" +
                            " OR login LIKE '%" + keyword.trim() + "%'" +
                            " OR name LIKE '%" + keyword.trim() + "%'" +
                            " OR comments LIKE '%" + keyword.trim() + "%'";
                }
                try (ResultSet rs = statement.executeQuery(sql)) {
                    while (rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setLogin(rs.getString("login"));
                        user.setName(rs.getString("name"));
                        user.setAddress(rs.getString("address"));
                        user.setPhone(rs.getString("phone"));
                        user.setActive(rs.getBoolean("active"));
                        user.setComments(rs.getString("comments"));

                        users.add(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return users;
    }

    public static User getUserById(DataManager dataManager, int userId) {
        User user = null;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM users WHERE user_id = " + userId;
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setLogin(rs.getString("login"));
                        user.setName(rs.getString("name"));
                        user.setAddress(rs.getString("address"));
                        user.setPhone(rs.getString("phone"));
                        user.setActive(rs.getBoolean("active"));
                        user.setComments(rs.getString("comments"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return user;
    }

    public static User getUserByLogin(DataManager dataManager, String login) {
        User user = null;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM users WHERE login = '" + login + "'";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setLogin(rs.getString("login"));
                        user.setName(rs.getString("name"));
                        user.setAddress(rs.getString("address"));
                        user.setPhone(rs.getString("phone"));
                        user.setActive(rs.getBoolean("active"));
                        user.setComments(rs.getString("comments"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return user;
    }

    public static boolean updateUserData(DataManager dataManager, int userId, User newUserData, InputStream avatar) {
        boolean updateSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            // TODO: sql
            String sql = "UPDATE library.users" +
                    " SET login = ?, password= ?, name= ?, phone= ?," +
                    " address= ?, comments= ?, active= ?" +
                    ", avatar = ?" + //((avatar == null) ? "" : "avatar = ?") +
                    " WHERE user_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUserData.getLogin());
                preparedStatement.setString(2, newUserData.getPassword());
                preparedStatement.setString(3, newUserData.getName());
                preparedStatement.setString(4, newUserData.getPhone());
                preparedStatement.setString(5, newUserData.getAddress());
                preparedStatement.setString(6, newUserData.getComments());
                preparedStatement.setBoolean(7, newUserData.isActive());
                //if (avatar != null) {
                    preparedStatement.setBlob(8, avatar);
                //}
                preparedStatement.setInt(9, userId);
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

//    public static boolean updateUserData(DataManager dataManager, int userId, User newUserData, InputStream avatar) {
//        boolean updateSucceed = false;
//        Connection connection = dataManager.getConnection();
//        if (connection != null) {
//            try (Statement statement = connection.createStatement()) {
//                // TODO: sql
//                String sql = "UPDATE `library`.`users`" +
//                        " SET `login`= '" + newUserData.getLogin() +
//                        "', `password`= '" + newUserData.getPassword() +
//                        "', `name`= '" + newUserData.getName() +
//                        "', `phone`= '" + newUserData.getPhone() +
//                        "', `address`= '" + newUserData.getAddress() +
//                        "', `comments`= '" + newUserData.getComments() +
//                        "', `active`= " + newUserData.isActive() +
//                        " WHERE `user_id`= " + userId;
//                // TODO: transaction ? preparedStatement?
//                if (statement.executeUpdate(sql) > 0) {
//                    updateSucceed = true;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            dataManager.putConnection(connection);
//        }
//        return updateSucceed;
//    }

    public static void getUserRoles(DataManager dataManager, User user) {
        if (user != null) {
            Connection connection = dataManager.getConnection();
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    String sql = "SELECT * FROM roles WHERE role_id IN " +
                            "(SELECT role_id FROM user_roles " +
                            "WHERE user_id = " + user.getUserId() + ")";
                    try (ResultSet rsRoles = statement.executeQuery(sql)) {
                        while (rsRoles.next()) {
                            user.addRole(Roles.valueOf(rsRoles.getString("name")));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dataManager.putConnection(connection);
            }
        }
    }

    public static boolean applyUserRoles(DataManager dataManager, int userId, User user) {
        boolean updateSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                // TODO: sql
                String sql = "delete from user_roles where user_id = " + userId + "";
                for (Roles role : user.getRoles()) {
                    sql += "; insert into user_roles (user_id, role_id) values (" + userId +
                            ", (select role_id from roles where name = '" + role + "'))";
                }
                // TODO: transaction ? preparedStatement?
                statement.executeUpdate(sql);
                updateSucceed = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return updateSucceed;
    }

    public static boolean addNewUser(DataManager dataManager, User newUserData, InputStream avatar) {
        boolean addUserSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            String sql = "insert into `library`.`users`" +
                    " (login, password, name, phone, address, comments, active" +
                    ((avatar == null)?")":", avatar)") +
                    " values (?, ?, ?, ?, ?, ?, ?" +
                    ((avatar == null)?")":", ?)");
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // TODO: sql
                preparedStatement.setString(1, newUserData.getLogin());
                preparedStatement.setString(2, newUserData.getPassword());
                preparedStatement.setString(3, newUserData.getName());
                preparedStatement.setString(4, newUserData.getPhone());
                preparedStatement.setString(5, newUserData.getAddress());
                preparedStatement.setString(6, newUserData.getComments());
                preparedStatement.setBoolean(7, newUserData.isActive());
                if (avatar != null) {
                    preparedStatement.setBlob(8, avatar);
                }
                // TODO: transaction ? preparedStatement?
                if (preparedStatement.executeUpdate() > 0) {
                    addUserSucceed = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return addUserSucceed;
    }

//    public static boolean addNewUser(DataManager dataManager, User newUserData, InputStream avatar) {
//        boolean addUserSucceed = false;
//        Connection connection = dataManager.getConnection();
//        if (connection != null) {
//            try (Statement statement = connection.createStatement()) {
//                String sql = "insert into `library`.`users`" +
//                        " (login, password, name, phone, address, comments, active)" +
//                        " values ('" + newUserData.getLogin() + "', '"
//                        + newUserData.getPassword() + "', '"
//                        + newUserData.getName() + "', '"
//                        + newUserData.getPhone() + "', '"
//                        + newUserData.getAddress() + "', '"
//                        + newUserData.getComments() + "', "
//                        + newUserData.isActive() + ")";
////                if (avatar != null) {
////                    sql +=
////                }
//                if (statement.executeUpdate(sql) > 0) {
//                    addUserSucceed = true;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            dataManager.putConnection(connection);
//        }
//        return addUserSucceed;
//    }

    public static boolean deleteUser(DataManager dataManager, User user) {
        boolean deleteSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try {
                // TODO: уровни изолированности и др.
                connection.setAutoCommit(false);
                try (Statement statement = connection.createStatement()) {
                    // TODO: sql check borrowed books
                    String sql = "SELECT user_id FROM borrowed_books WHERE user_id = " +
                            user.getUserId() + "";
                    try (ResultSet rs = statement.executeQuery(sql)) {
                        if (rs.next()) {
                        } else {
                            // TODO: sql delete user roles
                            sql = "DELETE FROM user_roles WHERE user_id = " +
                                    user.getUserId();
                            statement.executeUpdate(sql);
                            // TODO: sql delete user
                            sql = "DELETE FROM users WHERE user_id = " +
                                    user.getUserId();
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

    public static InputStream getAvatarByUserId(DataManager dataManager, int userId) {
        InputStream avatar = null;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT avatar FROM users WHERE user_id = " + userId;
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        avatar = rs.getBinaryStream("avatar");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return avatar;
    }

    public static boolean setAvatarByUserId(DataManager dataManager, int userId, InputStream avatar) {
        boolean setAvatarSucceed = false;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            // TODO: sql
            String sql = "UPDATE library.users" +
                    " SET avatar = ? WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setBlob(1, avatar);
                preparedStatement.setInt(2, userId);
                if (preparedStatement.executeUpdate() > 0) {
                    setAvatarSucceed = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return setAvatarSucceed;
    }

//    public static void getPermissionsForCheckedUser(ResultSet rs, User user) {
//        String
//    }
}
