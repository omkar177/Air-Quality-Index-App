package com.globallogic.entity.airindex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAirIndex {

    private String city;
    private Current current;
}
