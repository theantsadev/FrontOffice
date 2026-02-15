package com.hotel.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ReservationService {

    private String backofficeBaseUrl;

    public ReservationService() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("api.properties");
            if (input != null) {
                props.load(input);
                backofficeBaseUrl = props.getProperty("backoffice.api.url", "http://localhost:8080/backoffice");
                input.close();
            } else {
                backofficeBaseUrl = "http://localhost:8080/backoffice";
            }
        } catch (IOException e) {
            e.printStackTrace();
            backofficeBaseUrl = "http://localhost:8080/backoffice";
        }
    }

    /**
     * Appel API BackOffice : GET /reservations
     * Retourne le JSON brut de toutes les reservations
     */
    public String getAllReservationsJson() throws IOException {
        String url = backofficeBaseUrl + "/reservations";
        return makeGetRequest(url);
    }

    /**
     * Appel API BackOffice : GET /reservations?date=yyyy-MM-dd
     * Retourne le JSON brut des reservations filtrees par date
     */
    public String getReservationsByDateJson(String date) throws IOException {
        String url = backofficeBaseUrl + "/reservations?date=" + date;
        return makeGetRequest(url);
    }

    private String makeGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();

        InputStream inputStream;
        if (responseCode >= 200 && responseCode < 300) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            conn.disconnect();
        }

        return response.toString();
    }
}
