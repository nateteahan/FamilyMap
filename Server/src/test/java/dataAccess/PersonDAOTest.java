package dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import model.Person;

import static org.junit.Assert.*;

public class PersonDAOTest {
    Database db = new Database();
    UserDAO userDAO = new UserDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    AuthTokenDAO authTokenDAO = new AuthTokenDAO();
    Person person;

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
    public void deletePerson() throws DatabaseException {
        person = new Person("12dsf", "Jim", "Nate", "Teahan", "m", "23e", "43f", "3dsf");
        personDAO.createPerson(person);

        assertTrue(personDAO.deletePerson("Jim"));
        assertFalse(personDAO.deletePerson("324ffa"));
        assertFalse(personDAO.checkPersonID("12dsf"));
    }

    @Test //Unnecessary because of checkForEmptyPersonTable
    public void deletePersonTable() throws DatabaseException {
        person = new Person("12dsf", "Jim", "Nate", "Teahan", "m", "23e", "43f", "3dsf");
        personDAO.createPerson(person);

        assertTrue(personDAO.deletePerson("Jim"));
        assertFalse(personDAO.deletePerson("324ffa"));

        //Delete data from person table
        personDAO.createPersonTable();

        assertTrue(personDAO.checkForEmptyPersonTable());


    }

    @Test
    public void createPerson() throws DatabaseException {
        person = new Person("12dsf", "Jim", "Nate", "Teahan", "m", "23e", "43f", "3dsf");
        personDAO.createPerson(person);

        assertEquals("Jim", personDAO.getPerson("12dsf").getDescendant());
        assertNotEquals("Billy", personDAO.getPerson("12dsf").getFirstName());

        person = new Person("12dsf", "Jim", "Nate", "Teahan", "m", "23e", "43f", "3dsf");
    }

    @Test
    public void getPerson() throws DatabaseException{
        person = new Person("1122s", "Adam", "Bob", "Bares", "m", "32s", "r23fa", "23fs");
        personDAO.createPerson(person);

        assertTrue("Adam".equals(personDAO.getPerson("1122s").getDescendant()));
        assertFalse("Eve".equals(personDAO.getPerson("1122s").getFirstName()));

        person = new Person("1123", "Adam", "Eve", "Jares", "f", "432S", "F7AS", "FDSA3");
        personDAO.createPerson(person);

        assertTrue("Eve".equals(personDAO.getPerson("1123").getFirstName()));
    }

    @Test
    public void checkPersonID() throws DatabaseException {
        person = new Person("12d", "Adam", "Tilly", "Tares", "f", "32s", "32sa", "324wre");
        personDAO.createPerson(person);

        assertTrue(personDAO.checkPersonID("12d"));
        assertFalse(personDAO.checkPersonID("13d"));
    }

    @Test
    public void checkForEmptyPersonTable() throws DatabaseException {
        person = new Person("1122s", "Adam", "Bob", "Bares", "m", "32s", "r23fa", "23fs");
        personDAO.createPerson(person);

        db.clearTables();

        assertTrue(personDAO.checkForEmptyPersonTable());

        person = new Person("1123", "Adam", "Eve", "Jares", "f", "432S", "F7AS", "FDSA3");
        personDAO.createPerson(person);

        assertFalse(personDAO.checkForEmptyPersonTable());

        personDAO.deletePerson("Adam");

        assertTrue(personDAO.checkForEmptyPersonTable());
    }
}