package com.mimorphism.mangotracko.mango;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CurrentlyReadingRepo extends PagingAndSortingRepository<CurrentlyReading, Long> {
	
	public Optional<CurrentlyReading> findByMangoMangoIdAndUserId(Long mangoId, Long userId);    
	
	public List<CurrentlyReading> findByUserId(Long userId);
	
    public List<CurrentlyReading> findByUserId(Long userId, Pageable settings);
    
    public Page<CurrentlyReading> findAllByUserId(Long userId, Pageable settings);
    
    @Query("SELECT b.mango.anilistId FROM CurrentlyReading b WHERE b.user.id = ?1")
    public List<Long> findMangoMangoIdByUserId(Long userId);


	
	
}