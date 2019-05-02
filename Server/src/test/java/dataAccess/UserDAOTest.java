package dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.User;

import static org.junit.Assert.*;

public class UserDAOTest {
    Database db = new Database();
    UserDAO userDAO = new UserDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    AuthTokenDAO authTokenDAO = new AuthTokenDAO();
    User user;

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
    public void createUser() throws DatabaseException {
        user = new User("Nate", "fjdksl", "nate@gmail.com", "nate", "teahan", "m", "i38d0");
        userDAO.createUser(user);

        assertEquals("nate@gmail.com", userDAO.getUser("Nate").getEmail_address());
        assertFalse("nate".equals(userDAO.getUser("Nate").getUsername()));
    }

    @Test
    public void getUser() throws DatabaseException{
        user = new User("HenryRules", "ffdsa", "KingHenry@gmail.com", "Henry", "VIII", "m", "KD>0");
        userDAO.createUser(user);

        assertEquals("HenryRules", userDAO.getUser("HenryRules").getUsername());
        assertEquals("KingHenry@gmail.com", userDAO.getUser("HenryRules").getEmail_address());
        assertEquals("ffdsa", userDAO.getUser("HenryRules").getPassword());
        assertEquals("Henry", userDAO.getUser("HenryRules").getFirstName());
        assertEquals("VIII", userDAO.getUser("HenryRules").getLastName());
        assertEquals("m", userDAO.getUser("HenryRules").getGender());
        assertEquals("KD>0", userDAO.getUser("HenryRules").getPersonID());

        assertNotEquals("KD>1", userDAO.getUser("HenryRules").getPersonID());

    }

    @Test
    public void deleteUser() throws DatabaseException {
        user = new User("Nate", "fjdksl", "nate@gmail.com", "nate", "teahan", "m", "i38d0");
        userDAO.createUser(user);

        assertTrue(userDAO.deleteUser("Nate"));
    }

    @Test
    public void deleteUserTable() throws DatabaseException {
        user = new User("Nate", "fjdksl", "nate@gmail.com", "nate", "teahan", "m", "i38d0");
        userDAO.createUser(user);

        assertTrue(userDAO.deleteUser("Nate"));

        userDAO.createUserTable();

        assertTrue(userDAO.checkForEmptyUserTable());
    }

    @Test
    public void checkUserName() throws DatabaseException {
        user = new User("James", "fjdkddsl", "James@gmail.com", "James", "Beill", "m", "dds0");
        userDAO.createUser(user);

        assertTrue(userDAO.checkUserName("James"));
        assertFalse(userDAO.checkUserName(null));

        userDAO.deleteUser("James");

        assertFalse(userDAO.checkUserName("James"));
    }

    @Test
    public void checkForEmptyTable() throws DatabaseException {
        user = new User("James", "fjdkddsl", "James@gmail.com", "James", "Beill", "m", "dds0");
        userDAO.createUser(user);

        db.clearTables();
        assertTrue(userDAO.checkForEmptyUserTable());

        user = new User("Nate", "rtyuw7", "Nate@gmail.com", "Nate", "Heimer", "m", "678dne");
        userDAO.createUser(user);

        assertFalse(userDAO.checkForEmptyUserTable());
    }

}