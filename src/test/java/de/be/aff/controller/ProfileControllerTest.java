package de.be.aff.controller;

import de.be.aff.model.Profile;
import de.be.aff.service.ProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ProfileControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileSrv;

    private static final String TOKEN = "anything";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(profileController)
                .build();
    }

    @Test
    public void getProfiles() throws Exception {
        Profile[] profiles = mockedProfilesArray();

        //mocking profile service method
        given(profileSrv.getProfiles(TOKEN))
                .willReturn(new ResponseEntity<>(profiles, HttpStatus.OK));

        mockMvc.perform(get("/profiles").header("Authorization", TOKEN))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(profiles.length)))
                .andExpect(jsonPath("$[0].name", is(profiles[0].getName())))
                .andExpect(jsonPath("$[1].surname", is(profiles[1].getSurname())));
    }

    private Profile[] mockedProfilesArray() {

        Profile[] profileArray = new Profile[2];
        profileArray[0] = new Profile("John", "Doe");
        profileArray[1] = new Profile("Jane", "Smith");
        return profileArray;
    }
}
