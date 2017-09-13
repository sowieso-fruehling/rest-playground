package de.be.aff.controller;

import de.be.aff.model.Profile;
import de.be.aff.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor //lombok library creates constructor for fields marked as final
//this way, we're practically having constructor injection
public class ProfileController {

     private final ProfileService profileService;

    //get request example http://localhost:8080/profile/1
    @RequestMapping(value = "/profile/{id}")
    public Profile getProfileUsingPathVariable(@PathVariable Integer id) {

        return new Profile("Jane", "Smith");
    }

    //get request example http://localhost:8080/profile?id=1
    @RequestMapping(value = "/profile")
    public Profile getProfileUsingRequestParameter(@RequestParam Integer id) {

        return new Profile("John", "Doe");
    }

    //get request example http://localhost:8080/profiles
    //how to call other (non-existing) rest endpoint - reason why this method will return exception, if called
    // how to read http headers
    //this method has unit test, with ProfileService mocked
    @RequestMapping("/profiles")
    public ResponseEntity<Object> getProfiles(@RequestHeader(value = "Authorization") String token) {

        Profile[] objects;
        try {
            ResponseEntity<Profile[]> responseEntity = profileService.getProfiles(token);

            if (responseEntity != null)
                objects = responseEntity.getBody();
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (objects != null && objects.length > 0)
            return new ResponseEntity<>(Arrays.asList(objects), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ArrayList<Profile>(), HttpStatus.OK);
    }
}
