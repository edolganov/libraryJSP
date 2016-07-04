package com.examples.libraryJSP.model;

import com.examples.libraryJSP.beans.User;

/**
 * Created by Аяз on 29.06.2016.
 */
public class SessionDataManager {
    private User serveUser = null;
    private User currentSessionUser = null;
    private String lastLocation;
    private boolean hack = false;

    private String operationErrorMessage;

    // ------ Setters ------
    public void setServeUser(User serveUser) {
        this.serveUser = serveUser;
    }

    public void setCurrentSessionUser(User currentSessionUser) {
        this.currentSessionUser = currentSessionUser;
    }

    public void setOperationErrorMessage(String operationErrorMessage) {
        this.operationErrorMessage = operationErrorMessage;
    }

    public void setHack(boolean hack) {
        this.hack = hack;
    }

    // ------ Getters ------
    public User getServeUser() {
        return serveUser;
    }

    public User getCurrentSessionUser() {
        return currentSessionUser;
    }

    public String getOperationErrorMessage() {
        return operationErrorMessage;
    }

    public boolean isHack() {
        return hack;
    }


}
