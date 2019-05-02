package service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.AuthTokenDAO;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;

import static org.junit.Assert.*;

public class LoginServiceTest {
    Database db = new Database();
    UserDAO userDAO = new UserDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    AuthTokenDAO authTokenDAO = new AuthTokenDAO();

    @Before
    public void setUp() throws Exception {
        //Create tables for database
        userDAO.createUserTable();
        eventDAO.createEventTable();
        personDAO.createPersonTable();
        authTokenDAO.createAuthTokenTable();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void login() throws DatabaseException {
        //Try to log a user in when they have not yet registered. Get error message
        LoginService loginService = new LoginService();
        LoginRequest loginRequest = new LoginRequest("nate", "myPassword");
        LoginResponse loginResponse = loginService.login(loginRequest);

        assertEquals("Invalid login attempt. Register the user first.", loginResponse.getOutputMessage());

        //Register the user, then attempt to login again. Return the same personID as the one generated upon registration
        RegisterRequest registerRequest = new RegisterRequest("nateteahan", "myPassword", "nate@gmail.com",
                "nate", "teahan", "m");
        RegisterService service = new RegisterService();
        RegisterResponse registerResponse = service.register(registerRequest);

        loginRequest.setUserName("nateteahan");
        loginRequest.setPassword("myPassword");
        loginResponse = loginService.login(loginRequest);

        //Assure that some authentication token is generated. Then fetch it and make sure it indeed belongs to this person
        assertEquals(loginResponse.getPersonID(), registerResponse.getPersonID());
        assertNotNull(loginResponse.getAuthToken());

        AuthToken authToken = authTokenDAO.getUserToken(loginResponse.getAuthToken());

        //AuthToken generated from login belongs to the user that same user that registered
        assertEquals("nateteahan", authToken.getUsername());

        //Unsuccessful login w/ invalid password. Get failure/error output message
        loginRequest.setUserName("nateteahan");
        loginRequest.setPassword("myPassport");
        loginResponse = loginService.login(loginRequest);

        assertEquals("Incorrect password", loginResponse.getOutputMessage());

        //Unsuccessful login w/ missing JSON info. Get failure/error output message
        loginRequest.setUserName("nateteahan");
        loginRequest.setPassword("");
        loginResponse = loginService.login(loginRequest);

        assertEquals("Missing info in the JSON", loginResponse.getOutputMessage());
    }
}