package com.examples.libraryJSP.controller;

import com.examples.libraryJSP.beans.Book;
import com.examples.libraryJSP.beans.User;
import com.examples.libraryJSP.beans.permissions.Roles;
import com.examples.libraryJSP.model.DataManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;

import static com.examples.libraryJSP.beans.encoding.Encoder.md5Custom;

/**
 * Created by Аяз on 30.06.2016.
 */
public class Utils {

    static int prepareUserId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("userId"));
    }

    static User prepareNewUserData(HttpServletRequest request) {
        User newUserData = new User();
        newUserData.setLogin(request.getParameter("login"));
        newUserData.setPassword(md5Custom(request.getParameter("pass")));
        newUserData.setName(request.getParameter("name"));
        newUserData.setPhone(request.getParameter("phone"));
        newUserData.setAddress(request.getParameter("address"));
        newUserData.setComments(request.getParameter("comments"));
        newUserData.setActive(Boolean.parseBoolean(request.getParameter("active")));
        return newUserData;
    }

    static User prepareUserRoles(HttpServletRequest request) {
        String userRoles = request.getParameter("userRoles");
        User user = new User();
        if (userRoles != null) {
            for (int i = 0; i < userRoles.length(); i++) {

            }
        }
        for (Roles role : Roles.values()) {
            if (request.getParameter(role.toString()) != null) {
                user.addRole(role);
            }
        }
        return user;
    }

    public static int newUserId(DataManager dataManager, HttpServletRequest request) {
        User user = dataManager.getUserByLogin(request.getParameter("login"));
        return user==null?0:user.getUserId();
    }

    public static Book PrepareBook(DataManager dataManager, HttpServletRequest request) {
        String bookId = request.getParameter("bookId");
        return (bookId==null)?null:dataManager.getBookById(bookId);
    }

    public static boolean processDeleteUser(DataManager dataManager, HttpServletRequest request) {
        boolean deleteSucceed = false;
        User user = dataManager.getUserById(prepareUserId(request));
        if (user != null && dataManager.numberUserBorrowedBooks(user) == 0) {
            deleteSucceed = dataManager.deleteUser(user);
        }
        return deleteSucceed;
    }

    public static InputStream prepareAvatar(HttpServletRequest request) {
        InputStream avatar = null;
        try {
            Part avatarPart = request.getPart("avatar");
            // String fileName = filePart.getSubmittedFileName();
            avatar = avatarPart.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return avatar;
    }

    public static InputStream extractAvatar(DataManager dataManager, int userId) {
        return dataManager.getAvatarByUserId(userId);
    }

    public static void provideAvatar(HttpServletRequest request) {
    }

    public static Book prepareNewBookData(DataManager dataManager, HttpServletRequest request) {

        Book newBookData = new Book();
        newBookData.setId(request.getParameter("id"));
        newBookData.setAuthor(request.getParameter("author"));
        newBookData.setTitle(request.getParameter("title"));
        if (request.getParameter("year") != "") {
            newBookData.setYear(Integer.parseInt(request.getParameter("year")));
        }
        newBookData.setAbout(request.getParameter("about"));// (request.getParameter("about"));
        if (dataManager.getCategoryName(request.getParameter("categoryId")) != null && dataManager.getCategoryName(request.getParameter("categoryId")) != "") {
            newBookData.setCategory_id(Integer.parseInt(request.getParameter("categoryId")));

        }
        newBookData.setActive(Boolean.parseBoolean(request.getParameter("active")));
        return newBookData;
    }

    public static InputStream prepareBookCover(HttpServletRequest request) {
        InputStream cover = null;
        try {
            Part coverPart = request.getPart("cover");
            // String fileName = filePart.getSubmittedFileName();
            cover = coverPart.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
            return cover;
    }

    public static byte[] prepareBookAbout(HttpServletRequest request) {
        byte[] about = null;
//        //about = new byte[request.getParameter("")]
//        try {
//            Part aboutPart = request.getPart("about");
//            // String fileName = filePart.getSubmittedFileName();
//            //about = aboutPart. aboutPart.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ServletException e) {
//            e.printStackTrace();
//        }
        return about;
    }

    public static InputStream extractCover(DataManager dataManager, String bookId) {
        return dataManager.getCoverByBookId(bookId);
    }

    public static boolean processDeleteBook(DataManager dataManager, HttpServletRequest request) {
            boolean deleteSucceed = false;
            Book book = dataManager.getBookById(prepareBookId(request));
            if (book != null && dataManager.bookBorrowed(book) == false) {
                deleteSucceed = dataManager.deleteBook(book);
            }
            return deleteSucceed;
    }

    static String prepareBookId(HttpServletRequest request) {
        return request.getParameter("bookId");
    }
}
