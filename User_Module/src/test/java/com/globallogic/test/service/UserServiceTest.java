package com.globallogic.test.service;


import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;
import com.globallogic.repository.UserRepository;
import com.globallogic.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userServiceImpl;
    private User user1=new User(1,"Kamil Khan","ab@gmail.com","kamil@123","India","English");
    private User user2=new User(2,"Omkar Yadav","abc@gmail.com","omkar@123","India","English");
    private List<User> users;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        users=new ArrayList<>();
        users.add(user1);
        users.add(user2);

    }

    @Test
    public void givenValidUserIDThenShouldReturnUser() throws UserNotFoundException {

        Optional<User> optionalEmployee = Optional.of(user1);
        when(userRepository.findById(100)).thenReturn(optionalEmployee);
        User retreivedEmployee = userServiceImpl.getUserById(100);
        assertEquals(user1.getId(),retreivedEmployee.getId(),"should return employee for valid id of exixting employee");
    }
    @Test
    public void givenInValidUserIDThenShouldThrowException() throws UserNotFoundException {
        Optional<User> optionalEmployee = Optional.empty();
        when(userRepository.findById(100)).thenReturn(optionalEmployee);
        assertThrows(UserNotFoundException.class,()->{userServiceImpl.getUserById(100);});
    }
//    @Test
//    public void givenNewUserWhenSavedShouldReturnUser() throws UserAlreadyExistException {
//
//        Optional<User> optionalUser = Optional.empty();
//        when(userRepository.findById(1)).thenReturn(optionalUser);
//        User addedUser = userServiceImpl.addUser(user1);
//        verify(userRepository,times(1)).findById(1);
//        verify(userRepository,times(1)).save(user1);
//    }
//    @Test
//    public void givenDuplicateUserWhenSavedShouldThrowException() throws UserAlreadyExistException {
//
//        Optional<User> optionalEmployee = Optional.of(user1);
//        when(userRepository.findById(1)).thenReturn(optionalEmployee);
//        assertThrows(UserAlreadyExistException.class,()->{userServiceImpl.addUser(user1);});
//        verify(userRepository,times(1)).findById(1);
//        verify(userRepository,times(0)).save(user1);
//    }
    @Test
    public void testAddUser(){

        userRepository.save(user1);
        verify(userRepository,times(1)).save(user1);
    }
    @Test
    public void testListOfUsers(){

        when(userRepository.findAll()).thenReturn(users);
        List<User> result=userServiceImpl.getAllUser();
        verify(userRepository).findAll();
    }
    @Test
    public void testGetUserById() throws UserNotFoundException {

        given(userRepository.findById(1)).willReturn(Optional.of(user1));

        Optional<User> object= Optional.ofNullable(userServiceImpl.getUserById(1));
    }
    @Test
    public void testDeleteUserById() throws UserNotFoundException {
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        userServiceImpl.deleteUserById(2);
        verify(userRepository).deleteById(2);

    }
    @Test
    public void testUpdateUser() throws UserNotFoundException {

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        when(userRepository.save(user1)).thenReturn(user1);

        user1.setCountry("france");

        User user = userServiceImpl.updateUser(user1);

        verify(userRepository, times(1)).save(user1);

    }



}