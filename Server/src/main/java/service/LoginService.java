package service;

import java.util.UUID;

import dataAccess.AuthTokenDAO;
import dataAccess.DatabaseException;
import dataAccess.UserDAO;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import response.LoginResponse;

/**
 * Service class for Login. Performs one request and returns an LoginResponse object.
 */
public class LoginService {

    /**
     * Logs in the user and returns an auth token.
     * @param request LoginRequest object
     * @return  LoginResponse object
     */
    public LoginResponse login(LoginRequest request) throws DatabaseException {
        User loginUser;
        UserDAO userLoginDAO = new UserDAO();

        //Get the name and password from the login request object
        String requestedName = request.getUserName();
        String requestedPassword = request.getPassword();

        //Get the correct user out of the database
        loginUser = userLoginDAO.getUser(requestedName);
        LoginResponse responseObject = new LoginResponse();

        //Missing data in the database
        if (!request.isValidLogin(request)) {
            responseObject.setAuthToken(null);
            responseObject.setPersonID(null);
            responseObject.setUserName(null);
            responseObject.setOutputMessage("Missing info in the JSON");
        }
        else if (loginUser != null) {
            if (requestedName.equals(loginUser.getUsername()) && requestedPassword.equals(loginUser.getPassword())) {                       // If the user exists, check their password as well.
                //Generate a new authToken for the user
                String token = UUID.randomUUID().toString();
                responseObject.setAuthToken(token);
                responseObject.setUserName(loginUser.getUsername());
                responseObject.setPersonID(loginUser.getPersonID());
                responseObject.setOutputMessage(null);

                //Insert a new authToken for the given user into the authToken table
                AuthTokenDAO authDao = new AuthTokenDAO();
                AuthToken authTok = new AuthToken(loginUser.getUsername(), token);
                authDao.generateAuthToken(authTok);
            }
            else {
                responseObject.setAuthToken(null);
                responseObject.setPersonID(null);
                responseObject.setUserName(null);
                responseObject.setOutputMessage("Incorrect password");
            }
        }
        // User info did not match anybody in the database.
        else {
            responseObject.setAuthToken(null);
            responseObject.setPersonID(null);
            responseObject.setUserName(null);
            responseObject.setOutputMessage("Invalid login attempt. Register the user first.");
        }
        return responseObject;
    }
}
