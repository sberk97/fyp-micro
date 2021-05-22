package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.AuthenticationRequest;
import com.berk.zuulserver.model.AuthenticationResponse;
import com.berk.zuulserver.model.MyUserDetails;
import com.berk.zuulserver.service.MyUserDetailsService;
import com.berk.zuulserver.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @Resource(name = "jwtUtlService")
    private final JwtUtil jwtTokenUtil;

    @Resource(name = "userService")
    private MyUserDetailsService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> generateToken(@RequestBody AuthenticationRequest loginUser) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final MyUserDetails user = userService.loadUserByUsername(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
