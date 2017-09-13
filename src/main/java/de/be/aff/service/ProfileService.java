package de.be.aff.service;


import de.be.aff.model.Profile;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    ResponseEntity<Profile[]> getProfiles(String token);

}
