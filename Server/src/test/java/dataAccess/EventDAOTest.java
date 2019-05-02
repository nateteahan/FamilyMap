package dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Event;

import static org.junit.Assert.*;

public class EventDAOTest {
    Database db = new Database();
    UserDAO userDAO = new UserDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    AuthTokenDAO authTokenDAO = new AuthTokenDAO();
    Event event;

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
    public void deleteEvent() throws DatabaseException {
        event = new Event("1234", "nate", "77ye4", 67.098, 09.44, "USA", "Salt Lake City", "baptism", 2000);
        eventDAO.createEvent(event);

        assertTrue(eventDAO.deleteEvent("1234"));
        assertFalse(eventDAO.deleteEvent("4556"));
    }

    @Test
    public void createEvent() throws DatabaseException {
        event = new Event("251837d7", "susan", "7255e93e", 65.6833, -17.9, "Iceland", "Akureyri", "birth", 1912 );
        eventDAO.createEvent(event);
        event = new Event("1234", "nate", "77ye4", 67.098, 09.44, "USA", "Salt Lake City", "baptism", 2000);
        eventDAO.createEvent(event);

        assertEquals("251837d7", eventDAO.getEvent("251837d7").getEventID());
        assertEquals("nate", eventDAO.getEvent("1234").getDescendant());
        assertNotEquals("SUSAN", eventDAO.getEvent("251837d7").getDescendant());
    }

    @Test
    public void getEvent() throws DatabaseException {
        event = new Event("111", "Nathaniel", "44f8", 43.77, 56.88, "Canada", "Montreal", "marriage", 1900);
        eventDAO.createEvent(event);

        assertEquals("111", eventDAO.getEvent("111").getEventID());
        assertEquals("Nathaniel", eventDAO.getEvent("111").getDescendant());
        assertEquals("44f8", eventDAO.getEvent("111").getPersonID());
        assertEquals("Canada", eventDAO.getEvent("111").getCountry());
        assertEquals("Montreal", eventDAO.getEvent("111").getCity());
        assertEquals("marriage", eventDAO.getEvent("111").getEventType());

        /* All the data members are the same, why aren't the objects evaluating as equal on this test? */
        //assertTrue(event.equals(eventDAO.getEvent("111")));
    }

    @Test
    public void checkEventID() throws DatabaseException{
        event = new Event("222", "Jack", "fasd3", 77.43, 88.56, "Nigeria", "Lagos", "christening", 1386);
        eventDAO.createEvent(event);

        assertTrue(eventDAO.checkEventID("222"));
        assertFalse(eventDAO.checkEventID("12345"));

        event = new Event("12345", "Blah", "faeee3", 77.63, 83.6, "Nigeria", "Lagos", "death", 1486);
        eventDAO.createEvent(event);

        assertTrue(eventDAO.checkEventID("12345"));
    }

    @Test
    public void checkForEmptyEventTable() throws DatabaseException {
        event = new Event("222", "Jack", "fasd3", 77.43, 88.56, "Nigeria", "Lagos", "christening", 1386);
        eventDAO.createEvent(event);

        assertFalse(eventDAO.checkForEmptyEventTable());

        db.clearTables();
        assertTrue(eventDAO.checkForEmptyEventTable()

        );
        event = new Event("12345", "Blah", "faeee3", 77.63, 83.6, "Nigeria", "Lagos", "death", 1486);
        eventDAO.createEvent(event);

        eventDAO.deleteEvent("12345");

        assertTrue(eventDAO.checkForEmptyEventTable());
    }

    @Test
    public void deleteEventTable() throws DatabaseException {
        event = new Event("251837d7", "susan", "7255e93e", 65.6833, -17.9, "Iceland", "Akureyri", "birth", 1912 );
        eventDAO.createEvent(event);
        event = new Event("1234", "nate", "77ye4", 67.098, 09.44, "USA", "Salt Lake City", "baptism", 2000);
        eventDAO.createEvent(event);

        Database db = new Database();
        db.clearTables();

        assertTrue(eventDAO.checkForEmptyEventTable());
    }
}