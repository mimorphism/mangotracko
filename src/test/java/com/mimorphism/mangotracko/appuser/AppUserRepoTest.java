package com.mimorphism.mangotracko.appuser;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AppUserRepoTest {

	@Autowired
	private AppUserRepo testAppUserRepo;

	@Test
	void userShouldBeEnabled() {

		AppUser testUser = new AppUser();
		testUser.setAppUserRole(AppUserRole.ADMIN);
		testUser.setEnabled(false);// SET FALSE
		testUser.setId(Long.valueOf(123));
		testUser.setLocked(false);
		testUser.setPassword("YOYOYO");
		testUser.setUsername("ABC123");

		testAppUserRepo.save(testUser);
		testAppUserRepo.enableAppUser("ABC123");

		Optional<AppUser> savedUser = testAppUserRepo.findByUsername("ABC123");
		
		assertThat(savedUser.get().getEnabled()).isEqualTo(true);
	}

}
