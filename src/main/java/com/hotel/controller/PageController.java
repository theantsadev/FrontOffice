package com.hotel.controller;

import servlet.annotations.Controller;
import servlet.ModelView;
import servlet.annotations.mapping.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/pages/")
    public ModelView index() {
        ModelView mv = new ModelView();
        mv.setView("pages/index.jsp");
        return mv;
    }

    @GetMapping(value = "/pages/liste-reservations")
    public ModelView listeReservations() {
        ModelView mv = new ModelView();
        mv.setView("pages/liste-reservations.jsp");
        return mv;
    }
}
