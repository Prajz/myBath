package com.example.mybath.networktask;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetBuildingTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... buildingNames) {
        URL endpoint;
        System.out.println(buildingNames[0]);
        System.out.println(buildingNames[1]);
        try {
            endpoint = new URL(String.format("http://192.168.0.47:8081/buildings?name=%s", buildingNames[0].replace(' ', '+')));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) endpoint.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream responseBody = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(reader);
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("name")) {
                        String value = jsonReader.nextString();
                        System.out.printf("Was able to find building %s!%n", value);
                    }
                    else {
                        jsonReader.skipValue();
                    }
                }
            }
            else {
                System.err.println("unhappy server");
            }
        } catch (IOException e) {
            System.err.printf("could not connect to server: %s%n", e);
            return null;
        }
        return null;
    }
}
