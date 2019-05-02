package net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import request.LoginRequest;
import request.RegisterRequest;
import response.EventResponse;
import response.LoginResponse;
import response.PersonResponse;
import response.RegisterResponse;

public class ServerProxy {

    public LoginResponse login(String serverHost, String serverPort, LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Gson gson = new Gson();
            String reqData = gson.toJson(loginRequest);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);

            reqBody.close();
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                response = gson.fromJson(respData, LoginResponse.class);

            }
            else {
                response.setOutputMessage("Failed login attempt");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public RegisterResponse register(String hostNumber, String portNumber, RegisterRequest registerRequest) {
        RegisterResponse response = new RegisterResponse();

        try {
            URL url = new URL("http://" + hostNumber + ":" + portNumber + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            Gson gson = new Gson();
            String reqData = gson.toJson(registerRequest);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                response = gson.fromJson(respData, RegisterResponse.class);
            }
            else {
                response.setMessage("Failed register attempt");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public PersonResponse getAllPeople(String hostNumber, String portNumber, String authToken) {
        PersonResponse response = new PersonResponse();

        try {
            URL url = new URL("http://" + hostNumber + ":" + portNumber + "/person/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson gson = new Gson();
                response = gson.fromJson(respData, PersonResponse.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public EventResponse getAllEvents(String hostNumber, String portNumber, String authToken) {
        EventResponse response = new EventResponse();

        try {
            URL url = new URL("http://" + hostNumber + ":" + portNumber + "/event/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson gson = new Gson();
                response = gson.fromJson(respData, EventResponse.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
