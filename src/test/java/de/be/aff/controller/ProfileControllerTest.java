package de.be.aff.controller;

import de.be.aff.model.Profile;
import de.be.aff.service.ProfileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest //if we need springboot support ( ie to load application.properties)
@RunWith(SpringRunner.class) // SpringRunner is the new name for SpringJUnit4ClassRunner
@WebMvcTest(ProfileController.class)//this is instead
                                    // @injectMocks private ProfileController profileController;
                                    //
                                    //    @Before
                                    //    public void init() {
                                    //        MockitoAnnotations.initMocks(this);
                                    //        mockMvc = MockMvcBuilders
                                    //                .standaloneSetup(profileController)
                                    //                .build();
                                    //    }
                                    //additionally, when we use this above, we do not put
                                    //@Autowired on private MockMvc mockMvc;as it's initialized in init() method
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //Injecting ProfileService mock into ApplicationContext as a bean
    private ProfileService profileSrv;

    private static final String TOKEN = "anything";

    //code is only to show testing techniques, it doesn't test all the possible scenarios
    @Test
    public void getProfiles() throws Exception {
        Profile[] profiles = mockedProfilesArray();

        //mocking profile service method
        given(this.profileSrv.getProfiles(TOKEN))
                .willReturn(new ResponseEntity<>(profiles, HttpStatus.OK));

        this.mockMvc.perform(get("/profiles").header("Authorization", TOKEN))
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
