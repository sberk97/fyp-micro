package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.AuthenticationRequest;
import com.berk.zuulserver.model.AuthenticationResponse;
import com.berk.zuulserver.model.ReturnUserDetails;
import com.berk.zuulserver.service.MyUserDetailsService;
import com.berk.zuulserver.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtTokenUtil;

    @Resource(name = "userService")
    private MyUserDetailsService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest loginUser)
            throws AuthenticationException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUser.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails user = userService.loadUserByUsername(loginUser.getUsername());
            final String token = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username or password.");
        }
    }
}
