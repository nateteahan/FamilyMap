package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import dataAccess.DatabaseException;
import request.LoadRequest;
import request.LoginRequest;
import response.LoadResponse;
import response.LoginResponse;
import service.LoadService;
import service.LoginService;

public class LoadHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Gson gson = new Gson();
                LoadRequest loadRequest = gson.fromJson(reqData, LoadRequest.class);

                try {
                    if (loadRequest != null) {
                        LoadService loadService = new LoadService();
                        LoadResponse loadResponse = loadService.load(loadRequest);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        Gson gsonOBJ = new Gson();
                        String responseData = gsonOBJ.toJson(loadResponse);
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(responseData, respBody);
                        exchange.getResponseBody().close();

                        success = true;
                    }
                } catch (DatabaseException e) {
                    System.out.println("CHECK YOUR LOAD HANDLER");
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
