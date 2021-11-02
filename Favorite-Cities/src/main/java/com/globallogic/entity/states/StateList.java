package com.globallogic.entity.states;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class StateList
{
    List<DataStates> data;
}
