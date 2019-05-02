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
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.LoadResponse;

import static org.junit.Assert.*;

public class LoadServiceTest {
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
    public void load() throws DatabaseException {
        User[] userArray = new User[2];
        Person[] personArray = new Person[1];
        Event[] eventArray = new Event[3];

        //Create userArray
        User userOne = new User("nate", "myPassword", "nate@gmail.com", "nate", "teahan", "m", "123-789");
        User userTwo = new User("cayla", "letsDate", "cayla@gmail.com", "cayla", "robinson", "f", "456-789");
        userArray[0] = userOne;
        userArray[1] = userTwo;

        //Create personArrray
        Person personOne = new Person("789-456", "cayla", "james", "Teahan", "m", "123-789", "456-789", null);
        personArray[0] = personOne;

        //Create eventArray
        Event eventOne = new Event("23456", "nate", "123-456", 45.667, 424.24,"United States", "Salt Lake City",
                "marriage", 1980);
        Event eventTwo = new Event("78943", "cayla", "456-789", 45.667, 424.24,"United States", "Salt Lake City",
                "marriage", 1980);
        Event eventThree = new Event("67483939", "james", "789-456", 465.98, 432.68,"United States", "Seattle",
                "birth", 1987);
        eventArray[0] = eventOne;
        eventArray[1] = eventTwo;
        eventArray[2] = eventThree;

        //Do the actual loading into the database
        LoadService loadService = new LoadService();
        LoadRequest loadRequest = new LoadRequest(userArray, personArray, eventArray);
        LoadResponse loadResponse = loadService.load(loadRequest);

        assertEquals("Successfully added 2 users, 1 persons, and 3 events to the database", loadResponse.getOutputMessage());


    }
}