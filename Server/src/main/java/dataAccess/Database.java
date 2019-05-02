package dataAccess;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.lang.Exception;

public class Database {


    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection conn;

    public boolean openConnection() throws DatabaseException {
        boolean connectionStatus = true;

        try {
            final String CONNECTION_URL = "jdbc:sqlite:familyData.sqlite";                          // Name of database = familyData.sqlite

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        }
        catch (SQLException e) {
            connectionStatus = false;
            throw new DatabaseException("openConnection failed");
        }

        return connectionStatus;
    }

    public boolean closeConnection(boolean commit) throws DatabaseException {
        boolean connectionStatus = true;

        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
                connectionStatus = false;
            }

            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            connectionStatus = false;
            throw new DatabaseException("closeConnection failed");
        }

        return connectionStatus;
    }

    public Connection getConnection() {
        return conn;
    }

    public void clearTables() throws DatabaseException {

        openConnection();

        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM USER";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DatabaseException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DatabaseException("SQL Error encountered while clearing USER table");
        }

        openConnection();

        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM PERSON";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DatabaseException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DatabaseException("SQL Error encountered while clearing PERSON table");
        }

        openConnection();

        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM AUTHTOKEN";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DatabaseException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DatabaseException("SQL Error encountered while clearing AUTHTOKEN table");
        }

        openConnection();

        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM EVENT";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DatabaseException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DatabaseException("SQL Error encountered while clearing EVENT table");
        }
    }

    public static void main(String[] args) {
        Database db = new Database();

        try {                                                                                       // Only placeholders.
            db.openConnection();
            db.closeConnection(false);

        } catch (DatabaseException e) {
            System.out.println("Bad database connection");
        }
    }
}
