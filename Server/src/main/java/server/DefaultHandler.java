package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;


public class DefaultHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String FILE_ROOT_DIR = "/Users/NatetheWizard/AndroidStudioProjects/Family_Map/Server/src/main/web";
        String urlPath = "";
        boolean success = false;

        try {
            urlPath = exchange.getRequestURI().getPath();
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
            }

            if (urlPath.isEmpty() || urlPath.equals("/")) {
                urlPath = "index.html";
            }
            else {
                urlPath = "/HTML/404.html";
            }

            // Prepend the root directory to the urlPath and add a '/'
            String filePath = FILE_ROOT_DIR + "/" + urlPath;
            File file = new File(filePath);


            if (file.exists() && file.canRead()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream responseBody = exchange.getResponseBody();
                Files.copy(file.toPath(), responseBody);
                responseBody.close();
                success = true;
            }

            if (!success) {
                //Do something here.
                //Send bad request
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                //Close body
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }


    }
}
