package dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.AuthToken;

public class AuthTokenDAO {

    public AuthTokenDAO() {}

    /**
     * Constructor for AuthToken
     * @param userName  name of the user to generate an authToken for
     * @param password  password for the current user
     */
    public void createAuthTokenTable() throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successCreate = false;

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS AUTHTOKEN " +
                    "(username TEXT NOT NULL, " +
                    "authToken TEXT NOT NULL UNIQUE PRIMARY KEY)";

            stmt.executeUpdate(sql);
            successCreate = true;
        } catch (SQLException e) {
            throw new DatabaseException("createTables failed");
        }
        finally {
            if (stmt != null) {
                //stmt.close();
                stmt = null;
            }
        }

        db.closeConnection(successCreate);
    }

    /**
     * Searches for an authToken to delete after a certain amount of time so it is no longer valid
     * @param authToken AuthToken string for a user
     * @return      True if authToken was deleted, false otherwise
     */
    public boolean deleteAuthToken(String authToken) throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();                                                       // Allow access to connection
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM AUTHTOKEN WHERE authToken = '" + authToken + "'";                 // DELETE FROM or just Delete. Caps?
            stmt = conn.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                db.closeConnection(true);
                return true;
            } else {
                db.closeConnection(false);
                return false;
            }
        } catch (SQLException e) {
            //db.closeConnection(false);
            throw new DatabaseException("Bad connection in deleteAuthToken");
        } finally {
            try {
                if (stmt != null) {
//                    db.closeConnection(false);                                              //FIXME i think i close connection too much
                    stmt.close();
                }
            } catch (SQLException e) {
               // db.closeConnection(false);
                throw new DatabaseException("stmt failed to close");
            }
        }
    }

    /**
     * Create a new authToken for a user
     * @param a AuthToken object
     */
    public void generateAuthToken(AuthToken a) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successfulInsert = false;

        try {
            String sql = "INSERT into AUTHTOKEN (username, authToken) values (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, a.getUsername());
            stmt.setString(2, a.getAuthToken());

            if (stmt.executeUpdate() == 1) {
                successfulInsert = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.closeConnection(successfulInsert);

        return;
    }

    /**
     * Returns the authToken string of a person
     * @param userName  name of the user to return the authToken to
     * @return AuthToken object
     */
    public AuthToken retrieveAuthToken(String userName) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        AuthToken authTokenObj = null;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT username, authToken FROM AUTHTOKEN WHERE username = '" +
                    userName + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);                                                     // What do I do when I retrieve it?
                String token = rs.getString(2);

                authTokenObj = new AuthToken(name, token);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        db.closeConnection(false);
        return authTokenObj;
    }

    /**
     * returns the authtoken based on a given user names authtoken, instead of their username
     * @param authToken
     * @return
     * @throws DatabaseException
     */
    public AuthToken getUserToken(String authToken) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        AuthToken authTokenObj = null;

        try {
            String sql = "SELECT username, authToken FROM AUTHTOKEN WHERE authToken = '" +
                    authToken + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);                                                     // What do I do when I retrieve it?
                String token = rs.getString(2);

                authTokenObj = new AuthToken(name, token);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        db.closeConnection(false);
        return authTokenObj;
    }

    /**
     * Checks to see if an authToken is valid or not
     * @param authToken String of an authToken to be inspected
     * @return  true if the authToken is valid, false otherwise
     */
    public boolean checkAuthToken(String authToken) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        boolean success = false;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT authToken FROM AUTHTOKEN WHERE authToken = '" +
                    authToken + "'";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
               // String name = rs.getString(1);
                String token = rs.getString(1);

                if (token.equals(authToken)) {
                    // If the username exists, return true.
                    success = true;
                    db.closeConnection(success);
                    return success;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        db.closeConnection(false);
        return false;
    }

    /**
     * Checks to see if the AUTHTOKEN table is empty or not
     * @return  true if emtpy, false otherwise
     * @throws DatabaseException
     */
    public boolean checkForEmptyAuthTokenTable() throws DatabaseException {
        boolean isEmpty = false;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;

        try {
            Statement stmt = null;
            stmt = conn.createStatement();

            //Select all rows from the USER table.
            String sql = "SELECT * FROM AUTHTOKEN";

            rs = stmt.executeQuery(sql);

            //If there is nothing in the table, rs will be empty
            if (rs.next() == false) {
                isEmpty = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Need to pass in true or else an error to close the connection will be thrown.
        db.closeConnection(true);
        return isEmpty;
    }
}
