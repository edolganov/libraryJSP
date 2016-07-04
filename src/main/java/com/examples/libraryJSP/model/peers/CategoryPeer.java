package com.examples.libraryJSP.model.peers;

import com.examples.libraryJSP.beans.Category;
import com.examples.libraryJSP.model.DataManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Аяз on 21.06.2016.
 */
public class CategoryPeer {

    public static HashMap<String, String> getAllCategories(DataManager dataManager) {
        HashMap<String, String> categories = new HashMap<>();
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM categories";
                try (ResultSet rs = statement.executeQuery(sql)) {
                    while (rs.next()) {
                        categories.put(rs.getString("category_id"), rs.getString("name"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return categories;
    }

    public static Category getCategoryById(DataManager dataManager, String categoryId) {
        Category category = null;
        Connection connection = dataManager.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM categories WHERE category_id = " + categoryId;
                try (ResultSet rs = statement.executeQuery(sql)) {
                    if (rs.next()) {
                        category = new Category(rs.getInt("category_id"), rs.getString("name"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dataManager.putConnection(connection);
        }
        return category;
    }
}