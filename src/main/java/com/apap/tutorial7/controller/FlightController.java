package com.apap.tutorial7.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.apap.tutorial7.model.FlightModel;
import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.service.FlightService;
import com.apap.tutorial7.service.PilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * FlightController
 */

@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private PilotService pilotService;

    @PostMapping(value = "/add")
    private FlightModel addFlightSubmit(@RequestBody FlightModel flight) {
        return flightService.addFlight(flight);
    }

    @GetMapping(value = "/view/{flightNumber}")
    private FlightModel flightView(@PathVariable("flightNumber") String flightNumber) {
        FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        return flight;
    }

    @GetMapping(value = "/all")
    private List<FlightModel> flightViewAll() {
        List<FlightModel> flights = flightService.getAllFlight();
        return flights;
    }
    @DeleteMapping(value = "/delete")
    private String deletePilot(@PathVariable("flightId") long flightId) {
        Optional<FlightModel> flight = flightService.getFlightDetailById(flightId);
        flightService.deleteByFlightNumber(flight.get().getFlightNumber());
        return "success";
    }

    @PutMapping(value="/update/{flightId}")
    public String updatePilotSubmit(@PathVariable("flightId") long flightId,
                                    @RequestParam(value = "destination", required = false) String destination,
                                    @RequestParam(value = "origin", required = false) String origin,
                                    @RequestParam(value = "time", required = false) Date time){
        FlightModel flight = flightService.getFlightDetailById(flightId).get();
        if(flight.equals(null)){
            return "Couldn't find the flight";
        }

        if (destination != null) {
            flight.setDestination(destination);
        }
        if (origin != null) {
            flight.setOrigin(origin);
        }
        if (time != null) {
            flight.setTime(time);
        }
        flightService.addFlight(flight);
        return "update";
    }

}