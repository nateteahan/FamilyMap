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
import model.Person;
import request.RegisterRequest;
import response.PersonResponse;
import response.RegisterResponse;

import static org.junit.Assert.*;

public class PersonServiceTest {
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
    public void person() throws DatabaseException {
        //Register some user--creating a user and a person in the database as a result
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest("nateteahan", "myPassword", "nate@gmail.com",
                "nate", "teahan", "m");
        RegisterResponse registerResponse = registerService.register(registerRequest);

        PersonService personService = new PersonService();
        PersonResponse personResponse = personService.person(registerResponse.getPersonID(), registerResponse.getAuthToken());
        //Get the person that was created as a result of the register service, see if it is equal to the personResponse object when give the personID
        Person personObj = personDAO.getPerson(registerResponse.getPersonID());

        assertEquals(personObj.getDescendant(), personResponse.getPerson().getDescendant());
    }
}