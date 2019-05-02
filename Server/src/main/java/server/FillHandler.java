package server;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import dataAccess.DatabaseException;
import response.FillResponse;
import service.FillService;

class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                int numberGenerations;
                String userName;

                //SPLIT FUNCTION returns an array of strings with parameters...
                // urlPath will start at the username part of the URI... Ignore /fill/
                String urlPath = exchange.getRequestURI().getPath().substring(6);

                String[] params = urlPath.split("/");

                // If there is a specified generation, params.length() will equal 2.
                // If only the userName was provided, params.length() will equal 1.
                if (params.length == 1) {
                    userName = params[0];
                    numberGenerations = 4;

                }
                else if (params.length == 2) {
                    userName = params[0];
                    numberGenerations = Integer.parseInt(params[1]);
                }
                else {
                    throw new DatabaseException("Incorrect number of specifications for Fill. Please provide the username and number of generations" +
                                                " to be filled(optional)");
                }

                    FillService fillService = new FillService();
                    FillResponse fillResponse = fillService.fill(userName, numberGenerations);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    Gson gsonOBJ = new Gson();
                    String responseData = gsonOBJ.toJson(fillResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(responseData, respBody);
                    exchange.getResponseBody().close();

                    success = true;

            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();

            e.printStackTrace();
        }
        catch (DatabaseException f) {
            f.printStackTrace();
        }
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}