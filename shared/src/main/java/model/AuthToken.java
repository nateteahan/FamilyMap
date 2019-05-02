package model;

/**
 * Contain the appropriate data to generate an authentication token for every user who logs in
 *
 * userName: Unique identifier for this person (non-empty string)
 * authToken: authorization token for a user
 */
public class AuthToken {
    private String username;
    private String authToken;

    /**
     * Default constructor for AuthToken, in the case that no parameters are given
     */
    public AuthToken() {};

    /**
     * Constructor for AuthToken
     * @param username      user to which the authentication token belongs to
     * @param authToken     unique string that is generated upon login and give to a user
     */
    public AuthToken(String username, String authToken) {
        this.setUsername(username);
        this.setAuthToken(authToken);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
