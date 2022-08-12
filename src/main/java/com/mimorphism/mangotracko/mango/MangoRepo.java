package com.mimorphism.mangotracko.mango;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface MangoRepo extends CrudRepository<Mango, Long> {
	
    public Optional<Mango> findByMangoTitle(String mangoTitle);
    
    public Optional<Mango> findByAnilistId(Long anilistId);
    
    public List<Mango> findAll();
    
}