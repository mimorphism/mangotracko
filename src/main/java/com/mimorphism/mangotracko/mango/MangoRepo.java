package com.mimorphism.mangotracko.mango;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MangoRepo extends CrudRepository<Mango, Long> {
	
    public Mango findByMangoTitle(String mangoTitle);
    
    public List<Mango> findByMangoStatus(String mangoStatus);
    
}