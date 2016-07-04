package com.examples.libraryJSP.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import com.examples.libraryJSP.beans.Book;
import com.examples.libraryJSP.beans.User;
import com.examples.libraryJSP.beans.permissions.Activities;
import com.examples.libraryJSP.model.DataManager;
import com.examples.libraryJSP.model.SessionDataManager;

import com.examples.libraryJSP.beans.encoding.Encoder;

/**
 * Created by Аяз on 21.06.2016.
 */
@javax.servlet.annotation.MultipartConfig
public class LibraryServlet extends HttpServlet {
    private String viewBase;
    private final String httpMethod = "post"; // "post" "get"

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("*** initializing controller servlet.");
        super.init(config);

        DataManager dataManager = new DataManager();
        // dataManager.setDbURL(config.getInitParameter("dbURL")); // use DbURL from dataManager
        dataManager.setDbUserName(config.getInitParameter("dbUserName"));
        dataManager.setDbPassword(config.getInitParameter("dbPassword"));

        ServletContext context = config.getServletContext();
        context.setAttribute("base", config.getInitParameter("viewBase"));
        context.setAttribute("imageURL", config.getInitParameter("imageURL"));
        context.setAttribute("dataManager", dataManager);
        context.setAttribute("httpMethod", httpMethod);

        try {  // load the database JDBC driver
            Class.forName(config.getInitParameter("jdbcDriver"));
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        viewBase = config.getInitParameter("viewBase");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    // TODO: forward URL
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        String action = request.getParameter("action");
        String url; // = viewBase + "g_Index.jsp";

        HttpSession session = request.getSession(false);
        SessionDataManager sDataManager = null;
        User currentUser = null;
        if (session != null) {
            //currentUser = (User) session.getAttribute("currentSessionUser");
            sDataManager = (SessionDataManager) session.getAttribute("sessionDataManager");
            if (sDataManager != null) {
                currentUser = sDataManager.getCurrentSessionUser();
            }
        }

        if (currentUser == null) {
            url = viewBase + "g_Login.jsp";
            String login = request.getParameter("login");
            String pass = request.getParameter("pass");
            if (action != null && login != null
                    && pass != null && login != "") {
                switch (action) {
                    case "login":
                        currentUser = new User();
                        if (dataManager.loginUser(login, Encoder.md5Apache(pass), currentUser)) {
                            url = viewBase + "g_Index.jsp";
                            if (session == null) {
                                session = request.getSession(true);
                            }
                            sDataManager = new SessionDataManager();
                            sDataManager.setCurrentSessionUser(currentUser);
                            session.setAttribute("currentSessionUser", currentUser);
                            session.setAttribute("sessionDataManager", sDataManager);
                        }
                        break;
                    default:
                        break;
                }
            }

        } else {
            url = viewBase + "g_Index.jsp";
            if (action != null && currentUser != null) {
                switch (action) {
                    case "sign":
                        currentUser = null;
                        session.setAttribute("currentSessionUser", currentUser);
                        sDataManager = null;
                        session.setAttribute("sessionDataManager", sDataManager);
                        url = viewBase + "g_Login.jsp";
                        break;
                    case "searchUsers":
                        if (currentUser.activityAllowed(Activities.USERS_SERVE)
                                || currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            url = viewBase + "c_SearchUsers.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "userDetails":
                        if (currentUser.activityAllowed(Activities.USERS_SERVE)
                                || currentUser.activityAllowed(Activities.USERS_MANAGE)
                                || (currentUser.getUserId() == ((request.getParameter("userId") == null) ? -1 : Integer.parseInt(request.getParameter("userId"))))) {
                            // { //currentUser.activityAllowed(Activities.USERS_SERVE)
                            //|| currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            url = viewBase + "c_UserDetails.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "handleUser":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)
                                || (currentUser.getUserId() == ((request.getParameter("userId") == null) ? -1 : Integer.parseInt(request.getParameter("userId"))))) {
                            url = viewBase + "c_HandleUser.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "userRoles":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            url = viewBase + "c_UserRoles.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "updateUserData":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)
                                || (currentUser.getUserId() == ((request.getParameter("userId") == null) ? -1 : Integer.parseInt(request.getParameter("userId"))))) {
                            if (dataManager.updateUserData(Utils.prepareUserId(request), Utils.prepareNewUserData(request), Utils.prepareAvatar(request))) {
                                sDataManager.setOperationErrorMessage("User data updated");
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("No currentUser data updated");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "applyUserRoles":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            if (dataManager.applyUserRoles(Utils.prepareUserId(request), Utils.prepareUserRoles(request))) {
                                sDataManager.setOperationErrorMessage("User roles applied");
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("No user roles applied");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "newUser":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            url = viewBase + "c_AddUser.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "addUser":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            if (dataManager.addNewUser(Utils.prepareNewUserData(request), Utils.prepareAvatar(request))) {
                                int userId = Utils.newUserId(dataManager, request);
                                sDataManager.setOperationErrorMessage("User added. UserID = " + userId);
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("User not added");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "startServe":
                        if (currentUser.activityAllowed(Activities.USERS_SERVE)) {
                            sDataManager.setServeUser(dataManager.getUserById(Utils.prepareUserId(request)));
                            url = viewBase + "c_UserDetails.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "endServe":
                        if (currentUser.activityAllowed(Activities.USERS_SERVE)) {
                            sDataManager.setServeUser(null);
                            url = viewBase + "c_UserDetails.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "deleteUser":
                        if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
                            if (Utils.processDeleteUser(dataManager, request)) {
                                sDataManager.setOperationErrorMessage("User deleted");
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("User is not deleted");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "searchBooks":
                        if (currentUser.activityAllowed(Activities.BOOKS_READ)) {
                            url = viewBase + "c_SearchBooks.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "selectCatalog":
                        if (currentUser.activityAllowed(Activities.BOOKS_READ)) {
                            url = viewBase + "c_SelectCatalog.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "bookDetails":
                        if (currentUser.activityAllowed(Activities.BOOKS_READ)) {
                            url = viewBase + "c_BookDetails.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "issueReturnBook": // TODO: change forward url
                        if (currentUser.activityAllowed(Activities.BOOKS_READ)) {
                            Book book = Utils.PrepareBook(dataManager, request);
                            if (dataManager.bookAccessForIssue(book)) {
                                if (dataManager.issueBook(book, sDataManager.getServeUser(), sDataManager.getCurrentSessionUser(), true)) {
                                    sDataManager.setOperationErrorMessage("Book issued");
                                    url = viewBase + "g_ErrorPage.jsp";
                                } else {
                                    sDataManager.setOperationErrorMessage("Book not issued");
                                    url = viewBase + "g_ErrorPage.jsp";
                                }
                            } else {
                                if (dataManager.bookBorrowed(book)) {
                                    if (dataManager.issueBook(book, sDataManager.getServeUser(), sDataManager.getCurrentSessionUser(), false)) {
                                        sDataManager.setOperationErrorMessage("Book returned");
                                        url = viewBase + "g_ErrorPage.jsp";
                                    } else {
                                        sDataManager.setOperationErrorMessage("Book not returned");
                                        url = viewBase + "g_ErrorPage.jsp";
                                    }
                                }
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "reportBorrowedBooks":
                        if (currentUser.activityAllowed(Activities.REPORTS)) {
                            url = viewBase + "r_BorrowedBooks.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "reportExpiredBooks":
                        if (currentUser.activityAllowed(Activities.REPORTS)) {
                            url = viewBase + "r_ExpiredBooks.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "newBook":
                        if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {
                            url = viewBase + "c_AddBook.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "addBook":
                        if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {
                            if (dataManager.addNewBook(Utils.prepareNewBookData(dataManager, request),
                                    Utils.prepareBookAbout(request), Utils.prepareBookCover(request))) {
                                sDataManager.setOperationErrorMessage("Book added");
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("Book not added");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "handleBook":
                        if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {
                            url = viewBase + "c_HandleBook.jsp";
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "deleteBook":
                        if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {
                            if (Utils.processDeleteBook(dataManager, request)) {
                                sDataManager.setOperationErrorMessage("Book deleted");
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("Book is not deleted");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    case "updateBookData":
                        if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {
                            if (dataManager.updateBookData(Utils.prepareBookId(request), Utils.prepareNewBookData(dataManager, request), Utils.prepareBookCover(request))) {
                                sDataManager.setOperationErrorMessage("Book data updated");
                                url = viewBase + "g_ErrorPage.jsp";
                            } else {
                                sDataManager.setOperationErrorMessage("No book data updated");
                                url = viewBase + "g_ErrorPage.jsp";
                            }
                        } else {
                            url = viewBase + "g_MissingPermissions.jsp";
                        }
                        break;
                    default:
                        url = viewBase + "g_PageNotFound.jsp";
                        break;
                }
            }
        }

        RequestDispatcher requestDispatcher =
                getServletContext().getRequestDispatcher(url);
        requestDispatcher.forward(request, response);
    }
}
