package com.globallogic.controller;

import com.globallogic.entity.JwtRequest;
import com.globallogic.entity.JwtResponse;
import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;
import com.globallogic.service.UserServiceImpl;
import com.globallogic.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtility jwtTokenUtil;

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;


    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userDetailsService.addUser(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistException userAlreadyExistException) {
            return new ResponseEntity("User Already Exist", HttpStatus.CONFLICT);
        }
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

        private void authenticate(String email, String password) throws Exception {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            } catch (DisabledException e) {
                throw new Exception("USER_DISABLED", e);
            } catch (BadCredentialsException e) {
                throw new Exception("INVALID_CREDENTIALS", e);
            }
        }


    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> userList=userDetailsService.getAllUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") int userId) {
    try {
        return new ResponseEntity<>(userDetailsService.deleteUserById(userId), HttpStatus.OK);
    }
    catch (UserNotFoundException userNotFoundException){
        return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
    }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        try {
            User updatedUser = userDetailsService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id){
        try{
            return new ResponseEntity<>(userDetailsService.getUserById(id),HttpStatus.OK);
        }
        catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Air Pollution ";
    }
}
