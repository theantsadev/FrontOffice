package com.hotel.controller;


import com.hotel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * RestController pour les réservations
 * Proxy vers l'API BackOffice avec gestion du token
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;



    /**
     * GET /reservations
     * Récupère toutes les réservations ou filtre par date
     * Requiert un token valide
     */
    @GetMapping
    public String getReservations(
            @RequestParam(name = "token", required = false) String token,
            @RequestParam(name = "date", required = false) String dateStr) {

        try {

            // Appeler le BackOffice avec le token
            String jsonResponse;
            if (dateStr != null && !dateStr.isEmpty()) {
                jsonResponse = reservationService.getReservationsByDateJson(dateStr, token);
            } else {
                jsonResponse = reservationService.getAllReservationsJson(token);
            }

            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Erreur de connexion au BackOffice";
            return reservationService.getErrorJson(500, errorMsg);
        }
    }
}
