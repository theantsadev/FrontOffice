package com.hotel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service pour communiquer avec l'API BackOffice
 * Passe le token pour authentifier les requêtes
 */
@Service
public class ReservationService {

    @Value("${backoffice.api.url:http://localhost:8080/backoffice}")
    private String backofficeBaseUrl;

    /**
     * Récupère toutes les réservations du BackOffice
     */
    public String getAllReservationsJson(String token) throws IOException {
        String url = backofficeBaseUrl + "/reservations?token=" + encodeUrl(token);
        return makeGetRequest(url);
    }

    /**
     * Récupère les réservations filtrées par date du BackOffice
     */
    public String getReservationsByDateJson(String date, String token) throws IOException {
        String url = backofficeBaseUrl + "/reservations?date=" + encodeUrl(date) + "&token=" + encodeUrl(token);
        return makeGetRequest(url);
    }

    /**
     * Effectue une requête GET vers le BackOffice
     */
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

    /**
     * Encode une chaîne pour l'URL
     */
    private String encodeUrl(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * Génère un JSON d'erreur formaté
     */
    public String getErrorJson(int code, String message) {
        String escapedMsg = message.replace("\"", "\\\"").replace("\n", "\\n");
        return "{\"status\":\"error\",\"code\":" + code + ",\"message\":\"" + escapedMsg + "\",\"data\":null}";
    }
}
