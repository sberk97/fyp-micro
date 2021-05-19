package com.berk.zuulserver.service;

import com.berk.zuulserver.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "userService")
public class MyUserDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MyUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return user.map(MyUserDetails::new).get();
    }

    public boolean userExists(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent();
    }

    public void saveUser(RegisterUser user) {
        var newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setRoles("ROLE_USER");
        newUser.setActive(true);
        userRepository.save(newUser);
    }

    public Optional<ReturnUserDetails> getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(this::userToUserDetails);
    }

    public Optional<ReturnUserDetails> getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::userToUserDetails);
    }

    public Optional<List<ReturnUserDetails>> getUsers() {
        List<User> userList = userRepository.findAll();
        List<ReturnUserDetails> userDetails = new ArrayList<>();
        for (User u : userList) {
            userDetails.add(userToUserDetails(u));
        }
        return userList.isEmpty() ? Optional.empty() : Optional.of(userDetails);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    private ReturnUserDetails userToUserDetails(User user) {
        var userReturnData = new ReturnUserDetails();
        userReturnData.setUsername(user.getUsername());
        userReturnData.setId(user.getId());
        userReturnData.setRoles(user.getRoles().split(","));

        return userReturnData;
    }
}
