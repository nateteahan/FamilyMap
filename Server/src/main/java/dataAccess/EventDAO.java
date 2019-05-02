package dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Event;

public class EventDAO extends Database {                                                            // Extends Database to have access to connection.

    public EventDAO() {}

    /**
     * Creates the Event table for the relational database
     * @throws DatabaseException
     */
    public void createEventTable() throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successfulCreate = false;

        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS EVENT " +
                        "(EventID TEXT NOT NULL UNIQUE PRIMARY KEY, " +
                        " Descendant TEXT, " +
                        " PersonID TEXT, " +
                        " Latitude  REAL, " +
                        " Longitude REAL, " +
                        " Country TEXT, " +
                        " City TEXT, " +
                        " EventType TEXT, " +
                        " Year INTEGER)";
//                stmt.executeUpdate("DROP TABLE if exists EVENT");
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
     *
     * @param eventID
     * @return
     * @throws DatabaseException
     */
    public boolean deleteEvent(String eventID) throws DatabaseException{
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = null;
        boolean deleteStatus = false;

        try {
            String sql = "DELETE FROM EVENT WHERE eventID = '" + eventID + "'";

            stmt = conn.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {                                                     // CHECK TO MAKE SURE when I pass in sql it isn't messing anything up.
                // All good
                deleteStatus = true;
                db.closeConnection(deleteStatus);
                //System.out.println("Successfully deleted '" + eventID + "' from database!");
                return true;
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

    public boolean deleteDescendantEvents(String descendant) throws DatabaseException {
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = null;
        boolean deleteStatus = false;

        try {
            String sql = "DELETE FROM EVENT WHERE Descendant = '" + descendant + "'";

            stmt = conn.prepareStatement(sql);

            if (stmt.executeUpdate() >= 1) {                                                     // CHECK TO MAKE SURE when I pass in sql it isn't messing anything up.
                // All good
                deleteStatus = true;
                db.closeConnection(deleteStatus);
                //System.out.println("Successfully deleted '" + descendant + "' from database!");
                return true;
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

    /**
     * Creates an event
     * @param e Event object
     */
    public void createEvent(Event e) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        boolean successfulInsert = true;

        try {
            String sql = "INSERT into EVENT (eventID, descendant, personID, latitude, longitude, " +
                    "country, city, eventType, year) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, e.getEventID());
            stmt.setString(2, e.getDescendant());
            stmt.setString(3, e.getPersonID());
            stmt.setDouble(4, e.getLatitude());
            stmt.setDouble(5, e.getLongitude());
            stmt.setString(6, e.getCountry());
            stmt.setString(7, e.getCity());
            stmt.setString(8, e.getEventType());
            stmt.setInt(9, e.getYear());

            if (stmt.executeUpdate() == 1) {
                successfulInsert = true;
            }

        } catch (SQLException f) {
            successfulInsert = false;
            throw new DatabaseException("Error encountered while inserting into the databse.");
        }
        db.closeConnection(successfulInsert);
        return;
    }

    /**
     * Retrieves an event based of its ID
     * @param eventID   ID of the event to be searched for and returned
     * @return  Event object
     */
    public Event getEvent(String eventIdentification) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        Event eventObj = null;                                                                      // FIXME, what if try catch is unsuccessful, do we want to return null?
        boolean successfulGet = false;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT EventID, Descendant, PersonID, Latitude, Longitude, " +
                    "Country, City, EventType, Year FROM EVENT WHERE EventID = '" +
                    eventIdentification + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);                                                           // What do I do when I retrieve it? Is eventID supposed to be an int?
                String descendant = rs.getString(2);
                String personId = rs.getString(3);
                double latitude = rs.getDouble(4);
                double longitude = rs.getDouble(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);

                eventObj = new Event(id, descendant, personId, latitude, longitude, country, city, eventType, year);            // Create new user with appropriate data.
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
        return eventObj;
    }

    /**
     * Retrieves an event based of its ID
     * @param eventID   ID of the event to be searched for and returned
     * @return  Event object
     */
    public Event getEventFromDescendant(String descen) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        Event eventObj = null;                                                                      // FIXME, what if try catch is unsuccessful, do we want to return null?
        boolean successfulGet = false;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT EventID, Descendant, PersonID, Latitude, Longitude, " +
                    "Country, City, EventType, Year FROM EVENT WHERE Descendant = '" +
                    descen + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);                                                           // What do I do when I retrieve it? Is eventID supposed to be an int?
                String descendant = rs.getString(2);
                String personId = rs.getString(3);
                double latitude = rs.getDouble(4);
                double longitude = rs.getDouble(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);

                eventObj = new Event(id, descendant, personId, latitude, longitude, country, city, eventType, year);            // Create new user with appropriate data.
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
        return eventObj;
    }

    /**
     * Checks to see if an eventID is valid or not
     * @param eventID   String of the eventID to be inspected
     * @return  true if the eventID is valid, false otherwise
     */
    public boolean checkEventID(String eventID) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT eventID FROM EVENT WHERE eventID = '" +
                    eventID + "'";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);

                if (id.equals(eventID)) {                                                        // If the username exists, return true.
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

    public boolean checkForEmptyEventTable() throws DatabaseException {
        boolean isEmpty = false;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;

        try {
            Statement stmt = null;
            stmt = conn.createStatement();

            //Select all rows from the USER table.
            String sql = "SELECT * FROM EVENT";

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

    public ArrayList<Event> getAllPeople(String desc) throws DatabaseException {
        PreparedStatement stmt = null;
        Database db = new Database();
        db.openConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        Event eventObj = null;
        boolean successfulGet = false;
        ArrayList<Event> allEvents = new ArrayList<>();

        try {                                                                                       // Do I need to access all variables of user and assign it?
            String sql = "SELECT EventID, Descendant, PersonID, Latitude, Longitude, Country, City, EventType, Year FROM EVENT WHERE Descendant = '" +
                    desc + "'";

            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String evID = rs.getString(1);                                                     // What do I do when I retrieve it?
                String descendant = rs.getString(2);
                String personID = rs.getString(3);
                Double lat = rs.getDouble(4);
                Double longit = rs.getDouble(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eType = rs.getString(8);
                int year = rs.getInt(9);

                eventObj = new Event(evID, descendant, personID, lat, longit, country, city, eType, year);
                allEvents.add(eventObj);
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
        return allEvents;
    }
}
