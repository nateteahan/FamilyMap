package request;

/**
 * Send appropriate data to the LoginService class
 */
public class LoginRequest {
    private String userName;
    private String password;

    /**

     * Constructor for LoginRequest
     *
     * @param userName  name of the user who wishes to login
     * @param password  secret password of the current user
     */
    public LoginRequest(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public boolean isValidLogin(LoginRequest l) {
        if (l.getUserName().equals("") || l.getPassword().equals("")) {
            return false;
        }
        return true;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
