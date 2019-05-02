package dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

import model.AuthToken;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    Database db = new Database();
    UserDAO userDAO = new UserDAO();
    PersonDAO personDAO = new PersonDAO();
    EventDAO eventDAO = new EventDAO();
    AuthTokenDAO authTokenDAO = new AuthTokenDAO();
    AuthToken authToken;

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
    public void deleteAuthToken() throws DatabaseException {
            authToken = new AuthToken("Henry", "12we45ty");
            authTokenDAO.generateAuthToken(authToken);

            assertTrue(authTokenDAO.deleteAuthToken("12we45ty"));
    }

    @Test
    public void generateAuthToken() throws DatabaseException {
        authToken = new AuthToken("Nate", "a0z1");
        authTokenDAO.generateAuthToken(authToken);
        authToken = new AuthToken("Pam", "1a2s");
        authTokenDAO.generateAuthToken(authToken);

        assertEquals("Nate", authTokenDAO.retrieveAuthToken("Nate").getUsername());
        assertEquals("a0z1", authTokenDAO.retrieveAuthToken("Nate").getAuthToken());

        assertEquals("Pam", authTokenDAO.retrieveAuthToken("Pam").getUsername());
        assertEquals("1a2s", authTokenDAO.retrieveAuthToken("Pam").getAuthToken());

        assertTrue(authTokenDAO.checkAuthToken("a0z1"));
        assertTrue(authTokenDAO.checkAuthToken("1a2s"));

        assertTrue(authTokenDAO.deleteAuthToken("a0z1"));
        assertTrue(authTokenDAO.deleteAuthToken("1a2s"));
    }

    @Test
    public void retrieveAuthToken() throws DatabaseException {
        AuthToken authTokenOne = new AuthToken("Nate", "a0z1");
        authTokenDAO.generateAuthToken(authTokenOne);
        authToken = new AuthToken("Pam", "a45t");
        authTokenDAO.generateAuthToken(authToken);

        assertTrue(authTokenDAO.checkAuthToken("a0z1"));
        assertTrue(authTokenDAO.checkAuthToken("a45t"));

        AuthToken newAuth = authTokenDAO.retrieveAuthToken("Nate");

        assertEquals(newAuth.getAuthToken(), authTokenOne.getAuthToken());
        assertEquals(newAuth.getUsername(), authTokenOne.getUsername());
    }

    @Test
    public void checkAuthToken() throws DatabaseException {
        authToken = new AuthToken("Jill", "ufndld");
        authTokenDAO.generateAuthToken(authToken);

        assertTrue(authTokenDAO.checkAuthToken("ufndld"));
    }

    @Test
    public void checkForEmptyAuthTokenTable() throws DatabaseException {
        Database db = new Database();
        authToken = new AuthToken("Nate", "a0z1");
        authTokenDAO.generateAuthToken(authToken);

        db.clearTables();
        authTokenDAO.createAuthTokenTable();
        assertTrue(authTokenDAO.checkForEmptyAuthTokenTable());

        authToken = new AuthToken("Pam", "1a2s");
        authTokenDAO.generateAuthToken(authToken);

        assertFalse(authTokenDAO.checkForEmptyAuthTokenTable());

        authTokenDAO.deleteAuthToken("1a2s");
        assertTrue(authTokenDAO.checkForEmptyAuthTokenTable());
    }

    @Test
    public void getUserToken() throws DatabaseException {
        //Same authtokens generated as retrieveAuthToken() test
        AuthToken authTokenOne = new AuthToken("Nate", "a0z1");
        authTokenDAO.generateAuthToken(authTokenOne);
        AuthToken authTokenTwo = new AuthToken("Pam", "a45t");
        authTokenDAO.generateAuthToken(authTokenTwo);

        AuthToken checkToken = authTokenDAO.getUserToken(authTokenOne.getAuthToken());

        //Returns the authtoken object based off the authtoken of a user
        assertEquals(authTokenOne.getUsername(), checkToken.getUsername());

    }
}