package com.hotel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

@Service
public class ReservationService {

    @Value("${backoffice.api.url:http://localhost:8080/backoffice}")
    private String backofficeBaseUrl;

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
