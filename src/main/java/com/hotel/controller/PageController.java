package com.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/pages/")
    public String index() {
        return "pages/index.jsp";
    }

    @GetMapping(value = "/pages/liste-reservations")
    public String listeReservations() {
        return "pages/liste-reservations.jsp";
    }
}
