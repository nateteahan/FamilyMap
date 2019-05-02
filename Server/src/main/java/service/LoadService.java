package service;

import java.util.UUID;

import dataAccess.AuthTokenDAO;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.ClearResponse;
import response.LoadResponse;

/**
 * Service class for Load. Performs one request and returns an LoadResponse object.
 */
public class LoadService {

    /**
     * Clears all data from the database, and then loads the
     posted user, person, and event data into the database.
     * @param l LoadRequest object
     * @return  LoadResponse object
     */
    public LoadResponse load(LoadRequest l) throws DatabaseException {
        LoadResponse response = new LoadResponse();
        UserDAO userDataAccess = new UserDAO();
        PersonDAO personDataAccess = new PersonDAO();
        EventDAO eventDataAccess = new EventDAO();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        /* Create these objects to clear the database */
        ClearService service = new ClearService();
        service.clear();

        int numUsers = 0;
        for (User u : l.getUsers()) {
            String token = UUID.randomUUID().toString();
            AuthToken authToken = new AuthToken(u.getUsername(), token);
            authTokenDAO.generateAuthToken(authToken);
            userDataAccess.createUser(u);
            numUsers++;
        }

        int numPersons = 0;
        for (Person p : l.getPersons()) {
            personDataAccess.createPerson(p);
            numPersons++;
        }

        int numEvents = 0;
        for (Event e : l.getEvents()) {
            eventDataAccess.createEvent(e);
            numEvents++;
        }

        if (!eventDataAccess.checkForEmptyEventTable() && !personDataAccess.checkForEmptyPersonTable() && !userDataAccess.checkForEmptyUserTable()) {
            response.setOutputMessage("Successfully added " + numUsers + " users, " + numPersons + " persons, and " + numEvents + " events to the database");
        }
        else {
            response.setOutputMessage("Something went wrong with the add. No users, persons, or events were added. Check the given arrays in request class.");
        }

        return response;
    }
}
