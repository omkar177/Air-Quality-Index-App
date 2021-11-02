package com.globallogic.entity.cities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class CityList {
    List<DataCities> data;
}
