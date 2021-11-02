package com.globallogic.controller;



import com.globallogic.entity.airindex.CityAndAirIndex;
import com.globallogic.entity.cities.CityList;
import com.globallogic.entity.cities.DataCities;
import com.globallogic.entity.states.StateList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favcity")
@Slf4j
public class FavCityController {

    @Value("${api.key}")
    private String apikey;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/states/{country}")
    public StateList getStates(@PathVariable String country){

        StateList data= restTemplate.getForObject(
                "http://api.airvisual.com/v2/states?country="+country+"&key="+apikey, StateList.class);
        return data;

    }

    @GetMapping("/cities/{state},{country}")
    public CityList getCities(@PathVariable String state, @PathVariable String country){

        CityList cities=restTemplate.getForObject(
                "http://api.airvisual.com/v2/cities?state="+state+"&country="+country+"&key="+apikey, CityList.class);
        return cities;

    }

    @GetMapping("/airqs/{city},{state},{country}")
    public CityAndAirIndex getCities(@PathVariable String city, @PathVariable String state, @PathVariable String country)
    throws InvocationTargetException{

        CityAndAirIndex cityAndAirIndex = restTemplate.getForObject(
                "http://api.airvisual.com/v2/city?city="+city+"&state="+state+"&country="+country+"&key="+apikey
                ,CityAndAirIndex.class);
        return  cityAndAirIndex;

    }

    @GetMapping("/airqs/{state},{country}")
    public List<CityAndAirIndex> getAirIndexOfAllCities(@PathVariable String state, @PathVariable String country) throws Exception {
        //List for adding cities
        List<DataCities> allCities = new ArrayList<>();
        //Handling exception for api server issues
        try {
            CityList cities = restTemplate.getForObject(
                    "http://api.airvisual.com/v2/cities?state=" + state + "&country=" + country + "&key=" + apikey, CityList.class);

            allCities.addAll(cities.getData());
        }
        catch (Exception e){
            throw new Exception();
        }
        //List that contains city and air index
        List<CityAndAirIndex> list = new ArrayList<>();
        //incase of an empty list the flow will be stopped
        if(allCities.isEmpty() || allCities==null){
            System.out.println("No Cities Found");
        }
        else {
            for (DataCities cityData : allCities) {
                CityAndAirIndex cityAndAirIndex = restTemplate.getForObject(
                        "http://api.airvisual.com/v2/city?city=" + cityData.getCity() + "&state=" + state + "&country=" + country + "&key=" + apikey
                        , CityAndAirIndex.class);
                //All airIndex of city to list\
                list.add(cityAndAirIndex);
            }

        }
        return list;
    }
}
