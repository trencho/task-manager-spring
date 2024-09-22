package com.project.taskmanager;

import com.project.taskmanager.controller.AuthController;
import com.project.taskmanager.dto.UserLoginDTO;
import com.project.taskmanager.dto.UserRegistrationDTO;
import com.project.taskmanager.entity.User;
import com.project.taskmanager.mapper.UserMapper;
import com.project.taskmanager.security.JwtTokenProvider;
import com.project.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldRegisterUserSuccessfully() {
        final var userRegistrationDTO = new UserRegistrationDTO("username", "email@example.com", "password");
        final var user = new User();
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setPassword("password");

        when(userMapper.toEntity(userRegistrationDTO)).thenReturn(user);
        doNothing().when(userService).registerUser(user);

        final var response = authController.register(userRegistrationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenRegisterUserFails() {
        final var userRegistrationDTO = new UserRegistrationDTO("username", "email@example.com", "password");
        when(userMapper.toEntity(userRegistrationDTO)).thenThrow(new IllegalArgumentException("User already exists"));

        final var response = authController.register(userRegistrationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void shouldLoginSuccessfully() {
        final var userLoginDTO = new UserLoginDTO("username", "password");

        final var authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("mocked-jwt-token");

        final var response = authController.login(userLoginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-jwt-token", response.getBody());
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() {
        final var userLoginDTO = new UserLoginDTO("username", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        final var response = authController.login(userLoginDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

}
