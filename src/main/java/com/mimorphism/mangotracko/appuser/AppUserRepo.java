package com.mimorphism.mangotracko.appuser;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepo
        extends PagingAndSortingRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE AppUser a SET a.enabled = TRUE WHERE a.username = ?1")
    int enableAppUser(String username);
    
}