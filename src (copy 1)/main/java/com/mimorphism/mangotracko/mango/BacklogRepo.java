package com.mimorphism.mangotracko.mango;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BacklogRepo extends PagingAndSortingRepository<Backlog, Long> {
	
    public Optional<Backlog> findByMangoMangoIdAndUserId(Long mangoId, Long userId);
	public List<Backlog> findByUserId(Long userId);
    public List<Backlog> findByUserId(Long userId, Pageable settings);
    public Page<Backlog> findAllByUserId(Long userId, Pageable settings);
    
    @Query("SELECT b.mango.anilistId FROM Backlog b WHERE b.user.id = ?1")
    public List<Long> findMangoMangoIdByUserId(Long userId);

}