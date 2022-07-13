package com.artplan.pets.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.LoginRequest;
import com.artplan.pets.entity.User;
import com.artplan.pets.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    
    public static final String REGISTER_SUCCESSFULY = "User registered successfully";
    public static final String LOGIN_SUCCESSFULY = "Authentication successfully";
    public static final String LOGOUT_SUCCESSFULY = "Successful logout";

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        userService.addUserWithDefaultRole(new User(loginRequest.getUsername(), passwordEncoder.encode(loginRequest.getPassword())));
        authenticateUserAndSetSession(request, loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, REGISTER_SUCCESSFULY));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        authenticateUserAndSetSession(request, loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, LOGIN_SUCCESSFULY));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request, HttpServletResponse resource) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, resource, null);
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, LOGOUT_SUCCESSFULY));
    }

    private void authenticateUserAndSetSession(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        request.getSession();
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
