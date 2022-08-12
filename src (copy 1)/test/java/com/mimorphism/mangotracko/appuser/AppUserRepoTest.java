package com.mimorphism.mangotracko.appuser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
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
