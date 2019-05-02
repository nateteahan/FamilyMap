package service;

import dataAccess.AuthTokenDAO;
import dataAccess.Database;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;
import response.ClearResponse;

/**
 * Service class for Clear. Performs one request and returns an ClearResponse object.
 */
public class ClearService {

    /**
     * clears all data from the database
     * @return returns a ClearResponse object.
     */
    public ClearResponse clear() throws DatabaseException {
        AuthTokenDAO authTokenTable = new AuthTokenDAO();
        EventDAO eventTable = new EventDAO();
        PersonDAO personTable = new PersonDAO();
        UserDAO userTable = new UserDAO();
        ClearResponse response = new ClearResponse();
        Database db = new Database();

        // By calling each DAO's createTable method, I am dropping the old tables if they exist in the database
//        authTokenTable.createAuthTokenTable();
//        eventTable.createEventTable();
//        personTable.createPersonTable();
//        userTable.clearUserTable();
            db.clearTables();
        // If all tables return true for being empty, handle the response object.
        if (userTable.checkForEmptyUserTable() && eventTable.checkForEmptyEventTable()
                && authTokenTable.checkForEmptyAuthTokenTable() && personTable.checkForEmptyPersonTable()) {
            response.setOutputMessage("Clear succeeded.");
        }
        else {
            response.setOutputMessage("Something went wrong when attempting to clear all the data from the database.");
        }

        return response;
    }
}
