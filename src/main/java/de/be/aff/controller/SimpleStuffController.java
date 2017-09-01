package de.be.aff.controller;

import de.be.aff.model.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SimpleStuffController {

    @RequestMapping("/matches")
    public List<Profile> getMatches() {
        List<Profile> list = new ArrayList<>();
        list.add(new Profile("John", "Doe"));
        list.add(new Profile("Jane", "Smith"));
        return list;
    }

    @RequestMapping(value = "/match/{id}")
    public Profile getMatchUsingPathVariable(@PathVariable Integer id) {
        return new Profile("John", "Doe");
    }

    @RequestMapping(value = "/match" )
    public Profile getMatchUsingRequestParameter(@RequestParam Integer id) {
        return new Profile("John", "Doe");
    }
}
