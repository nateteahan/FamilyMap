package service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import model.User;
import request.RegisterRequest;
import response.FillResponse;

import static org.junit.Assert.*;

public class FillServiceTest {
    Database db = new Database();
    EventDAO eventDAO = new EventDAO();
    PersonDAO personDAO = new PersonDAO();
    UserDAO userDAO = new UserDAO();

    @Before
    public void setUp() throws DatabaseException {
        db.openConnection();
        eventDAO.createEventTable();
        personDAO.createPersonTable();
        userDAO.createUserTable();
    }

    @After
    public void tearDown() throws DatabaseException {
        db.closeConnection(true);
    }

    @Test
    public void fill() throws DatabaseException {
        //register user
        RegisterRequest registerRequest = new RegisterRequest("nateteahan", "myPassword", "nate@gmail.com",
                "nate", "teahan", "m");
        RegisterService service = new RegisterService();
        service.register(registerRequest);

        User userToRegister = new User("nateteahan", "myPassword", "nate@gmail.com", "nate", "teahan", "m", "123-456");
        int numberGenerations = 2;

        FillService fillService = new FillService();
        FillResponse fillResponse = fillService.fill(registerRequest.getUsername(), numberGenerations);

        //Assume successful message
        assertEquals("Successfully added 7 persons and 19 events to the database", fillResponse.getOutputMessage());


    }
}