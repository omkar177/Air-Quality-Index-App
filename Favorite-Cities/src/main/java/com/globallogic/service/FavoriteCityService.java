package com.globallogic.service;

import com.globallogic.entity.FavoriteCities;
import com.globallogic.repository.FavoriteCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FavoriteCityService {

    @Autowired
    private FavoriteCityRepository favoriteCityRepository;


    public FavoriteCities addNewCityToFavorite(FavoriteCities city) {
        return favoriteCityRepository.save(city);
    }
}
