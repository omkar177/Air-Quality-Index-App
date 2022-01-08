package com.globallogic.controller;

import com.globallogic.entity.FavoriteCities;
import com.globallogic.service.UserFavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class UserFavController {

    @Autowired
    private UserFavService userFavService;


    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        List<FavoriteCities> list=userFavService.getAll();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
