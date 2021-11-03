package com.globallogic.service;

import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService  {
     User addUser(User user) throws UserAlreadyExistException;

     List<User> getAllUser();

     User getUserById(int userId) throws UserNotFoundException;

     User updateUser(User user) throws UserNotFoundException;

     User deleteUserById(int userId) throws UserAlreadyExistException, UserNotFoundException;


}
