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
import model.Event;
import model.Person;
import model.User;
import response.ClearResponse;

import static org.junit.Assert.*;

public class ClearServiceTest {
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
    public void clear() throws DatabaseException {
        boolean tablesAreEmpty = false;

        //Initial check to make sure all tables are empty upon creation
        if (eventDAO.checkForEmptyEventTable() && userDAO.checkForEmptyUserTable()
                && personDAO.checkForEmptyPersonTable() && authTokenDAO.checkForEmptyAuthTokenTable()) {
            tablesAreEmpty = true;
        }

        assertTrue(tablesAreEmpty);

        //Insert data into Database
        AuthToken authToken = new AuthToken("Nate", "123456-hfgdsdf");
        authTokenDAO.generateAuthToken(authToken);

        Event eventOne = new Event("23456", "Nate", "123-789", 45.667, 424.24,"United States", "Salt Lake City",
                "marriage", 1980);
        eventDAO.createEvent(eventOne);

        Person personOne = new Person("789-456", "cayla", "james", "Teahan", "m", "123-789", "456-789", null);
        personDAO.createPerson(personOne);

        User userOne = new User("Nate", "myPassword", "nate@gmail.com", "nate", "teahan", "m", "123-789");
        userDAO.createUser(userOne);

        //Make sure the database has data in it now
        if (!eventDAO.checkForEmptyEventTable() && !userDAO.checkForEmptyUserTable()
                && !personDAO.checkForEmptyPersonTable() && !authTokenDAO.checkForEmptyAuthTokenTable()) {
            tablesAreEmpty = false;
        }

        assertFalse(tablesAreEmpty);

        ClearService clearService = new ClearService();
        ClearResponse clearResponse = clearService.clear();

        //Clear service checks for the empty database
        assertEquals("Clear succeeded.", clearResponse.getOutputMessage());


    }
}