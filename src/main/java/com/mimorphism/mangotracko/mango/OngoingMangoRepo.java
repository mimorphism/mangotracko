package com.mimorphism.mangotracko.mango;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface OngoingMangoRepo extends CrudRepository<OngoingMango, Long> {
	
    public OngoingMango findByMangoTitle(String mangoTitle);
    

}
