package com.globallogic.repository;

import com.globallogic.entity.FavoriteCities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCityRepository extends JpaRepository<FavoriteCities,Integer> {
}
