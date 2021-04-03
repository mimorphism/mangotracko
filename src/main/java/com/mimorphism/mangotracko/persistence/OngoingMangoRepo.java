package com.mimorphism.mangotracko.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mimorphism.mangotracko.model.Mango;
import com.mimorphism.mangotracko.model.OngoingMango;


public interface OngoingMangoRepo extends CrudRepository<OngoingMango, Long> {
	
    public OngoingMango findByMangoTitle(String mangoTitle);
    

}
