package service;

import java.util.UUID;

import dataAccess.AuthTokenDAO;
import dataAccess.DatabaseException;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import model.Person;
import model.User;
import request.RegisterRequest;
import response.FillResponse;
import response.RegisterResponse;

/**
 * Service class for Register. Performs one request and returns an RegisterResponse object.
 */
public class RegisterService {

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     user, logs the user in, and returns an auth token.
     * @param request RegisterRequest object
     * @return     RegisterResponse object
     */
    public RegisterResponse register(RegisterRequest request) throws DatabaseException {
        User registerUser;
        UserDAO userRegisterDAO = new UserDAO();
        RegisterResponse responseObject = new RegisterResponse();

        // Get the name from the request object to see if it already exists in the database or not.
        String userName = request.getUsername();

        // Check to see if the username already exists in database
        registerUser = userRegisterDAO.getUser(userName);

        //Check for missing data
        if (!request.isValidRequest(request)) {
            responseObject.setAuthToken(null);
            responseObject.setUserName(null);
            responseObject.setPersonID(null);
            responseObject.setMessage("Missing data in the JSON");
        }

        else if (!request.checkValidGender(request)) {
            responseObject.setAuthToken(null);
            responseObject.setUserName(null);
            responseObject.setPersonID(null);
            responseObject.setMessage("Gender must be either m or f");
        }
        else if (registerUser != null) {
            responseObject.setAuthToken(null);
            responseObject.setUserName(null);
            responseObject.setPersonID(null);
            responseObject.setMessage("This username already exists. Please login with your password.");
        }
        else {
            //Generate a random personID and authToken for the user
            String ID = UUID.randomUUID().toString();
            String token = UUID.randomUUID().toString();                                            // How do I assign the authToken to belong to the person?

            /* Create an authToken and insert it into the database for the current username */
            AuthTokenDAO authTokenDAO = new AuthTokenDAO();

            responseObject.setAuthToken(token);
            responseObject.setUserName(userName);
            responseObject.setPersonID(ID);
            responseObject.setMessage(null);

            //Generate an authToken for the user and insert into the database
            AuthToken newToken = new AuthToken(userName, token);
            AuthTokenDAO authTokenDataAccess = new AuthTokenDAO();
            authTokenDataAccess.generateAuthToken(newToken);


            //Create the user based off the data in the request object. Place in database
            registerUser = new User(request.getUsername(), request.getPassword(), request.getEmail_address(),
                                    request.getFirstName(), request.getLastName(), request.getGender(), ID);
            userRegisterDAO.createUser(registerUser);

            //Insert the user into the person table as well
            Person registerPerson = new Person(ID, request.getUsername(), request.getFirstName(),
                                                request.getLastName(), request.getGender(), null, null, null);
            PersonDAO personDao = new PersonDAO();
            personDao.createPerson(registerPerson);

            //Fill service to register the user as a person and generate at least 4 generations.
            int defaultGenerations = 4;
            FillService fService = new FillService();
            FillResponse fillResponse = fService.fill(userName, defaultGenerations);
        }
        return responseObject;
    }
}
