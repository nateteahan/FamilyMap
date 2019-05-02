package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.*;

import dataAccess.AuthTokenDAO;
import dataAccess.DatabaseException;
import dataAccess.EventDAO;
import dataAccess.PersonDAO;
import dataAccess.UserDAO;

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);

        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        // Log message indicating that the server is creating and installing
        // its HTTP handlers.
        // The HttpServer class listens for incoming HTTP requests.  When one
        // is received, it looks at the URL path inside the HTTP request, and
        // forwards the request to the handler for that URL path.
        System.out.println("Creating contexts");

        // Create and install the HTTP handler for the "/login" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/login" URL path, it will forward the request to LoginHandler
        // for processing.
        server.createContext("/user/register", new RegisterHandler());

        server.createContext("/user/login", new LoginHandler());

        server.createContext("/clear/", new ClearHandler());

        server.createContext("/fill/", new FillHandler());

        server.createContext("/load/", new LoadHandler());

        server.createContext("/person/", new PersonHandler());

        server.createContext("/event/", new EventHandler());

        server.createContext("/", new DefaultHandler());

        System.out.println("Starting server");

        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started");
    }

    public static void main(String[] args) throws DatabaseException {
        String portNumber = args[0];
        EventDAO event = new EventDAO();
        AuthTokenDAO authToken = new AuthTokenDAO();
        PersonDAO person = new PersonDAO();
        UserDAO user = new UserDAO();

        // Create the table for the database when starting the database...
        event.createEventTable();
        authToken.createAuthTokenTable();
        person.createPersonTable();
        user.createUserTable();

        new Server().run(portNumber);
    }
}
