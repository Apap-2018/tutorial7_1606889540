package com.apap.tutorial7.controller;

import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.rest.PilotDetail;
import com.apap.tutorial7.rest.Setting;
import com.apap.tutorial7.service.PilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


/**
 * PilotController
 */
@RestController
@RequestMapping("/pilot")
public class PilotController {
    @Autowired
    private PilotService pilotService;

    /**@RequestMapping("/")
    private String home() {
        return "home";
    }**/

    /**@PostMapping(value = "/add", method = RequestMethod.GET)
    private String add(Model model) {
        model.addAttribute("pilot", new PilotModel());
        return "add-pilot";
    }**/

    @PostMapping(value = "/add")
    private PilotModel addPilotSubmit(@RequestBody PilotModel pilot) {
        return pilotService.addPilot(pilot);
    }

    @GetMapping(value = "/view/{licenseNumber}")
    private PilotModel pilotView(@PathVariable("licenseNumber") String licenseNumber) {
        PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
        return pilot;
    }

    @DeleteMapping(value = "/delete")
    private String deletePilot(@RequestParam("pilotId") long pilotId) {
        PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
        pilotService.deletePilotByLicenseNumber(pilot.getLicenseNumber());
        return "success";
    }

    @PutMapping(value="/update/{pilotId}")
    public String updatePilotSubmit(@PathVariable("pilotId") long pilotId,
                                    @RequestParam("name") String name,
                                    @RequestParam("flyHour") int flyHour){
        PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
        if(pilot.equals(null)){
            return "Couldn't find your pilot";
        }

        pilot.setName(name);
        pilot.setFlyHour(flyHour);
        pilotService.addPilot(pilot);
        return "update";
    }

    /**@RequestMapping(value = "/pilot/update", method = RequestMethod.GET)
    private String update(@RequestParam(value = "licenseNumber") String licenseNumber, Model model) {
        Optional<PilotModel> archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
        model.addAttribute("pilot", archive.get());
        return "update-pilot";
    }

    @RequestMapping(value = "/pilot/update", method = RequestMethod.POST)
    private @ResponseBody
    PilotModel updatePilotSubmit(@ModelAttribute PilotModel pilot, Model model) {
        pilotService.addPilot(pilot);
        return pilot;
    }**/


@Autowired
RestTemplate restTemplate;

@Bean
public RestTemplate rest(){
    return new RestTemplate();
}

@GetMapping(value="/status/{licenseNumber}")
public String getStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception{
    String path = Setting.pilotUrl + "/pilot?licenseNumber=" + licenseNumber;
    return restTemplate.getForEntity(path, String.class).getBody();
}

@GetMapping(value = "/full/{licenseNumber}")
public PilotDetail postStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception {
    String path = Setting.pilotUrl + "/pilot";
    PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
    PilotDetail detail = restTemplate.postForObject(path, pilot, PilotDetail.class);
    return detail;
}

    @GetMapping(value = "/airport/{kota}")
    public String getAirport(@PathVariable("kota") String kota) throws Exception {
        String path = Setting.airportUrl + "&term=" + kota + "&country=ID&all_airports=true";
        return restTemplate.getForEntity(path, String.class).getBody();
    }
}