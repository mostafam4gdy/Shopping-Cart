package com.reljicd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginStylesheetsAreAvailableBeforeAuthentication() throws Exception {
        mockMvc.perform(get("/css/main.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));

        mockMvc.perform(get("/webjars/bootstrap/3.3.7/css/bootstrap.min.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));
    }

    @Test
    void invalidCredentialsReturnToLoginAndShowError() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "invalid-user")
                        .param("password", "invalid-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));

        mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Invalid username and password.")));
    }
}
