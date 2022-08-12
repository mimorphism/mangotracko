package com.mimorphism.mangotracko.security.activejwtsession;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveJWTRepository extends CrudRepository<ActiveJWT, String> {
		

}
