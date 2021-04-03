package com.mimorphism.mangotracko.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mimorphism.mangotracko.model.Mango;

public interface MangoRepo extends CrudRepository<Mango, Long> {
	
    public Mango findByMangoTitle(String mangoTitle);
    
}