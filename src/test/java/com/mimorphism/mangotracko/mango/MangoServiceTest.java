package com.mimorphism.mangotracko.mango;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.appuser.AppUserRole;
import com.mimorphism.mangotracko.appuser.AppUserService;
import com.mimorphism.mangotracko.exception.BadRequestException;
import com.mimorphism.mangotracko.exception.MangoAlreadyExistsException;
import com.mimorphism.mangotracko.exception.MangoNotFoundException;
import com.mimorphism.mangotracko.mango.dto.BacklogDTO;
import com.mimorphism.mangotracko.mango.dto.CurrentlyReadingDTO;
import com.mimorphism.mangotracko.mango.dto.DeleteRecordDTO;
import com.mimorphism.mangotracko.mango.dto.FinishedDTO;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Data;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.Media;
import com.mimorphism.mangotracko.util.MangoUtil;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MangoServiceTest {

	private MangoService mangoServiceTest;

	@Mock
	private MangoRepo mangoRepoTest;

	@Mock
	private MangoInfoService mangoInfoServiceTest;

	@Mock
	private FinishedRepo finishedRepoTest;

	@Mock
	private CurrentlyReadingRepo currentlyReadingRepoTest;

	@Mock
	private BacklogRepo backlogRepoTest;

	@Mock
	private MangoUtil mangoUtilTest;

	@Mock
	private AppUserService appUserServiceTest;

	private AppUser testUser;

	private Mango testMango;

	@BeforeEach
	void setUp() {

		mangoServiceTest = new MangoService(mangoRepoTest, mangoInfoServiceTest, finishedRepoTest,
				currentlyReadingRepoTest, backlogRepoTest, mangoUtilTest, appUserServiceTest);

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

		// mocked mangoinfo object
//		MangoInfo mangoInfoMock = new MangoInfo();
//		mangoInfoMock.setData(new Data());
//		mangoInfoMock.getData().setMedia(new Media());
//		mangoInfoMock.getData().getMedia().setAnilistId(123L);
//		mangoInfoMock.getData().getMedia().setBannerImage("mock banner img url");
//		
//		mangoInfoMock.getData().getMedia().setCoverImage(new CoverImage());
//		mangoInfoMock.getData().getMedia().getCoverImage().setLarge("mock coverimage large url");
//		
//		mangoInfoMock.getData().getMedia().setLastChapter(null);
//		
//		mangoInfoMock.getData().getMedia().setStaff(new Staff());

	}

	@Test
	void canGetCompletedMangoes() {

		String userName = "ABC123";
		given(appUserServiceTest.getUser(userName)).willReturn(testUser);
		mangoServiceTest.getFinished(userName, 0, 1);
		verify(finishedRepoTest).findByUserId(testUser.getId(), PageRequest.of(0, 1));

	}

	@Test
	void canGetCurrentlyReading() {

		String userName = "ABC123";
		given(appUserServiceTest.getUser(userName)).willReturn(testUser);
		mangoServiceTest.getCurrentlyReading(userName, 0, 1);
		verify(currentlyReadingRepoTest).findByUserId(testUser.getId(), PageRequest.of(0, 1));

	}

	@Test
	void canGetBacklog() {

		String userName = "ABC123";
		given(appUserServiceTest.getUser(userName)).willReturn(testUser);
		mangoServiceTest.getBacklog(userName, 0, 1);
		verify(backlogRepoTest).findByUserId(testUser.getId(), PageRequest.of(0, 1));

	}

	@Test
	void givenMangoIsPresentButFinishedRecordAlreadyExists_whenGenerateFinished_throwsBadRequestException() {
		String completionDateTime = "2022-12-11T12:45";
		String remarks = "YOYOYO";
		Optional<Finished> finishedRecordMock = Optional.of(new Finished());
		String errorMsg = "BadRequestException thrown for givenMangoIsPresentButFinishedRecordAlreadyExists_whenGenerateFinished_throwsBadRequestException";

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);

		FinishedDTO requestData = new FinishedDTO(testMango.getMangoTitle(), completionDateTime, remarks,
				testUser.getUsername(), testMango.getAnilistId());
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(finishedRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(finishedRecordMock);
		given(mangoUtilTest.getMessage("MangoService.RECORD_ALREADY_EXISTS")).willReturn(errorMsg);
		assertThatThrownBy(() -> mangoServiceTest.generateFinishedRecord(requestData))
				.isInstanceOf(BadRequestException.class).hasMessageContaining(errorMsg);

	}

	@Test
	void givenMangoIsPresentButCtlyReadingRecordAlreadyExists_whenGenerateFinished_throwsBadRequestException() {
		String completionDateTime = "2022-12-11T12:45";
		String remarks = "YOYOYO";
		Optional<CurrentlyReading> ctlyReadingMock = Optional.of(new CurrentlyReading());

		String errorMsg = "BadRequestException thrown for givenMangoIsPresentButCtlyReadingRecordAlreadyExists_whenGenerateFinished_throwsBadRequestException";

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);

		FinishedDTO requestData = new FinishedDTO(testMango.getMangoTitle(), completionDateTime, remarks,
				testUser.getUsername(), testMango.getAnilistId());
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(ctlyReadingMock);
		given(mangoUtilTest.getMessage("MangoService.MANGO_ALREADY_EXISTS_IN_CURRENTLYREADINGRECORD")).willReturn(errorMsg);
		assertThatThrownBy(() -> mangoServiceTest.generateFinishedRecord(requestData))
				.isInstanceOf(BadRequestException.class).hasMessageContaining(errorMsg);
	}

	@Test
	void givenMangoIsPresentButBacklogRecordAlreadyExists_whenGenerateFinished_returnsCorrectRecord() {
		String completionDateTime = "2022-12-11T12:45";
		String remarks = "YOYOYO";
		Optional<Backlog> backlogMock = Optional.of(new Backlog(123L, testUser, testMango, ""));
		FinishedDTO requestData = new FinishedDTO(testMango.getMangoTitle(), completionDateTime, remarks,
				testUser.getUsername(), testMango.getAnilistId());
		Finished finishedMock = new Finished(123L, testUser, testMango, completionDateTime, remarks);

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(backlogMock);
		given(mangoUtilTest.generateFinished(requestData, testUser, testMango)).willReturn(finishedMock);

		mangoServiceTest.generateFinishedRecord(requestData);

		// then
		ArgumentCaptor<Backlog> backlogArgumentCaptor = ArgumentCaptor.forClass(Backlog.class);

		// side effect of this scenario - backlogrepo delete record call
		verify(backlogRepoTest).delete(backlogArgumentCaptor.capture());
		Backlog actualBacklog = backlogArgumentCaptor.getValue();
		assertThat(actualBacklog).isEqualTo(backlogMock.get());

		ArgumentCaptor<Finished> finishedArgumentCaptor = ArgumentCaptor.forClass(Finished.class);
		verify(finishedRepoTest).save(finishedArgumentCaptor.capture());
		Finished actualFinished = finishedArgumentCaptor.getValue();
		assertThat(actualFinished).isEqualTo(finishedMock);

	}

	@Test
	void givenMangoIsPresentButNoUserRecords_whenGenerateFinished_returnsCorrectRecord() {
		// given
		String completionDateTime = "2022-12-11T12:45";
		String remarks = "YOYOYO";
		FinishedDTO requestData = new FinishedDTO(testMango.getMangoTitle(), completionDateTime, remarks,
				testUser.getUsername(), testMango.getAnilistId());
		Finished finishedMock = new Finished(123L, testUser, testMango, completionDateTime, remarks);

		// when
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(mangoUtilTest.generateFinished(requestData, testUser, testMango)).willReturn(finishedMock);

		// no records exist
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());
		given(finishedRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());

		mangoServiceTest.generateFinishedRecord(requestData);

		ArgumentCaptor<Finished> finishedArgumentCaptor = ArgumentCaptor.forClass(Finished.class);
		verify(finishedRepoTest).save(finishedArgumentCaptor.capture());
		Finished actualFinished = finishedArgumentCaptor.getValue();
		assertThat(actualFinished).isEqualTo(finishedMock);
	}

	@Test
	void givenMangoNotPresent_whenGenerateFinished_returnsCorrectRecord() {
		// given
		String completionDateTime = "2022-12-11T12:45";
		String remarks = "YOYOYO";
		FinishedDTO requestData = new FinishedDTO(testMango.getMangoTitle(), completionDateTime, remarks,
				testUser.getUsername(), testMango.getAnilistId());
		Finished finishedMock = new Finished(123L, testUser, testMango, completionDateTime, remarks);
		MangoInfo mangoInfoMock = new MangoInfo();
		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(123L);

		// when
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.empty());

		given(mangoInfoServiceTest.getMangoInfoByAnilistId(requestData.getAnilistId())).willReturn(mangoInfoMock);
		given(mangoUtilTest.setMangoInfo(mangoInfoMock)).willReturn(testMango);
		given(mangoUtilTest.generateFinished(requestData, testUser, testMango)).willReturn(finishedMock);
		given(mangoRepoTest.save(testMango)).willReturn(testMango);

		mangoServiceTest.generateFinishedRecord(requestData);

		ArgumentCaptor<Finished> finishedArgumentCaptor = ArgumentCaptor.forClass(Finished.class);
		verify(finishedRepoTest).save(finishedArgumentCaptor.capture());
		Finished actualFinished = finishedArgumentCaptor.getValue();
		assertThat(actualFinished).isEqualTo(finishedMock);
		assertThat(actualFinished.getMango().getAnilistId()).isEqualTo(123L);
	}

	@Test
	void givenMangoIsPresentButBacklogRecordExists_whenGenerateCurrentlyReading_returnsCorrectRecord() {

		String lastReadTime = "2022-12-11T12:45";
		Optional<Backlog> backlogMock = Optional.of(new Backlog(123L, testUser, testMango, ""));
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, lastReadTime,
				testUser.getUsername(), 123L, null);
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, lastReadTime);

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(backlogMock);

		given(mangoUtilTest.generateCurrentlyReading(requestData, testUser, testMango)).willReturn(ctlyReadingMock);

		mangoServiceTest.generateCurrentlyReadingRecord(requestData);

		// then
		ArgumentCaptor<CurrentlyReading> ctlyReadingArgumentCaptor = ArgumentCaptor.forClass(CurrentlyReading.class);
		ArgumentCaptor<Backlog> backlogArgumentCaptor = ArgumentCaptor.forClass(Backlog.class);

		// side effect of this scenario - backlogrepo delete record call
		verify(backlogRepoTest).delete(backlogArgumentCaptor.capture());

		// CurrentlyReading object returned from mangoUtil.generateFinished
		verify(currentlyReadingRepoTest).save(ctlyReadingArgumentCaptor.capture());

		Backlog actualBacklog = backlogArgumentCaptor.getValue();
		assertThat(actualBacklog).isEqualTo(backlogMock.get());

		CurrentlyReading actualCtlyReading = ctlyReadingArgumentCaptor.getValue();
		assertThat(actualCtlyReading).isEqualTo(ctlyReadingMock);

	}

	@Test
	void givenMangoIsPresentButCtlyReadingRecordAlreadyExists_whenGenerateCurrentlyReading_throwsBadRequestException() {

		// given
		String lastReadTime = "2022-12-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, lastReadTime,
				testUser.getUsername(), 123L, null);
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, lastReadTime);
		String errorMsg = "BadRequestException thrown for givenMangoIsPresentButCtlyReadingRecordAlreadyExists_whenGenerateCurrentlyReading_throwsBadRequestException";

		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		// Mock currentlyReadingRecord exists
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		// Mock getErrorMessage
		given(mangoUtilTest.getMessage("MangoService.RECORD_ALREADY_EXISTS")).willReturn(errorMsg);

		assertThatThrownBy(() -> mangoServiceTest.generateCurrentlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class).hasMessageContaining(errorMsg);

	}

	@Test
	void givenMangoIsPresentButFinishedRecordAlreadyExists_whenGenerateCurrentlyReading_throwsBadRequestException() {

		// given
		String completionDateTime = "2022-12-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, "2022-11-22T12:45",
				testUser.getUsername(), 123L, null);
		Finished finishedMock = new Finished(123L, testUser, testMango, completionDateTime, "YOYOYO");

		String errorMsg = "BadRequestException thrown for givenMangoIsPresentButCtlyReadingRecordAlreadyExists_whenGenerateCurrentlyReading_throwsBadRequestException";

		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		// Mock Finished record exists
		given(finishedRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(finishedMock));

		// Mock getErrorMessage
		given(mangoUtilTest.getMessage("MangoService.RECORD_ALREADY_EXISTS")).willReturn(errorMsg);

		assertThatThrownBy(() -> mangoServiceTest.generateCurrentlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class).hasMessageContaining(errorMsg);

	}

	@Test
	void givenMangoIsPresentButNoUserRecordsExist_whenGenerateCurrentlyReading_returnsCorrectRecord() {
		// given
		String lastReadTime = "2022-12-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, "2022-11-22T12:45",
				testUser.getUsername(), 123L, null);
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, lastReadTime);

		// when

		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		// mock currentlyReading record
		given(mangoUtilTest.generateCurrentlyReading(requestData, testUser, testMango)).willReturn(ctlyReadingMock);

		// no records exist
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());
		given(finishedRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());

		mangoServiceTest.generateCurrentlyReadingRecord(requestData);

		ArgumentCaptor<CurrentlyReading> ctlyReadingArgumentCaptor = ArgumentCaptor.forClass(CurrentlyReading.class);

		// verify save method called for currentlyREading record
		verify(currentlyReadingRepoTest).save(ctlyReadingArgumentCaptor.capture());

		// verify equal objects in the argument for save and the one generated by
		// mangoUtil.generateCurrentlyReading
		CurrentlyReading actualCtlyReading = ctlyReadingArgumentCaptor.getValue();
		assertThat(actualCtlyReading).isEqualTo(ctlyReadingMock);
	}

	@Test
	void givenMangoNotPresent_whenGenerateCurrentlyReading_returnsCorrectRecord() {
		String lastReadTime = "2022-12-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, "2022-11-22T12:45",
				testUser.getUsername(), 123L, null);
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, lastReadTime);

		// Mock user and mango-but mango is empty for this test
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.empty());

		// Mock mangoInfo
		MangoInfo mangoInfoMock = new MangoInfo();
		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(123L);

		// Mock mangoInfoService getMangoInfo call
		given(mangoInfoServiceTest.getMangoInfoByAnilistId(requestData.getAnilistId())).willReturn(mangoInfoMock);

		// Mock setMangoInfo call
		given(mangoUtilTest.setMangoInfo(mangoInfoMock)).willReturn(testMango);

		// Mock generateCurrentlyReading
		given(mangoUtilTest.generateCurrentlyReading(requestData, testUser, testMango)).willReturn(ctlyReadingMock);

		mangoServiceTest.generateCurrentlyReadingRecord(requestData);

		// verify mangorep save method called
		verify(mangoRepoTest).save(testMango);

		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<CurrentlyReading> ctlyReadingArgumentCaptor = ArgumentCaptor.forClass(CurrentlyReading.class);
		verify(currentlyReadingRepoTest).save(ctlyReadingArgumentCaptor.capture());

		CurrentlyReading actualCtlyReading = ctlyReadingArgumentCaptor.getValue();

		assertThat(actualCtlyReading).isEqualTo(ctlyReadingMock);
		assertThat(actualCtlyReading.getMango().getAnilistId()).isEqualTo(123L);
	}

	@Test
	void givenMangoIsPresentButRecordsAlreadyExist_whenGenerateBacklog_throwsMangoAlreadyExistsException() {

		String addedDateTime = "2022-12-11T12:45";

		BacklogDTO requestData = new BacklogDTO(testMango.getMangoTitle(), addedDateTime, testUser.getUsername(), 123L);
		String errorMsg = "MangoAlreadyExistsException thrown for givenMangoIsPresentButRecordsAlreadyExist_whenGenerateBacklog_givenMangoIsPresentButRecordsAlreadyExist_whenGenerateBacklog_throwsMangoAlreadyExistsException";
		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		// no records exist
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(new CurrentlyReading()));

		// Mock getErrorMessage
		given(mangoUtilTest.getMessage("MangoService.MANGO_ALREADY_EXIST_FOR_USER")).willReturn(errorMsg);
		assertThatThrownBy(() -> mangoServiceTest.generateBacklogRecord(requestData))
				.isInstanceOf(MangoAlreadyExistsException.class).hasMessageContaining(errorMsg);
	}

	@Test
	void givenMangoIsPresentAndNoUserRecordsExist_whenGenerateBacklog_returnsCorrectRecord() {
		String addedDateTime = "2022-12-11T12:45";
		BacklogDTO requestData = new BacklogDTO(testMango.getMangoTitle(), addedDateTime, testUser.getUsername(), 123L);
		Backlog backlogMock = new Backlog(123L, testUser, testMango, addedDateTime);

		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		// no records exist
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());
		given(finishedRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());

		// Mock generateBacklog
		given(mangoUtilTest.generateBacklog(requestData, testUser, testMango)).willReturn(backlogMock);

		mangoServiceTest.generateBacklogRecord(requestData);

		ArgumentCaptor<Backlog> backlogArgumentCaptor = ArgumentCaptor.forClass(Backlog.class);

		// verify save method called for backlog record
		verify(backlogRepoTest).save(backlogArgumentCaptor.capture());

		// verify equal objects in the argument for save and the one generated by
		// mangoUtil.generateBacklog
		Backlog actualBacklog = backlogArgumentCaptor.getValue();
		assertThat(actualBacklog).isEqualTo(backlogMock);
	}

	@Test
	void givenMangoNotPresent_whenGenerateBacklog_returnsCorrectRecord() {
		String addedDateTime = "2022-12-11T12:45";
		BacklogDTO requestData = new BacklogDTO(testMango.getMangoTitle(), addedDateTime, testUser.getUsername(), 123L);
		Backlog backlogMock = new Backlog(123L, testUser, testMango, addedDateTime);

		// Mock mangoInfo
		MangoInfo mangoInfoMock = new MangoInfo();
		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(123L);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.empty());

		// Mock mangoInfoService getMangoInfo call
		given(mangoInfoServiceTest.getMangoInfoByAnilistId(requestData.getAnilistId())).willReturn(mangoInfoMock);

		// Mock setMangoInfo call
		given(mangoUtilTest.setMangoInfo(mangoInfoMock)).willReturn(testMango);

		// Mock generateCurrentlyReading
		given(mangoUtilTest.generateBacklog(requestData, testUser, testMango)).willReturn(backlogMock);

		mangoServiceTest.generateBacklogRecord(requestData);

		// verify mangorepo save method called
		verify(mangoRepoTest).save(testMango);

		// capture argument object for backlogrepo save call
		ArgumentCaptor<Backlog> backlogArgumentCaptor = ArgumentCaptor.forClass(Backlog.class);
		verify(backlogRepoTest).save(backlogArgumentCaptor.capture());

		Backlog actualBacklog = backlogArgumentCaptor.getValue();

		assertThat(actualBacklog).isEqualTo(backlogMock);
		assertThat(actualBacklog.getMango().getAnilistId()).isEqualTo(123L);

	}

	@Test
	void givenMangoIsNotPresent_whenUpdateCtlyReading_throwsMangoNotFoundException() {

		String lastReadTime = "2022-12-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 123, lastReadTime,
				testUser.getUsername(), 123L, null);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecord(requestData))
				.isInstanceOf(MangoNotFoundException.class)
				.hasMessageContaining(String.format("Mango %s doesn't exist!", requestData.getMangoTitle()));
	}

	@Test
	void givenMangoStatusIsFinishedAndLastChapterReadIsEqualToFinalChapter_whenUpdateCtlyReading_throwsBadRequestException() {

		// Request object with last chapter read < currentt record's
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 125, "2022-05-14T12:45",
				testUser.getUsername(), 123L, null);
		
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 50, "2022-03-14T12:45");

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
		.willReturn(Optional.of(ctlyReadingMock));
		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Invalid operation: mango %s has already finished publishing", requestData.getMangoTitle()));
	}

	@Test
	void givenLastReadChapterInRequestIsEarlierThanExistingRecord_whenUpdateCtlyReading_throwsBadRequestException() {

		// Request object with last chapter read < currentt record's
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 14, "2022-05-14T12:45",
				testUser.getUsername(), 123L, null);

		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 50, "2022-03-14T12:45");

		// set mango status to releasing
		testMango.setStatus(MangoStatus.RELEASING);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class).extracting("errorMsgs", InstanceOfAssertFactories.ITERABLE)
				.containsExactly("last chapter read must be later than the value in your record");
	}

	@Test
	void givenLastReadTimeInRequestIsBeforeExistingRecord_whenUpdateCtlyReading_throwsBadRequestException() {

		String lastReadTime = "2022-05-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 124, lastReadTime,
				testUser.getUsername(), 123L, null);

		// CurrentlyReading mocked object with earlier lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, "2022-12-22T12:45");
		// set mango status to releasing
		testMango.setStatus(MangoStatus.RELEASING);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class).extracting("errorMsgs", InstanceOfAssertFactories.ITERABLE)
				.containsExactly("last read time must be later than the value in your record");
	}

	@Test
	void givenLastReadTimeInRequestIsEqualToExistingRecord_whenUpdateCtlyReading_throwsBadRequestException() {

		String lastReadTime = "2022-05-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 124, lastReadTime,
				testUser.getUsername(), 123L, null);

		// CurrentlyReading mocked object with SAME lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, lastReadTime);

		// set mango status to releasing
		testMango.setStatus(MangoStatus.RELEASING);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class).extracting("errorMsgs", InstanceOfAssertFactories.ITERABLE)
				.containsExactly("last read time must be later than the value in your record");
	}

	/**
	 * Correct requirements:<br>
	 * 1. Last read time must be later than current record<br>
	 * 2. Last chapter read must be later than current record
	 */
	@Test
	void givenCorrectRequirements_whenUpdateCtlyReading_updatesRecordCorrectly() {

		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 50, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		// CurrentlyReading mocked object with SAME lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 11, "2022-05-11T12:44");

		// set mango status to releasing
		testMango.setStatus(MangoStatus.RELEASING);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		mangoServiceTest.updateCtlyReadingRecord(requestData);

		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<CurrentlyReading> ctlyReadingArgumentCaptor = ArgumentCaptor.forClass(CurrentlyReading.class);
		verify(currentlyReadingRepoTest).save(ctlyReadingArgumentCaptor.capture());

		CurrentlyReading actualRecord = ctlyReadingArgumentCaptor.getValue();

		assertThat(actualRecord).isEqualTo(ctlyReadingMock);
		assertThat(actualRecord.getMango().getAnilistId()).isEqualTo(123L);
		assertThat(actualRecord.getLastChapterRead()).isEqualTo(50);
		assertThat(actualRecord.getLastReadTime()).isEqualTo("2022-07-11T12:45");
	}

	@Test
	void givenBacklogRecordExists_whenUpdateCtlyReadingFromBacklogRecord_updatesRecordCorrectly() {

		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 50, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		// Mocked backlog record
		Backlog backlogMock = new Backlog(123L, testUser, testMango, "2022-01-22T12:45");

		// Mock user and mango and backlog return
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(backlogMock));
		given(mangoUtilTest.generateCurrentlyReading(requestData, testUser, testMango)).willCallRealMethod();

		// set mango status to releasing
		testMango.setStatus(MangoStatus.RELEASING);

		mangoServiceTest.updateCtlyReadingRecord(requestData);

		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<CurrentlyReading> ctlyReadingArgumentCaptor = ArgumentCaptor.forClass(CurrentlyReading.class);
		verify(currentlyReadingRepoTest).save(ctlyReadingArgumentCaptor.capture());

		CurrentlyReading actualRecord = ctlyReadingArgumentCaptor.getValue();

		assertThat(actualRecord.getLastChapterRead()).isEqualTo(1);// because update from a backlog record, lastchapter
																	// read is always 1
		assertThat(actualRecord.getLastReadTime()).isEqualTo("2022-07-11T12:45");
		assertThat(actualRecord.getCurrentlyReadingId()).isEqualTo(123123L);
	}

	@Test
	void givenBacklogRecordNotPresent_whenUpdateCtlyReadingFromBacklogRecord_throwsBadRequestException() {

		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 50, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		// set mango status to releasing
		testMango.setStatus(MangoStatus.RELEASING);

		// Mock user and mango and backlog return
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(backlogRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecord(requestData))
				.isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Backlog record for mango: %s and user: %s not found",
						requestData.getMangoTitle(), requestData.getUser()));
	}

	@Test
	void givenMangoStatusIsNotFinished_whenUpdateCtlyReadingAndFinish_throwsMangoNotFoundException() {

		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 50, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		// Mock user and mango and backlog return
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		testMango.setStatus(MangoStatus.RELEASING);

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData))
				.isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Mango %s's status is not finished!", requestData.getMangoTitle()));
	}

	@Test
	void givenMangoDoesntExist_whenUpdateCtlyReadingAndFinish_throwsMangoNotFoundException() {

		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 50, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		// Mock user and mango and backlog return
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData))
				.isInstanceOf(MangoNotFoundException.class)
				.hasMessage(String.format("Mango %s doesn't exist!", requestData.getMangoTitle()));
	}

	@Test
	void givenCurrentlyReadingRecordDoesntExist_whenUpdateCtlyReadingAndFinish_throwsMangoNotFoundException() {

		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 50, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		// Mock user and mango and backlog return
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData))
				.isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Currently reading record for mango: %s and user: %s not found",
						requestData.getMangoTitle(), requestData.getUser()));
	}

	@Test
	void givenLastReadChapterInRequestIsNotEqualToMangoFinalChapter_whenUpdateCtlyReadingAndFinish_throwsBadRequestException() {

		// Request object with last chapter read < currentt record's
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 14, "2022-05-14T12:45",
				testUser.getUsername(), 123L, null);

		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 50, "2022-03-14T12:45");

		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));

		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData))
				.isInstanceOf(BadRequestException.class).extracting("errorMsgs", InstanceOfAssertFactories.ITERABLE)
				.containsExactly("last chapter read is not equal to mango's final chapter");
	}

	@Test
	void givenLastReadTimeInRequestIsBeforeExistingRecord_whenUpdateCtlyReadingAndFinish_throwsBadRequestException() {

		String lastReadTime = "2022-05-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 125, lastReadTime,
				testUser.getUsername(), 123L, null);

		// CurrentlyReading mocked object with earlier lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, "2022-12-22T12:45");

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData))
				.isInstanceOf(BadRequestException.class).extracting("errorMsgs", InstanceOfAssertFactories.ITERABLE)
				.containsExactly("last read time must be later than the value in your record");
	}

	@Test
	void givenLastReadTimeInRequestIsEqualToExistingRecord_whenUpdateCtlyReadingAndFinish_throwsBadRequestException() {

		String lastReadTime = "2022-05-11T12:45";
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 125, lastReadTime,
				testUser.getUsername(), 123L, null);

		// CurrentlyReading mocked object with SAME lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 123, lastReadTime);

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));

		assertThatThrownBy(() -> mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData))
				.isInstanceOf(BadRequestException.class).extracting("errorMsgs", InstanceOfAssertFactories.ITERABLE)
				.containsExactly("last read time must be later than the value in your record");
	}

	@Test
	void givenCorrectRequirements_whenUpdateCtlyReadingAndFinish_updatesRecordsCorrectly() {
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 125, "2022-07-11T12:45",
				testUser.getUsername(), 123L, null);

		FinishedDTO finishedDTO = new FinishedDTO(testMango.getMangoTitle(), "2022-07-11T12:45", null,
				testUser.getUsername(), 123L);

		// CurrentlyReading mocked object with SAME lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 11, "2022-05-11T12:44");

		// Mock user and mango - but mango is empty
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));
		given(mangoUtilTest.getFinishedMangoRemarks(finishedDTO.getRemarks())).willCallRealMethod();
		given(mangoUtilTest.generateFinished(finishedDTO, testUser, testMango)).willCallRealMethod();

		mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData);

		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<Finished> finishedArgumentCaptor = ArgumentCaptor.forClass(Finished.class);
		verify(finishedRepoTest).save(finishedArgumentCaptor.capture());

		Finished actualRecord = finishedArgumentCaptor.getValue();

//		assertThat(actualRecord).isEqualTo(ctlyReadingMock);
		assertThat(actualRecord.getMango().getAnilistId()).isEqualTo(123L);
		assertThat(actualRecord.getFinishedId())
				.isEqualTo(Long.valueOf(testUser.getId() + "" + testMango.getMangoId()));
		assertThat(actualRecord.getRemarks()).isEqualTo("No remarks");

		// verify deletion of old currently reading record
		verify(currentlyReadingRepoTest).delete(ctlyReadingMock);

	}

	@Test
	void givenCorrectRequirementsAndRemarksIsPresent_whenUpdateCtlyReadingAndFinish_updatesRecordsCorrectly() {
		CurrentlyReadingDTO requestData = new CurrentlyReadingDTO(testMango.getMangoTitle(), 125, "2022-07-11T12:45",
				testUser.getUsername(), 123L, "GOOD MANGO MAN...");

		FinishedDTO finishedDTO = new FinishedDTO(testMango.getMangoTitle(), "2022-07-11T12:45", "GOOD MANGO MAN...",
				testUser.getUsername(), 123L);

		// CurrentlyReading mocked object with SAME lastreadtime than the request
		CurrentlyReading ctlyReadingMock = new CurrentlyReading(123L, testUser, testMango, 11, "2022-05-11T12:44");

		// Mock user and mango
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(mangoRepoTest.findByMangoTitle(requestData.getMangoTitle())).willReturn(Optional.of(testMango));
		given(currentlyReadingRepoTest.findByMangoMangoIdAndUserId(testMango.getMangoId(), testUser.getId()))
				.willReturn(Optional.of(ctlyReadingMock));
		given(mangoUtilTest.generateFinished(finishedDTO, testUser, testMango)).willCallRealMethod();
		given(mangoUtilTest.getFinishedMangoRemarks(finishedDTO.getRemarks())).willCallRealMethod();


		mangoServiceTest.updateCtlyReadingRecordAndFinish(requestData);

		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<Finished> finishedArgumentCaptor = ArgumentCaptor.forClass(Finished.class);
		verify(finishedRepoTest).save(finishedArgumentCaptor.capture());

		Finished actualRecord = finishedArgumentCaptor.getValue();

//		assertThat(actualRecord).isEqualTo(ctlyReadingMock);
		assertThat(actualRecord.getMango().getAnilistId()).isEqualTo(123L);
		assertThat(actualRecord.getFinishedId())
				.isEqualTo(Long.valueOf(testUser.getId() + "" + testMango.getMangoId()));
		assertThat(actualRecord.getRemarks()).isEqualTo("GOOD MANGO MAN...");

		// verify deletion of old currently reading record
		verify(currentlyReadingRepoTest).delete(ctlyReadingMock);

	}

	@Test
	void givenMangoDoesntExist_whenUpdateMangoStatus_DoNothing() {
		Long anilistId = 123123L;
		given(mangoRepoTest.findByAnilistId(anilistId)).willReturn(Optional.empty());
		mangoServiceTest.updateMangoPublicationStatus(anilistId);
		verify(mangoInfoServiceTest, never()).getMangoInfoByAnilistId(anilistId);
	}

	@Test
	void givenMangoIsPresentAndMangoStatusUpdateToFinishedIsAvailable_whenUpdateMangoStatus_throwsAnilistMangoInfoNotFoundException() {
		Long anilistId = 123123L;
		given(mangoRepoTest.findByAnilistId(anilistId)).willReturn(Optional.of(testMango));

		testMango.setStatus(MangoStatus.RELEASING);
		testMango.setLastChapter(null);
		testMango.setAnilistId(anilistId);

		MangoInfo mangoInfoMock = new MangoInfo();
		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(anilistId);
		mangoInfoMock.getData().getMedia().setLastChapter(123);
		mangoInfoMock.getData().getMedia().setStatus(MangoStatus.FINISHED);

		given(mangoInfoServiceTest.getMangoInfoByAnilistId(anilistId)).willReturn(mangoInfoMock);

		mangoServiceTest.updateMangoPublicationStatus(anilistId);

		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<Mango> mangoArgumentCaptor = ArgumentCaptor.forClass(Mango.class);
		verify(mangoRepoTest).save(mangoArgumentCaptor.capture());

		Mango actualMango = mangoArgumentCaptor.getValue();

		assertThat(actualMango.getLastChapter().isPresent());
		assertThat(actualMango.getStatus()).isEqualTo(MangoStatus.FINISHED);
	}

	@Test
	void givenMangoIsPresentAndMangoStatusUpdateToFinishedIsNotAvailable_whenUpdateMangoStatus_throwsAnilistMangoInfoNotFoundException() {
		Long anilistId = 123123L;
		given(mangoRepoTest.findByAnilistId(anilistId)).willReturn(Optional.of(testMango));

		testMango.setStatus(MangoStatus.RELEASING);
		testMango.setLastChapter(null);
		testMango.setAnilistId(anilistId);

		MangoInfo mangoInfoMock = new MangoInfo();
		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(anilistId);
		mangoInfoMock.getData().getMedia().setLastChapter(null);
		mangoInfoMock.getData().getMedia().setStatus(MangoStatus.RELEASING);

		given(mangoInfoServiceTest.getMangoInfoByAnilistId(anilistId)).willReturn(mangoInfoMock);

		mangoServiceTest.updateMangoPublicationStatus(anilistId);
		verify(mangoRepoTest, never()).save(testMango);
	}
	
	@Test
	void givenMangoIsPresentAndMangoStatusChangeIsAvailable_whenUpdateMangoStatus_throwsAnilistMangoInfoNotFoundException() {
		Long anilistId = 123123L;
		given(mangoRepoTest.findByAnilistId(anilistId)).willReturn(Optional.of(testMango));

		testMango.setStatus(MangoStatus.RELEASING);
		testMango.setLastChapter(null);
		testMango.setAnilistId(anilistId);

		MangoInfo mangoInfoMock = new MangoInfo();
		mangoInfoMock.setData(new Data());
		mangoInfoMock.getData().setMedia(new Media());
		mangoInfoMock.getData().getMedia().setAnilistId(anilistId);
		mangoInfoMock.getData().getMedia().setLastChapter(null);
		mangoInfoMock.getData().getMedia().setStatus(MangoStatus.CANCELLED);

		given(mangoInfoServiceTest.getMangoInfoByAnilistId(anilistId)).willReturn(mangoInfoMock);

		mangoServiceTest.updateMangoPublicationStatus(anilistId);
		verify(mangoRepoTest).save(testMango);
	}


	@Test
	void givenRecordIsEmpty_whenDeleteRecordBacklog_throwsBadRequestException() {

		DeleteRecordDTO requestData = new DeleteRecordDTO(RecordType.BACKLOG, 123L, testUser.getUsername());
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(backlogRepoTest.findById(requestData.getRecordId())).willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.deleteRecord(requestData)).isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Backlog record with id: %s for user: %s not found",
						requestData.getRecordId(), testUser.getUsername()));
	}

	@Test
	void givenRecordIsPresent_whenDeleteRecordBacklog_throwsBadRequestException() {

		DeleteRecordDTO requestData = new DeleteRecordDTO(RecordType.BACKLOG, 123L, testUser.getUsername());
		Backlog expected = new Backlog(123L, testUser, testMango, "");

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(backlogRepoTest.findById(requestData.getRecordId())).willReturn(Optional.of(expected));

		mangoServiceTest.deleteRecord(requestData);
		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<Long> backlogArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		verify(backlogRepoTest).deleteById(backlogArgumentCaptor.capture());

		Long actual = backlogArgumentCaptor.getValue();
		assertThat(actual).isEqualTo(123L);
	}
	
	@Test
	void givenRecordIsEmpty_whenDeleteRecordCurrentlyReading_throwsBadRequestException() {

		DeleteRecordDTO requestData = new DeleteRecordDTO(RecordType.CURRENTLY_READING, 123L, testUser.getUsername());
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(currentlyReadingRepoTest.findById(requestData.getRecordId())).willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.deleteRecord(requestData)).isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Currently reading record with id: %s for user: %s not found",
						requestData.getRecordId(), testUser.getUsername()));
	}

	@Test
	void givenRecordIsPresent_whenDeleteRecordCurrentlyReading_throwsBadRequestException() {

		DeleteRecordDTO requestData = new DeleteRecordDTO(RecordType.CURRENTLY_READING, 123L, testUser.getUsername());
		CurrentlyReading expected = new CurrentlyReading(123L, testUser, testMango, 12, "");

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(currentlyReadingRepoTest.findById(requestData.getRecordId())).willReturn(Optional.of(expected));

		mangoServiceTest.deleteRecord(requestData);
		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<Long> ctlyReadingArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		verify(currentlyReadingRepoTest).deleteById(ctlyReadingArgumentCaptor.capture());

		Long actual = ctlyReadingArgumentCaptor.getValue();
		assertThat(actual).isEqualTo(123L);
	}
	
	@Test
	void givenRecordIsEmpty_whenDeleteRecordFinished_throwsBadRequestException() {

		DeleteRecordDTO requestData = new DeleteRecordDTO(RecordType.FINISHED, 123L, testUser.getUsername());
		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(finishedRepoTest.findById(requestData.getRecordId())).willReturn(Optional.empty());

		assertThatThrownBy(() -> mangoServiceTest.deleteRecord(requestData)).isInstanceOf(BadRequestException.class)
				.hasMessage(String.format("Finished record with id: %s for user: %s not found",
						requestData.getRecordId(), testUser.getUsername()));
	}

	@Test
	void givenRecordIsPresent_whenDeleteRecordFinished_throwsBadRequestException() {

		DeleteRecordDTO requestData = new DeleteRecordDTO(RecordType.FINISHED, 123L, testUser.getUsername());
		Finished expected = new Finished(123L, testUser, testMango, "", "");

		given(appUserServiceTest.getUser(testUser.getUsername())).willReturn(testUser);
		given(finishedRepoTest.findById(requestData.getRecordId())).willReturn(Optional.of(expected));

		mangoServiceTest.deleteRecord(requestData);
		// capture argument object for currentlyReadingrepo save call
		ArgumentCaptor<Long> finishedArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		verify(finishedRepoTest).deleteById(finishedArgumentCaptor.capture());

		Long actual = finishedArgumentCaptor.getValue();
		assertThat(actual).isEqualTo(123L);
	}

}
