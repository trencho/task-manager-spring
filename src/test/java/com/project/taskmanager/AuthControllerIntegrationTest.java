package com.project.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.taskmanager.dto.RefreshTokenRequestDTO;
import com.project.taskmanager.dto.UserLoginDTO;
import com.project.taskmanager.dto.UserRegistrationDTO;
import com.project.taskmanager.entity.RefreshToken;
import com.project.taskmanager.entity.User;
import com.project.taskmanager.mapper.UserMapper;
import com.project.taskmanager.security.JwtTokenProvider;
import com.project.taskmanager.service.RefreshTokenService;
import com.project.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        final var userRegistrationDTO = new UserRegistrationDTO("username", "email@example.com", "password");
        final var user = new User();
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setPassword("password");

        when(userMapper.toEntity(any(UserRegistrationDTO.class))).thenReturn(user);
        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistrationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void shouldReturnBadRequestOnFailedRegistration() throws Exception {
        final var userRegistrationDTO = new UserRegistrationDTO("username", "email@example.com", "password");

        when(userMapper.toEntity(any(UserRegistrationDTO.class)))
                .thenThrow(new IllegalArgumentException("User already exists"));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        final var userLoginDTO = new UserLoginDTO("username", "password");
        final var accessTokenString = "mocked-jwt-token";
        final var refreshTokenString = "mocked-refresh-token";

        final var authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        final var refreshToken = mock(RefreshToken.class);
        when(tokenProvider.generateAccessToken(anyString())).thenReturn(accessTokenString);
        when(refreshTokenService.createRefreshToken(anyString())).thenReturn(refreshToken);
        when(refreshToken.getToken()).thenReturn(refreshTokenString);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessTokenString))
                .andExpect(jsonPath("$.refreshToken").value(refreshTokenString));
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        final var userLoginDTO = new UserLoginDTO("username", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userLoginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void shouldReturnNewAccessTokenForValidRefreshToken() throws Exception {
        final var refreshToken = "valid-refresh-token";
        final var refreshTokenRequest = new RefreshTokenRequestDTO(refreshToken);
        final var newAccessToken = "new-access-token";

        when(refreshTokenService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);

        mockMvc.perform(post("/api/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(newAccessToken));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidRefreshToken() throws Exception {
        final var refreshToken = "invalid-refresh-token";
        final var refreshTokenRequest = new RefreshTokenRequestDTO(refreshToken);

        when(refreshTokenService.refreshAccessToken(refreshToken)).thenThrow(new RuntimeException("Refresh token not found"));

        mockMvc.perform(post("/api/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(refreshTokenRequest)))
                .andExpect(status().isUnauthorized());
    }

}
