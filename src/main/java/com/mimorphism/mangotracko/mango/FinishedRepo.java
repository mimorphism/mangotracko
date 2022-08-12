package com.mimorphism.mangotracko.mango;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FinishedRepo extends PagingAndSortingRepository<Finished, Long> {
	
    public Optional<Finished> findByMangoMangoIdAndUserId(Long mangoId, Long userId);
    
    public List<Finished> findByUserId(Long userId);
    
    public List<Finished> findByUserId(Long userId, Pageable settings);
    
    public Page<Finished> findAllByUserId(Long userId, Pageable settings);
    
    @Query("SELECT b.mango.anilistId FROM Finished b WHERE b.user.id = ?1")
    public List<Long> findMangoMangoIdByUserId(Long userId);

    
    

    
    
    
}