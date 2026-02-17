package com.hotel.controller;

import com.hotel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * Proxy vers l'API BackOffice : GET /reservations
     * Avec ou sans filtre par date
     */
    @GetMapping
    public String getReservations(@RequestParam(name = "date", required = false) String dateStr) {
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                return reservationService.getReservationsByDateJson(dateStr);
            } else {
                return reservationService.getAllReservationsJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Erreur de connexion au BackOffice";
            return "{\"status\":\"error\",\"code\":500,\"message\":\"" + errorMsg + "\",\"data\":null}";
        }
    }
}
