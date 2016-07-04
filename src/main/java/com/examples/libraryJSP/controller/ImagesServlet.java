package com.examples.libraryJSP.controller;

import com.examples.libraryJSP.model.DataManager;
import sun.misc.IOUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Аяз on 03.07.2016.
 */
public class ImagesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        String pathInfo = request.getPathInfo();
        pathInfo = pathInfo.substring(pathInfo.indexOf('/') + 1, pathInfo.length());
        int mark = pathInfo.indexOf('/');
        if (mark > -1) {
            String pictureName = pathInfo.substring(0, mark);
            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;
            InputStream is;
            switch (pictureName) {
                case "avatar":
                    int userId = Integer.parseInt(pathInfo.substring(mark + 1));
                    is = Utils.extractAvatar(dataManager, userId);
                    if (is != null) {
                        while ((bytesRead = is.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                    }
                    break;
                case "cover":
                    String bookId = pathInfo.substring(mark + 1);
                    is = Utils.extractCover(dataManager, bookId);
                    if (is != null) {
                        while ((bytesRead = is.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

//String imageName = request.getPathInfo().substring(1); // Returns "foo.png"
//        response.setContentType(getServletContext().getMimeType(imageName));
//        response.setContentLength(content.length);
//        response.getOutputStream().write(content);
//response.setContentType(getServletContext().getMimeType(imageName));
//String imageName = request.getPathInfo().substring(2);