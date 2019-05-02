package service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import dataAccess.AuthTokenDAO;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import model.Person;
import model.User;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.Assert.*;

public class RegisterServiceTest {
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
    public void register() throws DatabaseException {
        userDAO.createUserTable();
        personDAO.createPersonTable();

        //Successful register request
        RegisterRequest registerRequest = new RegisterRequest("nateteahan", "myPassword", "nate@gmail.com",
                "nate", "teahan", "m");
        RegisterService service = new RegisterService();
        RegisterResponse registerResponse = service.register(registerRequest);
        User userToRegister = new User("nateteahan", "myPassword", "nate@gmail.com", "nate", "teahan", "m", "123-456");

        assertEquals(userToRegister.getUsername(), registerResponse.getUserName());

        //Test for missing data in the request object (REGISTER FAIL/ERROR TEST)
        RegisterRequest missingDataRequest = new RegisterRequest("", "myPassword", "nate@gmail.com",
                "nate", "teahan", "m");
        RegisterResponse missingDataResponse = service.register(missingDataRequest);

        assertEquals("Missing data in the JSON", missingDataResponse.getMessage());

        //Unsuccesful register attempt because gender field was not an 'm' or 'f'
        RegisterRequest genderFailRequest = new RegisterRequest("nateteahan", "myPassword", "nate@gmail.com",
                "nate", "teahan", "m/f");
        RegisterResponse genderFailResponse = service.register(genderFailRequest);

        assertEquals("Gender must be either m or f", genderFailResponse.getMessage());

        //Unsuccessful register because user has already registered. failedRegister's message will be equal to error output message
        RegisterResponse failedRegister = service.register(registerRequest);
        assertEquals("This username already exists. Please login with your password.", failedRegister.getMessage());

        //Creates 4 generations of persons when a user registers. We have one successful register attempt, so the number of persons in the database
        //should be equal to 31
        ArrayList<Person> numPeopleInDatabase = personDAO.getAllPeople("nateteahan");
        assertEquals(31, numPeopleInDatabase.size());
    }
}