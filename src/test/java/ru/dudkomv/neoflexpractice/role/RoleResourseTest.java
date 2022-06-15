package ru.dudkomv.neoflexpractice.role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RoleResourseTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/lesson";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getAllByUsername_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk());

        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getAllByUsername_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk());

        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUsername_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isUnauthorized());
    }
}