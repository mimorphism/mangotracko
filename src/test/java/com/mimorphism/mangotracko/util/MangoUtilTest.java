package com.mimorphism.mangotracko.util;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.appuser.AppUserRole;
import com.mimorphism.mangotracko.exception.BadRequestException;
import com.mimorphism.mangotracko.mango.Backlog;
import com.mimorphism.mangotracko.mango.CurrentlyReading;
import com.mimorphism.mangotracko.mango.Finished;
import com.mimorphism.mangotracko.mango.Mango;
import com.mimorphism.mangotracko.mango.MangoStatus;
import com.mimorphism.mangotracko.mango.dto.BacklogDTO;
import com.mimorphism.mangotracko.mango.dto.CurrentlyReadingDTO;
import com.mimorphism.mangotracko.mango.dto.FinishedDTO;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.CoverImage;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Data;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Media;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Name;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Node;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Staff;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Title;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MangoUtilTest {

	private MessageSource messageSource;

	private MangoUtil mangoUtilTest;
	
	private AppUser testUser;
	private Mango testMango;

	@BeforeAll
	void setUp() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setDefaultLocale(new Locale("en"));

		mangoUtilTest = new MangoUtil(messageSource);
		
		
		
		testUser = new AppUser();
		testUser.setAppUserRole(AppUserRole.ADMIN);
		testUser.setEnabled(true);
		testUser.setId(Long.valueOf(123));
		testUser.setLocked(false);
		testUser.setPassword("YOYOYO");
		testUser.setUsername("ABC123");

		testMango = new Mango();
		testMango.setAnilistId(Long.valueOf(123));
		testMango.setAuthor("ALI");
		testMango.setMangoId(Long.valueOf(123));
		testMango.setMangoTitle("NARUTO");
		testMango.setStatus(MangoStatus.FINISHED);
		testMango.setLastChapter(125);

	}

	@Test
	void givenNameIsPresent_whenConvertProperName_SuccesfullyReturns() {

		String name = "Masashi Kishimoto";
		String expected = mangoUtilTest.convertProperName(name);
		assertThat(expected).isEqualTo("Kishimoto Masashi");
	}

	@Test
	void givenMangoInfoWithMangoStatusFinished_whenSetMangoInfo_returnsMangoSuccesfully() {

		MangoInfo mangoInfoMock = new MangoInfo();

		Title title = new Title();
		title.setRomaji("Naruto");

		CoverImage coverImage = new CoverImage();
		coverImage.setLarge("");

		Staff staff = new Staff();
		staff.setNodes(new ArrayList<Node>());
		staff.getNodes().add(new Node());
		staff.getNodes().get(0).setName(new Name());
		staff.getNodes().get(0).getName().setFull("Masashi Kishimoto");

		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(123L);
		mangoInfoMock.getData().getMedia().setLastChapter(125);
		mangoInfoMock.getData().getMedia().setStatus(MangoStatus.FINISHED);
		mangoInfoMock.getData().getMedia().setTitle(title);

		mangoInfoMock.getData().getMedia().setCoverImage(coverImage);
		mangoInfoMock.getData().getMedia().setBannerImage("");

		mangoInfoMock.getData().getMedia().setStaff(staff);

		Mango expected = mangoUtilTest.setMangoInfo(mangoInfoMock);

		assertThat(expected.getAnilistId()).isEqualTo(123L);
		assertThat(expected.getAuthor()).isEqualTo("Kishimoto Masashi");
		assertThat(expected.getMangoTitle()).isEqualTo("Naruto");
		assertThat(expected.getStatus()).isEqualTo(MangoStatus.FINISHED);

	}
	
	@Test
	void givenMangoInfoWithNonFinishedMangoStatus_whenSetMangoInfo_returnsMangoSuccesfully() {

		MangoInfo mangoInfoMock = new MangoInfo();

		Title title = new Title();
		title.setRomaji("Naruto");

		CoverImage coverImage = new CoverImage();
		coverImage.setLarge("");

		Staff staff = new Staff();
		staff.setNodes(new ArrayList<Node>());
		staff.getNodes().add(new Node());
		staff.getNodes().get(0).setName(new Name());
		staff.getNodes().get(0).getName().setFull("Masashi Kishimoto");

		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(123L);
		mangoInfoMock.getData().getMedia().setLastChapter(null);
		mangoInfoMock.getData().getMedia().setStatus(MangoStatus.CANCELLED);
		mangoInfoMock.getData().getMedia().setTitle(title);

		mangoInfoMock.getData().getMedia().setCoverImage(coverImage);
		mangoInfoMock.getData().getMedia().setBannerImage("");

		mangoInfoMock.getData().getMedia().setStaff(staff);

		Mango expected = mangoUtilTest.setMangoInfo(mangoInfoMock);

		assertThat(expected.getAnilistId()).isEqualTo(123L);
		assertThat(expected.getAuthor()).isEqualTo("Kishimoto Masashi");
		assertThat(expected.getMangoTitle()).isEqualTo("Naruto");
		assertThat(expected.getStatus()).isEqualTo(MangoStatus.CANCELLED);

	}

	@Test
	void givenValidMessageKey_whenGetMessage_ReturnsCorrectMessage() {
		String messageKey = "MangoService.MANGO_ALREADY_EXIST_FOR_USER";
		String msg = mangoUtilTest.getMessage(messageKey);
		assertThat(msg).isEqualTo("Mango %s already exists in user's mango list");
	}

	@Test
	void givenInvalidMessageKey_whenGetMessage_throwsNoSuchMessageException() {
		String messageKey = "MangoService.MANGO_ALREADY_EXIFOR_USER";
		String msg = mangoUtilTest.getMessage(messageKey);

		assertThat(msg).isEqualTo(String.format("No message is found with message key: %s", messageKey));
	}
	
	@Test
	void canGenerateBacklog() {
		BacklogDTO requestData = new BacklogDTO("Naruto", "2022-12-12T12:45", "BIGBOSS", 123L);
		Backlog expected = mangoUtilTest.generateBacklog(requestData, testUser, testMango);
		
		assertThat(expected.getMango()).isEqualTo(testMango);
		assertThat(expected.getBacklogId()).isEqualTo(123123L);
		assertThat(expected.getAddedDateTime()).isEqualTo("2022-12-12T12:45");
	}
	
	@Test
	void canGenerateFinished() {
		String completionDateTime = "2022-12-11T12:45";
		String remarks = "YOYOYO";
		FinishedDTO requestData = new FinishedDTO(testMango.getMangoTitle(), completionDateTime, remarks,
				testUser.getUsername(), testMango.getAnilistId());
		
		Finished expected = mangoUtilTest.generateFinished(requestData, testUser, testMango);
		
		assertThat(expected.getMango()).isEqualTo(testMango);
		assertThat(expected.getFinishedId()).isEqualTo(123123L);
		assertThat(expected.getCompletionDateTime()).isEqualTo("2022-12-11T12:45");
	}
	
	@Test
	void canGenerateCurrentlyReading() {
		
		String lastReadTime = "2022-12-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, lastReadTime,
				testUser.getUsername(), 123L, null);
		
		CurrentlyReading expected = mangoUtilTest.generateCurrentlyReading(requestData, testUser, testMango);
		
		assertThat(expected.getMango()).isEqualTo(testMango);
		assertThat(expected.getCurrentlyReadingId()).isEqualTo(123123L);
		assertThat(expected.getLastReadTime()).isEqualTo("2022-12-11T12:45");
	}

}
