package ru.dudkomv.neoflexpractice.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(
        username = "admin",
        password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
        authorities = {"ROLE_ADMIN"}
)
class RoleControllerTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/role";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        Mockito.reset(roleController);
    }

    @Test
    void getAllByUsername() throws Exception {
        Long adminId = 0L;
        Long userId = 1L;
        Long nonExistentId = -1L;

        List<Role> expectedAdminRoles = List.of(Role.of(0L, DefinedRoles.ROLE_ADMIN));
        List<Role> expectedUserRoles = List.of(Role.of(1L, DefinedRoles.ROLE_TEACHER));

        String expectedAdminRolesString = objectMapper.writeValueAsString(expectedAdminRoles);
        String expectedUserRolesString = objectMapper.writeValueAsString(expectedUserRoles);

        mockMvc.perform(get(URL_PREFIX + "/" + adminId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAdminRolesString));

        Mockito.verify(roleController).getAllByUserId(adminId);

        mockMvc.perform(get(URL_PREFIX + "/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUserRolesString));

        Mockito.verify(roleController).getAllByUserId(userId);

        mockMvc.perform(get(URL_PREFIX + "/" + nonExistentId))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        Mockito.verify(roleController).getAllByUserId(nonExistentId);
    }
}