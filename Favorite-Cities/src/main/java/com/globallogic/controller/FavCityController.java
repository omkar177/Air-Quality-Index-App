package com.globallogic.controller;


import com.globallogic.entity.FavoriteCities;
import com.globallogic.entity.airindex.CityAndAirIndex;
import com.globallogic.entity.cities.CityList;
import com.globallogic.entity.cities.DataCities;
import com.globallogic.entity.states.StateList;
import com.globallogic.service.FavoriteCityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/")
public class FavCityController {

    @Value("${api.key}")
    private String apikey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FavoriteCityService favoriteCityService;

//    String apiUrl1="http://api.airvisual.com/v2/states?country=";

    @GetMapping("states")
    public StateList getStates(@QueryParam("country") String country) {
        StateList data = restTemplate.getForObject(
                "http://api.airvisual.com/v2/states?country=" + country + "&key=" + apikey, StateList.class);
        return data;
    }

    @GetMapping("cities")
    public CityList getCities(@QueryParam("state") String state, @QueryParam("country") String country) {

        CityList cities = restTemplate.getForObject(
                "http://api.airvisual.com/v2/cities?state=" + state + "&country=" + country + "&key=" + apikey, CityList.class);
        return cities;

    }

    @GetMapping("airindex")
    public ResponseEntity<?> getAirIndexForCity(@QueryParam("city") String city, @QueryParam("state") String state, @QueryParam("country") String country) throws Exception {

        List<CityAndAirIndex> list = new ArrayList<>();
        try {
            if (city == null || city.isEmpty()) {
                List<DataCities> allCities = new ArrayList<>();
                CityList cities = restTemplate.getForObject(
                        "http://api.airvisual.com/v2/cities?state=" + state + "&country=" + country + "&key=" + apikey, CityList.class);

                allCities.addAll(cities.getData());

                for (DataCities cityData : allCities) {
                    CityAndAirIndex cityAndAirIndex = restTemplate.getForObject(
                            "http://api.airvisual.com/v2/city?city=" + cityData.getCity() + "&state=" + state + "&country=" + country + "&key=" + apikey
                            , CityAndAirIndex.class);
                    list.add(cityAndAirIndex);
                }
            } else {
                CityAndAirIndex cityAndAirIndex = restTemplate.getForObject(
                        "http://api.airvisual.com/v2/city?city=" + city + "&state=" + state + "&country=" + country + "&key=" + apikey
                        , CityAndAirIndex.class);
                list.add(cityAndAirIndex);
            }
        }catch (Exception e)
        {
           return new ResponseEntity<>("call_per_minute_limit_reached",HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PostMapping("addCity")
    public ResponseEntity<?> addCityToFavaorite(@QueryParam("city") String city,@QueryParam("state") String state, @QueryParam("country") String country){

        CityAndAirIndex cityAndAirIndex = restTemplate.getForObject(
                "http://api.airvisual.com/v2/city?city=" + city + "&state=" + state + "&country=" + country + "&key=" + apikey
                , CityAndAirIndex.class);

        String newCity=cityAndAirIndex.getData().getCity();
        int aquis=cityAndAirIndex.getData().getCurrent().getPollution().getAqius();
        FavoriteCities newFavoriteCity= new FavoriteCities();
        newFavoriteCity.setCity(newCity);
        newFavoriteCity.setAqius(aquis);
        favoriteCityService.addNewCityToFavorite(newFavoriteCity);
        return new ResponseEntity<>(newFavoriteCity,HttpStatus.CREATED);
    }
}
