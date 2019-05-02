package dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Person;

public class PersonDAO extends Database {

    public PersonDAO() {
    }

    /**
     * Creates the Person table for the relational database
     * @throws DatabaseException
     */
    public void createPersonTable() throws DatabaseException{
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successfulCreate = false;

        try {

            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS PERSON " +
                        "(personID TEXT NOT NULL UNIQUE PRIMARY KEY, " +
                        " descendant TEXT, " +
                        " firstName TEXT NOT NULL, " +
                        " lastName TEXT NOT NULL, " +
                        " gender TEXT NOT NULL, " +
                        " fatherID TEXT, " +
                        " motherID TEXT, " +
                        " spouseID TEXT)";

//                stmt.executeUpdate("DROP TABLE if exists PERSON");
                stmt.executeUpdate(sql);
                successfulCreate = true;
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("createTables failed");
        }
        db.closeConnection(successfulCreate);
    }

    /**
     * Deletes the person data from the database
     * @param personID  ID of the person to be removed
     * @return  true if person was deleted, false otherwise
     */
    public boolean deletePerson(String descendant) throws DatabaseException{
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = null;
        boolean deleteStatus = false;

        try {
            String sql = "DELETE FROM PERSON WHERE descendant = '" + descendant + "'";

            stmt = conn.prepareStatement(sql);

            if (stmt.executeUpdate() >= 1) {                                                     // CHECK TO MAKE SURE when I pass in sql it isn't messing anything up.
                deleteStatus = true;
                db.closeConnection(deleteStatus);
                //System.out.println("Successfully deleted '" + descendant + "' from database!");
                return deleteStatus;
            }
            else {
                db.closeConnection(deleteStatus);
                return false;
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
        db.closeConnection(deleteStatus);
        return deleteStatus;
    };

    /**
     * Creates new person for the database
     * @param p Person object with all its data
     */
    public boolean createPerson(Person p) throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = null;
        boolean successfulCreate = false;

        try {
            String sql = "INSERT into PERSON (personID, descendant, firstName, lastName, " +
                            "gender, fatherID, motherID, spouseID) values (?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getPersonID());
            stmt.setString(2, p.getDescendant());
            stmt.setString(3, p.getFirstName());
            stmt.setString(4, p.getLastName());
            stmt.setString(5, p.getGender());
            stmt.setString(6, p.getFather());
            stmt.setString(7, p.getMother());
            stmt.setString(8, p.getSpouse());

            if (stmt.executeUpdate() == 1) {
                successfulCreate = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection(successfulCreate);
        return successfulCreate;
    }

    /**
     * Retrieves a person's data from the database
     * @param personID  ID of the person's data to get
     * @return  Person object.
     */
    public Person getPerson(String personID) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        Person personObj = null;
        boolean successfulGet = false;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT personID, descendant, firstName, lastName, " +
                    "gender, fatherID, motherID, spouseID FROM PERSON WHERE personID = '" +
                         personID + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);                                                     // What do I do when I retrieve it?
                String descendant = rs.getString(2);
                String first_name = rs.getString(3);
                String last_name = rs.getString(4);
                String gender = rs.getString(5);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);

                personObj = new Person(id, descendant, first_name, last_name, gender, fatherID, motherID, spouseID);
                successfulGet = true;
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

        db.closeConnection(successfulGet);
        return personObj;
    }

    /**
     * Checks to see if a personID is valid or not
     * @param personID  String of a person's ID
     * @return  true if the ID is valid, false otherwise
     */
    public boolean checkPersonID(String personID) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT personID FROM PERSON WHERE personID = '" +
                    personID + "'";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);

                if (name.equals(personID)) {                                                        // If the username exists, return true.
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
     * Checks to see if the Person table is empty
     * @return  true if PERSON is empty, otherwise false
     * @throws DatabaseException
     */
    public boolean checkForEmptyPersonTable() throws DatabaseException {
        boolean isEmpty = false;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;

        try {
            Statement stmt = null;
            stmt = conn.createStatement();

            //Select all rows from the USER table.
            String sql = "SELECT * FROM PERSON";

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

    /**
     * Function designed to delete all the data associated with a personID, but not to delete the person entirely.
     * @param personID
     * @return  true if the deletion worked properly, false otherwise.
     * @throws DatabaseException
     */
    public boolean deletePersonData(String personID) throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = null;
        boolean deleteStatus = false;

        try {
            String sql = "DELETE descendant, firstName, lastName, gender, fatherID, motherID, spouseID FROM PERSON WHERE personID = '" + personID + "'";

            stmt = conn.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                deleteStatus = true;
                db.closeConnection(deleteStatus);
                return deleteStatus;
            }
            else {
                db.closeConnection(deleteStatus);
                return false;
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
        db.closeConnection(deleteStatus);
        return deleteStatus;
    }

    public ArrayList<Person> getAllPeople(String desc) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        Person personObj = null;
        boolean successfulGet = false;
        ArrayList<Person> allPeople = new ArrayList<>();

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT personID, descendant, firstName, lastName, " +
                    "gender, fatherID, motherID, spouseID FROM PERSON WHERE descendant = '" +
                    desc + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);                                                     // What do I do when I retrieve it?
                String descendant = rs.getString(2);
                String first_name = rs.getString(3);
                String last_name = rs.getString(4);
                String gender = rs.getString(5);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);

                personObj = new Person(id, descendant, first_name, last_name, gender, fatherID, motherID, spouseID);
                allPeople.add(personObj);
                successfulGet = true;
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

        db.closeConnection(successfulGet);
        return allPeople;
    }
}
