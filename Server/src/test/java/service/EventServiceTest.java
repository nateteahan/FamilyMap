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
import request.LoadRequest;
import response.EventResponse;

import static org.junit.Assert.*;

public class EventServiceTest {
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
    public void event() throws DatabaseException {
        User[] userArray = new User[2];
        Person[] personArray = new Person[1];
        Event[] eventArray = new Event[3];

        //Create userArray
        User userOne = new User("nate", "myPassword", "nate@gmail.com", "nate", "teahan", "m", "123-789");
        User userTwo = new User("cayla", "letsDate", "cayla@gmail.com", "cayla", "robinson", "f", "456-789");
        userArray[0] = userOne;
        userArray[1] = userTwo;

        //Create personArrray
        Person personOne = new Person("789-456", "cayla", "james", "teahan", "m", "123-789", "456-789", null);
        personArray[0] = personOne;

        //Create eventArray
        Event eventOne = new Event("23456", "nate", "123-456", 45.667, 424.24,"United States", "Salt Lake City",
                "marriage", 1980);
        Event eventTwo = new Event("78943", "cayla", "456-789", 45.667, 424.24,"United States", "Salt Lake City",
                "marriage", 1980);
        Event eventThree = new Event("67483939", "nate", "123-456", 465.98, 432.68,"United States", "Seattle",
                "birth", 1957);
        eventArray[0] = eventOne;
        eventArray[1] = eventTwo;
        eventArray[2] = eventThree;

        //Do the actual loading into the database
        LoadService loadService = new LoadService();
        LoadRequest loadRequest = new LoadRequest(userArray, personArray, eventArray);
        loadService.load(loadRequest);

        //Check events. Assume server passed in eventID of "nate" (i.e. 23456) and his authToken
        String eventID = "23456";
        AuthToken authToken = authTokenDAO.retrieveAuthToken("nate");

        EventService eventService = new EventService();
        EventResponse eventResponse = eventService.event(eventID, authToken.getAuthToken());

        assertEquals(eventOne, eventResponse.getEvent());

        //Now see if the whole array of events associated with nate returns when not passed in an eventID
        eventResponse = eventService.event("", authToken.getAuthToken());

        assertEquals(eventOne, eventResponse.getData()[0]);
        assertEquals(eventThree, eventResponse.getData()[1]);

    }
}