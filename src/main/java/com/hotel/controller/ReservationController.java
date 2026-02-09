package com.hotel.controller;

import com.hotel.service.ReservationService;
import servlet.annotations.Controller;
import servlet.ModelView;
import servlet.annotations.mapping.GetMapping;
import servlet.annotations.RequestParam;

@Controller
public class ReservationController {

    private ReservationService reservationService = new ReservationService();

    /**
     * Proxy vers l'API BackOffice : GET /reservations
     * Avec ou sans filtre par date
     */
    @GetMapping(value = "/reservations")
    public ModelView getReservations(@RequestParam(name = "date") String dateStr) {
        ModelView mv = new ModelView();
        try {
            String jsonResponse;
            if (dateStr != null && !dateStr.isEmpty()) {
                jsonResponse = reservationService.getReservationsByDateJson(dateStr);
            } else {
                jsonResponse = reservationService.getAllReservationsJson();
            }
            mv.addAttribute("jsonResponse", jsonResponse);
            mv.setView("json-response.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Erreur de connexion au BackOffice";
            mv.addAttribute("jsonResponse",
                    "{\"status\":\"error\",\"code\":500,\"message\":\"" + errorMsg + "\",\"data\":null}");
            mv.setView("json-response.jsp");
        }
        return mv;
    }
}
