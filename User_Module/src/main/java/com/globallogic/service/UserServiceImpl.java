package com.globallogic.service;

import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;
import com.globallogic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) throws UserAlreadyExistException {
        if(userRepository.findById(user.getId()).isPresent())
        {
            throw new UserAlreadyExistException();
        }
        return userRepository.save(user);
    }

    public List<User> getAllUser() {

        return userRepository.findAll();
    }

    public User getUserById(int id) throws UserNotFoundException {

        Optional optional=userRepository.findById(id);
        if(optional.isPresent()) {
            return (User)optional.get();
        }
        else{
          throw new UserNotFoundException();
        }

    }

    public User updateUser(User user) throws UserNotFoundException {
        Optional optional=userRepository.findById(user.getId());
        if(optional.isPresent()) {
            return userRepository.save(user);
        }
        else{
            throw new UserNotFoundException();
        }
    }

    public User deleteUserById(int id) throws  UserNotFoundException {
        User user;
        Optional optional = userRepository.findById(id);
        if (optional.isPresent()) {
            user = (User) optional.get();
            userRepository.deleteById(id);
            return user;
        }
        else{
            throw new UserNotFoundException();
        }

    }

    public User findByemailAndPassword(String email,String password) {

        return userRepository.findByemailAndPassword(email, password);
    }
}
