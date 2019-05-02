package dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

public class UserDAO extends Database {

    public UserDAO() {
    }

    /**
     * Creates the User table for the relational database
     * @throws DatabaseException
     */
    public void createUserTable () throws DatabaseException {
        Database db = new Database();                                                               // All this was in the constructor... Removed for services.
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successCreateTable = false;

        try {

            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS USER " +
                        "(username TEXT NOT NULL UNIQUE PRIMARY KEY, " +
                        " password TEXT NOT NULL, " +
                        " email TEXT NOT NULL, " +
                        " firstName TEXT NOT NULL, " +
                        " lastName TEXT NOT NULL, " +
                        " gender TEXT NOT NULL, " +
                        " personID TEXT UNIQUE)";

//                stmt.executeUpdate("DROP TABLE if exists USER");
                stmt.executeUpdate(sql);
                successCreateTable = true;
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("createTables failed");
        }
        db.closeConnection(successCreateTable);
    }

    /**
     * Creates a user with all its data
     *
     * @param u User object
     */
    public void createUser(User u) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successfulInsert = false;

        try {
            String sql = "INSERT into USER (username, password, email, firstName, lastName, gender, " +
                    "personID) values (?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail_address());
            stmt.setString(4, u.getFirstName());
            stmt.setString(5, u.getLastName());
            stmt.setString(6, u.getGender());
            stmt.setString(7, u.getPersonID());

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
     * Returns a user from the database
     *
     * @param personID unique ID of the person to retrieve from database
     * @return User object
     */
    public User getUser(String username) throws DatabaseException {                                        // I want to delete PersonID as a parameter but it gives me an error.
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        User userObj = null;
        boolean successfulGet = false;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT username, password, email, firstName, lastName, gender, personID FROM USER WHERE username = '" +
                    username + "'";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String password = rs.getString(2);
                String email = rs.getString(3);
                String first_name = rs.getString(4);
                String last_name = rs.getString(5);
                String gender = rs.getString(6);
                String ID = rs.getString(7);

                userObj = new User(name, password, email, first_name, last_name, gender, ID);
                successfulGet = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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

        db.closeConnection(successfulGet);
        return userObj;
    }

    /**
     * Deletes a user from the database
     *
     * @param userName  userName of the user to be deleted
     * @return  true if delete was successful, false otherwise
     */
    public boolean deleteUser(String userName) throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = null;
        boolean successfulDelete = false;

        try {
            String sql = "DELETE FROM USER WHERE username = '" + userName + "'";

            stmt = conn.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {                                                     // CHECK TO MAKE SURE when I pass in sql it isn't messing anything up.
                successfulDelete = true;
            }
            else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        db.closeConnection(successfulDelete);
        return successfulDelete;
    }

    /**
     * Checks to see if a certain username already exists in database.
     * @param username  userName to be searched for in database
     * @return  true if userName already exists, false otherwise.
     */
    public boolean checkUserName(String username) throws DatabaseException{
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;


        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT username FROM USER WHERE username = '" +
                    username + "'";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);

                if (name.equals(username)) {                                                        // If the username exists, return true.
                    db.closeConnection(true);
                    return true;
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
     * Checks to see if the User table is empty or not. Useful for the clear service.
     * @return  true if empty, false otherwise
     * @throws DatabaseException
     */
    public boolean checkForEmptyUserTable() throws DatabaseException {
        boolean isEmpty = false;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;

        try {
            Statement stmt = null;
            stmt = conn.createStatement();

            //Select all rows from the USER table.
            String sql = "SELECT * FROM USER";

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

//    public void clearUserTable() throws DatabaseException {
//        Database db = new Database();
//        db.openConnection();
//        Connection conn = db.getConnection();
//
//        try (Statement stmt = conn.createStatement()){
//            String sql = "DELETE FROM USER";
//            stmt.executeUpdate(sql);
//            db.closeConnection(true);
//        } catch (DatabaseException e) {
//            db.closeConnection(false);
//            throw e;
//        } catch (SQLException e) {
//            db.closeConnection(false);
//            throw new DatabaseException("SQL Error encountered while clearing USER table");
//        }
//    }
    
}
