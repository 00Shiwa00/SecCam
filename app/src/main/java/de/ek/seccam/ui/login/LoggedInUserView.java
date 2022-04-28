package de.ek.seccam.ui.login;

import de.ek.seccam.data.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }
    LoggedInUser user;
    LoggedInUserView(LoggedInUser user) {
        displayName=user.getDisplayName();
        this.user = user;
    }
    LoggedInUser getDisplayLoggedInUser() {
        return user;
    }
    String getDisplayName() {
        return displayName;
    }
}