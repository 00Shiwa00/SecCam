package de.ek.seccam.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String password;

    public LoggedInUser(String userId, String displayName, String password) {
        this.userId = userId;
        this.displayName = displayName;
        this.password = password;
    }
    public String getpassword() {return password; }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}