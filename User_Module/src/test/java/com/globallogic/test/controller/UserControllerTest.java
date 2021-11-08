package com.globallogic.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.controller.UserController;
import com.globallogic.entity.User;
import com.globallogic.exceptions.UserAlreadyExistException;
import com.globallogic.exceptions.UserNotFoundException;
import com.globallogic.service.UserServiceImpl;
import com.globallogic.utility.JWTUtility;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class UserControllerTest {

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJra0BnbWFpbC5jb20iLCJleHAiOjE2MzYzNzA5MjksImlhdCI6MTYzNjM1MjkyOX0.dSU3gexSXJtPjqbGb5tFbVUQ8DcC5r3XaKiwiD_OyPq2MldLhLtQDdK1NGeen4ewKEm5vF685shXgecWFoWqzA";
    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    JWTUtility jwtTokenUtil;

    private static final ObjectMapper mapper = new ObjectMapper();


    private final User user1 = new User(1, "Kamil Khan", "ab@gmail.com", "kamil@123", "India", "English");
    private final User user2 = new User(2, "Omkar Yadav", "abc@gmail.com", "omkar@123", "India", "English");

    private List<User> users;

    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(null);

        when(jwtTokenUtil.getEmailFromToken(any(String.class))).thenReturn(user1.getEmail());
        when(jwtTokenUtil.validateToken(any(String.class), any(UserDetails.class))).thenReturn(true);
        when(userService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
    }


    @Test
    public void givenUserToSaveThenShouldReturnSavedUser() throws Exception {
        Mockito.when(userService.addUser(user1)).thenReturn(user1);
        String json = mapper.writeValueAsString(user1);
        mockMvc.perform(post("/users/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Kamil Khan")));
    }

    @Test
    public void givenDuplicateUserWhenPostThenReturnErrorMessage() throws Exception {
        when(userService.addUser(user1)).thenThrow(UserAlreadyExistException.class);
        String json = mapper.writeValueAsString(user1);
        mockMvc.perform(post("/users/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("User Already Exist"));
    }

    @Test
    public void givenGetAllUsersThenShouldReturnListOfAllUsers() throws Exception {
        Mockito.when(userService.getAllUser()).thenReturn(users);
        mockMvc.perform(get("/users/getAll").header(AUTHORIZATION, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Kamil Khan")));
    }

    @Test
    public void givenUserIdThenShouldReturnRespectiveBlog() throws Exception {
        Mockito.when(userService.getUserById(user1.getId())).thenReturn(user1);
        mockMvc.perform(get("/users/get/1").header(AUTHORIZATION, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("Kamil Khan")));
    }

    @Test
    public void givenInValidUserIdToGetUserThenReturnErrorMessage() throws Exception {
        when(userService.getUserById(anyInt())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(get("/users/get/100").header(AUTHORIZATION, TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User Not Found"));
    }

    @Test
    public void givenValueToUpdateThenShouldReturnUpdatedUser() throws Exception {
        user1.setLanguage("English,Hindi");
        Mockito.when(userService.updateUser(any(User.class))).thenReturn(user1);
        String json = mapper.writeValueAsString(user1);
        mockMvc.perform(put("/users/update")
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Kamil Khan")));
    }

    @Test
    public void givenInValidUserToUpdateThenReturnErrorMessage() throws Exception {
        String json = mapper.writeValueAsString(user1);
        when(userService.updateUser(any(User.class))).thenThrow(UserNotFoundException.class);
        mockMvc.perform(put("/users/update")
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User Not Found"));
    }


    @Test
    public void givenUserIdToDeleteThenShouldReturnDeletedBlog() throws Exception {
        Mockito.when(userService.deleteUserById(user1.getId())).thenReturn(user1);
        mockMvc.perform(delete("/users/delete/1").header(AUTHORIZATION, TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("Kamil Khan")));
    }

    @Test
    public void givenInValidUserIdToDeleteThenReturnErrorMessage() throws Exception {
        when(userService.deleteUserById(anyInt())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(delete("/users/delete/100").header(AUTHORIZATION, TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User Not Found"));
    }


//    @Test
//    public void givenUserEmailAndPasswordThenShouldGenerateToken() throws Exception {
//       mockMvc.perform(post("/users/login"))
//               .andExpect()
//               .andExpect(status().isOk());
//    }

    @Test
    public void givenJwtTokenThenShouldReturnHomePage() throws Exception {
        mockMvc.perform(get("/users/home").header(AUTHORIZATION, TOKEN))
                .andExpect(content().string("Welcome to the Air Pollution App"));
    }

}
