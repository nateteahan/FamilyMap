package response;

/**
 * Getters and Setters for:
 * authTokenResponse    non-empty string
 * userNameResponse     non-empty string of a user
 * personIDResponse     non-empty string of a unique ID
 * outputMessage   string to communicate to user the result of the response
 */
public class LoginResponse {

    private String authToken;
    private String userName;
    private String personID;
    private String outputMessage;


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }
}
