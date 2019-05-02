package server;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import dataAccess.DatabaseException;
import request.LoginRequest;
import response.LoginResponse;
import service.LoginService;

class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Gson gson = new Gson();
                LoginRequest loginRequest = gson.fromJson(reqData, LoginRequest.class);


                if (loginRequest != null) {
                    LoginService loginService = new LoginService();
                    LoginResponse loginResponse = loginService.login(loginRequest);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    Gson gsonOBJ = new Gson();
                    String responseData = gsonOBJ.toJson(loginResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(responseData, respBody);
                    exchange.getResponseBody().close();

                    success = true;
                }
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
        catch (DatabaseException d) {
            d.printStackTrace();
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