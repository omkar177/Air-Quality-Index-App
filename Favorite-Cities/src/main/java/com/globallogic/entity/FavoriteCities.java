package com.globallogic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteCities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
//    @Column(name = "City_Name")
    private String city;
//    @Column(name = "City_Air_Quality")
    private int aqius;
}

