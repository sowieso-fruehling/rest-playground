package de.be.aff.service;


import de.be.aff.model.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Override
    public ResponseEntity<Profile[]> getProfiles(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = setAuthorizationHeader(token);
        return restTemplate
                .exchange("rest endppoint url", HttpMethod.GET, entity, Profile[].class);
    }

    private HttpEntity<String> setAuthorizationHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return new HttpEntity<>("parameters", headers);
    }
}
