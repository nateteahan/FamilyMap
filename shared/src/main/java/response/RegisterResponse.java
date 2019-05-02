package response;

/**
 * Getters and Setters for:
 * authToken    non-empty string
 * userName     non-empty string of a user
 * personID     non-empty string of a unique ID
 * message   string to communicate to user the result of the response
 */
public class RegisterResponse {
    private String authToken;
    private String userName;
    private String personID;
    private String message;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;

    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }
}
