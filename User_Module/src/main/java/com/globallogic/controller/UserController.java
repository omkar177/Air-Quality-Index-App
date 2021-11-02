package com.globallogic.controller;

import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;
import com.globallogic.security.JwtTokenGenerator;
import com.globallogic.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtTokenGenerator tokenGenerator;


    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user){
    try{
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }
    catch (UserAlreadyExistException userAlreadyExistException){
        return new ResponseEntity("User Already Exist", HttpStatus.CONFLICT);
    }

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> userList=userService.getAllUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") int userId) {
    try {
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }
    catch (UserNotFoundException userNotFoundException){
        return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
    }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        try {
            User updatedUser = userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        catch (UserNotFoundException userNotFoundException){
            return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id){
        try{
            return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
        }
        catch (UserNotFoundException userNotFoundException) {
            return new ResponseEntity("User Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user){
        String email=user.getEmail();
        String password=user.getPassword();
        User registereduser= userService.findByemailAndPassword(email,password);

        String token=tokenGenerator.generateToken(registereduser);
        return new ResponseEntity<>(token,HttpStatus.ACCEPTED);

    }






}
