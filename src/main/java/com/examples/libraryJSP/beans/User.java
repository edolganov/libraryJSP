package com.examples.libraryJSP.beans;

import com.examples.libraryJSP.beans.permissions.Activities;
import com.examples.libraryJSP.beans.permissions.Roles;

import java.util.*;

/**
 * Created by Аяз on 21.06.2016.
 */
public class User {

    private int userId;
    private String login = "";
    private String password = "";
    private boolean active = false;

    private String Name = "";
    private String Address = "";
    private String phone = "";
    private String comments = "";

    private TreeSet<Roles> roles = new TreeSet<>();
    private TreeSet<Activities> permissions = new TreeSet<>();


    // ------ Permissions check ------
    public void addRole(Roles role) {
        roles.add(role);
    }
    public void addPermission(Activities activity) {
        permissions.add(activity);
    }
    public boolean roleAllowed(Roles role) {
        return roles != null && roles.contains(role);
    }
    public boolean activityAllowed(Activities activity) {
        return permissions != null && permissions.contains(activity);
    }

    // ------ Setters ------
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // ------ Getters ------
    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return phone;
    }

    public String getComments() {
        return comments;
    }

    public TreeSet<Roles> getRoles() {
        return roles;
    }
}
