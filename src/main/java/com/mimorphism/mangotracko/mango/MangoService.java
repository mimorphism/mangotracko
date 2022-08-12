package com.mimorphism.mangotracko.mango;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.appuser.AppUserService;
import com.mimorphism.mangotracko.exception.AnilistMangoNotFoundException;
import com.mimorphism.mangotracko.exception.BadRequestException;
import com.mimorphism.mangotracko.exception.MangoAlreadyExistsException;
import com.mimorphism.mangotracko.exception.MangoNotFoundException;
import com.mimorphism.mangotracko.mango.dto.BacklogDTO;
import com.mimorphism.mangotracko.mango.dto.CurrentlyReadingDTO;
import com.mimorphism.mangotracko.mango.dto.DeleteRecordDTO;
import com.mimorphism.mangotracko.mango.dto.FinishedDTO;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;
import com.mimorphism.mangotracko.util.MangoUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class MangoService {

	private static final String MANGO_ALREADY_EXISTS_IN_USER_MANGO_LIST = "MangoService.MANGO_ALREADY_EXIST_FOR_USER";

	private final MangoRepo mangoRepo;
	private final MangoInfoService mangoInfoService;
	private final FinishedRepo finishedRepo;
	private final CurrentlyReadingRepo currentlyReadingRepo;
	private final BacklogRepo backlogRepo;
	private final MangoUtil mangoUtil;
	private final AppUserService appUserService;

	private static final String RECORD_ALREADY_EXISTS = "MangoService.RECORD_ALREADY_EXISTS";

	private static final String MANGO_IN_USER_BACKLOG = "MangoService.MANGO_IN_USER_BACKLOG";

	private static final String INVALID_OPERATION = "MangoService.INVALID_OPERATION_EXCEPTION";
	
	private static final String MANGO_ALREADY_EXISTS_IN_CTLYREADING = "MangoService.MANGO_ALREADY_EXISTS_IN_CURRENTLYREADINGRECORD";
	
	
	public List<Finished> getFinished(String userName, int page, int size) {
		AppUser user = appUserService.getUser(userName);
		List<Finished> finished = finishedRepo.findByUserId(user.getId(), PageRequest.of(page, size));
		return finished;
	}
	
	public Page<Finished> getFinishedPage(String userName, int page, int size) {
		AppUser user = appUserService.getUser(userName);
		Page<Finished> finished = finishedRepo.findAllByUserId(user.getId(), PageRequest.of(page, size));
		return finished;
	}

	public List<CurrentlyReading> getCurrentlyReading(String userName, int page, int size) {

		AppUser user = appUserService.getUser(userName);
		List<CurrentlyReading> currentlyReading = currentlyReadingRepo.findByUserId(user.getId(),PageRequest.of(page, size));
		return currentlyReading;
	}
	
	public Page<CurrentlyReading> getCurrentlyReadingPage(String userName, int page, int size) {
		AppUser user = appUserService.getUser(userName);
		Page<CurrentlyReading> currentlyReading = currentlyReadingRepo.findAllByUserId(user.getId(), PageRequest.of(page, size));
		return currentlyReading;
	}

	public List<Backlog> getBacklog(String userName, int page, int size) {

		AppUser user = appUserService.getUser(userName);
		List<Backlog> backlog = backlogRepo.findByUserId(user.getId(),PageRequest.of(page, size));
		return backlog;
	}
	
	public Page<Backlog> getBacklogPage(String userName, int page, int size) {
		AppUser user = appUserService.getUser(userName);
		Page<Backlog> backlog = backlogRepo.findAllByUserId(user.getId(), PageRequest.of(page, size));
		return backlog;
	}

	public void generateFinishedRecord(FinishedDTO requestData) throws AnilistMangoNotFoundException {
		AppUser user = appUserService.getUser(requestData.getUser());
		Optional<Mango> mango = mangoRepo.findByMangoTitle(requestData.getMangoTitle());
		Optional<Finished> finishedRecord = generateFinishedRecord(requestData, user, mango);

		if (finishedRecord.isPresent()) {
			finishedRepo.save(finishedRecord.get());
		}

	}
	

	public void generateCurrentlyReadingRecord(CurrentlyReadingDTO requestData)
			throws MangoAlreadyExistsException, AnilistMangoNotFoundException {

		AppUser user = appUserService.getUser(requestData.getUser());
		Optional<Mango> mango = mangoRepo.findByMangoTitle(requestData.getMangoTitle());

		Optional<CurrentlyReading> currentlyReadingRecord = generateCurrentlyReadingRecord(requestData, user, mango);
		
		if(currentlyReadingRecord.isPresent()) {
			currentlyReadingRepo.save(currentlyReadingRecord.get());
		}
	}


	public void generateBacklogRecord(BacklogDTO requestData)
			throws MangoAlreadyExistsException, AnilistMangoNotFoundException {

		AppUser user = appUserService.getUser(requestData.getUser());
		Optional<Mango> mango = mangoRepo.findByMangoTitle(requestData.getMangoTitle());

		Optional<Backlog> backlogRecord = generateBacklogRecord(requestData, user, mango);
		if(backlogRecord.isPresent()) {
			backlogRepo.save(backlogRecord.get());
		}
	}


	public void updateCtlyReadingRecord(CurrentlyReadingDTO requestData) throws MangoAlreadyExistsException {

		AppUser user = appUserService.getUser(requestData.getUser());
		Optional<Mango> mango = mangoRepo.findByMangoTitle(requestData.getMangoTitle());
		if ((mango.isEmpty())) {
			throw new MangoNotFoundException(String.format("Mango %s doesn't exist!", requestData.getMangoTitle()));
		}
		
		Optional<CurrentlyReading> currentlyReading = currentlyReadingRepo
				.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId());

		Optional<CurrentlyReading> updatedCurrentlyReadingRecord = updateCtlyReadingRecord(requestData, user, mango, currentlyReading);
		
		if(updatedCurrentlyReadingRecord.isPresent()) {
			currentlyReadingRepo.save(updatedCurrentlyReadingRecord.get());
		}
	}

	public void updateCtlyReadingRecordAndFinish(CurrentlyReadingDTO requestData) throws MangoAlreadyExistsException {

		AppUser user = appUserService.getUser(requestData.getUser());
		Optional<Mango> mango = mangoRepo.findByMangoTitle(requestData.getMangoTitle());// TODO change to
																						// findbyanilistid
		if ((mango.isEmpty())) {
			throw new MangoNotFoundException(String.format("Mango %s doesn't exist!", requestData.getMangoTitle()));
		}
		
		if(mango.get().getStatus() == MangoStatus.RELEASING) {
			throw new BadRequestException(String.format("Mango %s's status is not finished!", requestData.getMangoTitle()));
		}
		
		Optional<CurrentlyReading> currentlyReading = currentlyReadingRepo
				.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId());

		if (currentlyReading.isPresent()) {
			List<String> errorMsgs = getCtlyReadingUpdateAndFinishValidationError(requestData, currentlyReading.get(),
					mango.get());
			if (!errorMsgs.isEmpty()) {
				throw new BadRequestException(errorMsgs);
			}

			FinishedDTO finishedDTO = new FinishedDTO(mango.get().getMangoTitle(), requestData.getLastReadTime(),
					requestData.getRemarks(), user.getUsername(), mango.get().getAnilistId());
			Finished finished = mangoUtil.generateFinished(finishedDTO, user, mango.get());
			finishedRepo.save(finished);
			currentlyReadingRepo.delete(currentlyReading.get());

		} else {
			throw new BadRequestException(String.format("Currently reading record for mango: %s and user: %s not found",
					requestData.getMangoTitle(), requestData.getUser()));
		}
	}

	/**
	 * Updates mango status (from RELEASING to FINISHED)
	 * <p>Last chapter's presence on Anilist means a mango
	 * <p>has finished publishing, but not always all the time for less popular mangoes
	 */
	@Async
	public void updateMangoPublicationStatus(Long anilistId) {
		Optional<Mango> mango = mangoRepo.findByAnilistId(anilistId);
		if ((mango.isEmpty())) {
			return;
		}
		MangoInfo mangoInfo = mangoInfoService.getMangoInfoByAnilistId(mango.get().getAnilistId());
		if (mangoStatusUpdateAvailable(mango.get().getStatus(), mangoInfo)) {

			if(mangoHasFinishedPublication(mango, mangoInfo)) {
				mango.get().setLastChapter(mangoInfo.getData().getMedia().getLastChapter());
				mango.get().setStatus(mangoInfo.getData().getMedia().getStatus());
				mangoRepo.save(mango.get());
			}
			//just a change in publishing status
			else {
				mango.get().setStatus(mangoInfo.getData().getMedia().getStatus());
				mangoRepo.save(mango.get());
			}
			
		}
	}

	public void deleteRecord(DeleteRecordDTO requestData) {
		AppUser user = appUserService.getUser(requestData.getUser());

		if (requestData.getRecordType() == RecordType.BACKLOG) {
			Optional<Backlog> record = backlogRepo.findById(requestData.getRecordId());
			if (record.isEmpty()) {
				throw new BadRequestException(String.format("Backlog record with id: %s for user: %s not found",
						requestData.getRecordId(), user.getUsername()));
			}
			backlogRepo.deleteById(record.get().getBacklogId());
		}
		if (requestData.getRecordType() == RecordType.CURRENTLY_READING) {
			Optional<CurrentlyReading> record = currentlyReadingRepo.findById(requestData.getRecordId());
			if (record.isEmpty()) {
				throw new BadRequestException(
						String.format("Currently reading record with id: %s for user: %s not found",
								requestData.getRecordId(), user.getUsername()));
			}
			currentlyReadingRepo.deleteById(record.get().getCurrentlyReadingId());
		}
		if (requestData.getRecordType() == RecordType.FINISHED) {
			Optional<Finished> record = finishedRepo.findById(requestData.getRecordId());
			if (record.isEmpty()) {
				throw new BadRequestException(String.format("Finished record with id: %s for user: %s not found",
						requestData.getRecordId(), user.getUsername()));
			}
			finishedRepo.deleteById(record.get().getFinishedId());
		}
	}
	
	//TODO look into how to achieve this in one go instead of querying sequentially for every record types etc
	public List<MangoRecordType> getUserExistingMangoRecords(String userName){
		AppUser user = appUserService.getUser(userName);
		
//		List<Long> backlog = backlogRepo.findMangoMangoIdByUserId(user.getId());
//		List<Long> ctlyReading = currentlyReadingRepo.findMangoMangoIdByUserId(user.getId()); 
//		List<Long> finished = finishedRepo.findMangoMangoIdByUserId(user.getId());
		
		List<Backlog> backlog = backlogRepo.findByUserId(user.getId());
		List<CurrentlyReading> ctlyReading = currentlyReadingRepo.findByUserId(user.getId()); 
		List<Finished> finished = finishedRepo.findByUserId(user.getId());
		
		return Stream.of(backlog,ctlyReading,finished)
				.flatMap(Collection::stream).collect(Collectors.toList());		
	}
	
	  /***********************/
	  /*IMPLEMENTATION DETAILS/
	  /***********************/
	

	private boolean recordAlreadyExistsForMangoAndUser(AppUser user, Optional<Mango> mango) {
		return ctlyReadingRecordAlreadyExistsForMangoAndUser(user, mango)
				|| finishedRecordAlreadyExistsForMangoAndUser(user, mango)
				|| backlogRecordAlreadyExistsForMangoAndUser(user, mango);
	}

	private boolean ctlyReadingRecordAlreadyExistsForMangoAndUser(AppUser user, Optional<Mango> mango) {
		return currentlyReadingRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId()).isPresent();
	}

	private boolean finishedRecordAlreadyExistsForMangoAndUser(AppUser user, Optional<Mango> mango) {
		return finishedRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId()).isPresent();
	}

	private boolean backlogRecordAlreadyExistsForMangoAndUser(AppUser user, Optional<Mango> mango) {
		return backlogRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId()).isPresent();
	}

	
	/**
	 * Second step of validation for updating currently reading record.
	 * <p>Checks last chapter read, and last read time
	 * @param requestData
	 * @param record
	 * @param mango
	 * @return List<String> with the error messages
	 */
	private List<String> getCtlyReadingUpdateValidationError(CurrentlyReadingDTO requestData, CurrentlyReading record,
			Mango mango) {
		List<String> errorMsgs = new ArrayList<>();
		
		if (requestData.getLastChapterRead() <= record.getLastChapterRead()) {
			errorMsgs.add("last chapter read must be later than the value in your record");
		}
		if (LocalDateTime.parse(requestData.getLastReadTime()).isBefore(LocalDateTime.parse(record.getLastReadTime()))
				|| LocalDateTime.parse(requestData.getLastReadTime()).isEqual(LocalDateTime.parse(record.getLastReadTime()))) {
			errorMsgs.add("last read time must be later than the value in your record");
		}
		return errorMsgs;
	}
	
	/**
	 * Second step of validation for updating currently reading record 
	 * <p>and subsequently finishes it.
	 * <p>Checks last chapter read, and last read time
	 * @param requestData
	 * @param record
	 * @param mango
	 * @return List<String> with the error messages
	 */
	private List<String> getCtlyReadingUpdateAndFinishValidationError(CurrentlyReadingDTO requestData, CurrentlyReading record,
			Mango mango) {
		
		List<String> errorMsgs = new ArrayList<>();
		
		if (isLastChapterReadNotEqualToFinishedMangoLastChapter(requestData.getLastChapterRead(), mango)) {
			errorMsgs.add("last chapter read is not equal to mango's final chapter");
		}
		if (LocalDateTime.parse(requestData.getLastReadTime()).isBefore(LocalDateTime.parse(record.getLastReadTime()))
				|| LocalDateTime.parse(requestData.getLastReadTime()).isEqual(LocalDateTime.parse(record.getLastReadTime()))) {
			errorMsgs.add("last read time must be later than the value in your record");
		}
		
		return errorMsgs;

	}

	private boolean isLastChapterReadNotEqualToFinishedMangoLastChapter(int lastChapterRead, Mango mango) {
		return mango.getStatus() == MangoStatus.FINISHED
				&& lastChapterRead != mango.getLastChapter().get();
	}

	private Optional<Finished> generateFinishedRecord(FinishedDTO requestData, AppUser user, Optional<Mango> mango) {

		if (mango.isPresent() && finishedRecordAlreadyExistsForMangoAndUser(user, mango)) {
			throw new BadRequestException(mangoUtil.getMessage(RECORD_ALREADY_EXISTS));
		}

		else if (mango.isPresent() && ctlyReadingRecordAlreadyExistsForMangoAndUser(user, mango)) {
			throw new BadRequestException(String.format(mangoUtil.getMessage(MANGO_ALREADY_EXISTS_IN_CTLYREADING), mango.get().getMangoTitle()));

		}

		else if (mango.isPresent() && backlogRecordAlreadyExistsForMangoAndUser(user, mango)) {
			// MANGO ALREADY EXISTS IN BACKLOG FOR THE USER, WE'LL ALLOW IF THE USER WANTS
			// TO MARK THIS MANGO AS FINISHED
			Optional<Backlog> backlog = backlogRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId());
			backlogRepo.delete(backlog.get());
			return Optional.of(mangoUtil.generateFinished(requestData, user, mango.get()));
		}

		// mango exists in the db but the user has no records -> proceed
		else if (mango.isPresent() && !recordAlreadyExistsForMangoAndUser(user, mango)) {
			return Optional.of(mangoUtil.generateFinished(requestData, user, mango.get()));

		} else if (mango.isEmpty()) {
			Mango newMango = generateNewMango(requestData.getAnilistId());
			mangoRepo.save(newMango);
			return Optional.of(mangoUtil.generateFinished(requestData, user, newMango));
		}
		return Optional.empty();
	}
	
	private Optional<CurrentlyReading> generateCurrentlyReadingRecord(CurrentlyReadingDTO requestData, AppUser user, Optional<Mango> mango) {
		if (mango.isPresent() && backlogRecordAlreadyExistsForMangoAndUser(user, mango)) {
			Optional<Backlog> backlog = backlogRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId());
			backlogRepo.delete(backlog.get());
			return Optional.of(mangoUtil.generateCurrentlyReading(requestData, user, mango.get()));
		}

		else if (mango.isPresent() && ctlyReadingRecordAlreadyExistsForMangoAndUser(user, mango)) {
			throw new BadRequestException(mangoUtil.getMessage(RECORD_ALREADY_EXISTS));

		}

		else if (mango.isPresent() && finishedRecordAlreadyExistsForMangoAndUser(user, mango)) {
			throw new BadRequestException(mangoUtil.getMessage(RECORD_ALREADY_EXISTS));

		}

		// mango exists in the db but the user has no records -> proceed
		else if (mango.isPresent() && !recordAlreadyExistsForMangoAndUser(user, mango)) {
			return Optional.of(mangoUtil.generateCurrentlyReading(requestData, user, mango.get()));
		}

		else if (mango.isEmpty()) {
			Mango newMango = generateNewMango(requestData.getAnilistId());
			mangoRepo.save(newMango);
			return Optional.of(mangoUtil.generateCurrentlyReading(requestData, user, newMango));
		}
		return Optional.empty();
	}
	
	private Optional<Backlog> generateBacklogRecord(BacklogDTO requestData, AppUser user, Optional<Mango> mango) {
		if (mango.isPresent() && recordAlreadyExistsForMangoAndUser(user, mango)) {
			throw new MangoAlreadyExistsException(String.format(
					mangoUtil.getMessage(MANGO_ALREADY_EXISTS_IN_USER_MANGO_LIST), requestData.getMangoTitle()));
		}

		else if (mango.isPresent() && !recordAlreadyExistsForMangoAndUser(user, mango)) {
			return Optional.of(mangoUtil.generateBacklog(requestData, user, mango.get()));
		}

		else if (mango.isEmpty()) {
			Mango newMango = generateNewMango(requestData.getAnilistId());
			mangoRepo.save(newMango);
			return Optional.of(mangoUtil.generateBacklog(requestData, user, newMango));
		}
		return Optional.empty();
	}
	
	private Optional<CurrentlyReading> updateCtlyReadingRecord(CurrentlyReadingDTO requestData, AppUser user, Optional<Mango> mango,
			Optional<CurrentlyReading> currentlyReading) {
		if (currentlyReading.isPresent()) { 
			if(mango.get().getStatus() == MangoStatus.FINISHED && requestData.getLastChapterRead() == mango.get().getLastChapter().get()) {
				throw new BadRequestException(String.format("Invalid operation: mango %s has already finished publishing", requestData.getMangoTitle()));
			}
			List<String> errorMsgs = getCtlyReadingUpdateValidationError(requestData, currentlyReading.get() ,
					mango.get());
			if (!errorMsgs.isEmpty()) {
				throw new BadRequestException(errorMsgs);
			}
			currentlyReading.get().setLastChapterRead(Integer.valueOf(requestData.getLastChapterRead()));
			currentlyReading.get().setLastReadTime(requestData.getLastReadTime());
			return currentlyReading;
		} else {
			return updateMangoFromBacklogRecord(requestData, user, mango);
		}
	}

	private Optional<CurrentlyReading> updateMangoFromBacklogRecord(CurrentlyReadingDTO requestData, AppUser user,
			Optional<Mango> mango) {
		Optional<Backlog> backlog = backlogRepo.findByMangoMangoIdAndUserId(mango.get().getMangoId(), user.getId());
		
		//from a backlog record, last chapter read is 1
		requestData.setLastChapterRead(1);

		if (backlog.isPresent()) {
			CurrentlyReading record = mangoUtil.generateCurrentlyReading(requestData, user, mango.get());
			backlogRepo.delete(backlog.get());
			return Optional.of(record);
		} else if (backlog.isEmpty()) {
			throw new BadRequestException(
					String.format("Backlog record for mango: %s and user: %s not found",
							requestData.getMangoTitle(), requestData.getUser()));
		}
		return Optional.empty();

	}
	
	private Mango generateNewMango(Long anilistId) {
		MangoInfo mangoInfo = mangoInfoService.getMangoInfoByAnilistId(anilistId);
		Mango newMango = new Mango();
		newMango = mangoUtil.setMangoInfo(mangoInfo);
		return newMango;
	}
	
	private boolean mangoHasFinishedPublication(Optional<Mango> mango, MangoInfo mangoInfo) {
		return mango.get().getStatus() != MangoStatus.FINISHED && mangoInfo.getData().getMedia().getStatus() == MangoStatus.FINISHED
				&& mangoInfo.getData().getMedia().getLastChapter() != null;
	}

	private boolean mangoStatusUpdateAvailable(MangoStatus currentStatus, MangoInfo mangoInfo) {
		return currentStatus != mangoInfo.getData().getMedia().getStatus();
	}

}
