package de.be.aff.controller;

import de.be.aff.model.Profile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleStuffControllerTests {
    private MockMvc mockMvc;

    private final String MATCHES_LIST_URL="/matches";
    private final String UNEXISTING_URL="/this_cant_be_an_url";
    private Profile validatorProfileJohn=new Profile("John", "Doe");
    private Profile validatorProfileJane=new Profile("Jane", "Smith");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void thatEndpointDoesntExist() throws Exception {
        mockMvc.perform(get(UNEXISTING_URL))
                .andExpect(status().isNotFound());
    }

    @Test
    public void thatEndpointExists() throws Exception {
        mockMvc.perform(get(MATCHES_LIST_URL))
                .andExpect(status().isOk());
    }

    @Test
    public void thatReturnsExactProfile() throws Exception {
        mockMvc.perform(get("/match/1"))
                .andExpect(jsonPath("$.name").value(validatorProfileJohn.getName()));
    }

    @Test
    public void thatReturnsExactProfiles() throws Exception {
        mockMvc.perform(get(MATCHES_LIST_URL))
                .andExpect(jsonPath("$.[0].name").value(validatorProfileJohn.getName()))
                .andExpect(jsonPath("$.[1].name").value(validatorProfileJane.getName()));
    }
}
