package com.mimorphism.mangotracko.mango;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.appuser.AppUserRepo;
import com.mimorphism.mangotracko.appuser.AppUserRole;

/**
 * SEEMS LIKE AN UNNECESSARY TEST SUITE, BUT 
 * SINCE THESE METHODS ARE INTERFACE METHODS DERIVED FROM NAME,
 * I GUESS IT'S OKAY TO TEST
 * 
 * @author mimo
 * @version 1.0
 * @since Jul 6, 2022
 */
//@SpringBootTest(classes = {BacklogRepoTest.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class BacklogRepoTest {
	
	@Autowired
	private BacklogRepo testBacklogRepo;
	
	@Autowired
	private  MangoRepo testMangoRepo;
	
	@Autowired
	private  AppUserRepo testAppUserRepo;
		
	private  Mango testMango;
	private  AppUser testUser;
	
	@BeforeEach
	  void setup() {
		testMango = new Mango();
		testMango.setAnilistId(Long.valueOf(123));
		testMango.setAuthor("ALI");
		testMango.setMangoId(Long.valueOf(123));
		testMango.setMangoTitle("NARUTO");
		testMango.setStatus(MangoStatus.FINISHED);
		
		
		testUser = new AppUser();
		testUser.setAppUserRole(AppUserRole.ADMIN);
		testUser.setEnabled(true);
		testUser.setId(Long.valueOf(123));
		testUser.setLocked(false);
		testUser.setPassword("YOYOYO");
		testUser.setUsername("ABC123");
		
		testMangoRepo.save(testMango);
		testAppUserRepo.save(testUser);
	}
	
	@AfterEach
	void tearDown() {
		testMangoRepo.deleteAll();
		testAppUserRepo.deleteAll();
	}
	

	@Test
	void testFindByMangoMangoIdAndUserId_ShouldReturnBacklog() {
		
		//save mango and user required for the test
		Optional<AppUser> user = testAppUserRepo.findByUsername("ABC123");
		Optional<Mango> mango = testMangoRepo.findByMangoTitle("NARUTO");

		//given
		Backlog testBacklog = new Backlog();
		testBacklog.setAddedDateTime("2014-02-12T12:45");
		testBacklog.setBacklogId(Long.valueOf(123));
		testBacklog.setUser(user.get());

		testBacklog.setMango(mango.get());
		
		//when saved
		testBacklogRepo.save(testBacklog);
		
		Optional<Backlog> savedTestBacklog = testBacklogRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), 
				user.get().getId());
		
		assertThat(savedTestBacklog).isNotEmpty().get().isEqualTo(testBacklog);
		
	}

	@Test
	void testFindByUserId_ShouldReturnBacklogList() {
		Optional<AppUser> user = testAppUserRepo.findByUsername("ABC123");
		Optional<Mango> mango = testMangoRepo.findByMangoTitle("NARUTO");
		
		//given
				Backlog testBacklog = new Backlog();
				testBacklog.setAddedDateTime("2014-02-12T12:45");
				testBacklog.setBacklogId(Long.valueOf(123));
				testBacklog.setUser(user.get());

				testBacklog.setMango(mango.get());
				
				//when saved
				testBacklogRepo.save(testBacklog);

		
		List<Backlog> backlogs = testBacklogRepo.findByUserId(user.get().getId());

		assertThat(backlogs).hasSize(1);
	}

}
