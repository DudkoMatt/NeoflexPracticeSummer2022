package ru.dudkomv.neoflexpractice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(
        username = "admin",
        password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
        authorities = {"ROLE_ADMIN"}
)
class UserControllerTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/user";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUsernameById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk())
                .andExpect(content().string("admin"));

        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("user"));
    }
}