package com.globallogic.service;

import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;
import com.globallogic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public User addUser(User user) throws UserAlreadyExistException {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setLanguage(user.getLanguage());
        newUser.setCountry(user.getCountry());
        User user1 = userRepository.save(newUser);

        return user1;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByemail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
