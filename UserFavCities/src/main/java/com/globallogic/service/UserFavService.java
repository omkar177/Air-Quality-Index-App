package com.globallogic.service;

import com.globallogic.entity.FavoriteCities;
import com.globallogic.repository.UserFavRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavService {

    @Autowired
    private UserFavRepository userFavRepository;

    public List<FavoriteCities> getAll() {
        return userFavRepository.findAll();
    }
}
