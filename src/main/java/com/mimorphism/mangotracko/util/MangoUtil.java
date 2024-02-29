package com.mimorphism.mangotracko.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.mango.Backlog;
import com.mimorphism.mangotracko.mango.CurrentlyReading;
import com.mimorphism.mangotracko.mango.Finished;
import com.mimorphism.mangotracko.mango.Mango;
import com.mimorphism.mangotracko.mango.MangoRepo;
import com.mimorphism.mangotracko.mango.MangoStatus;
import com.mimorphism.mangotracko.mango.dto.BacklogDTO;
import com.mimorphism.mangotracko.mango.dto.CurrentlyReadingDTO;
import com.mimorphism.mangotracko.mango.dto.FinishedDTO;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;

@Component
public class MangoUtil {

	private MessageSource messageSource;
	public static final String APP_ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
	public static final String DEFAULT_PAGE_NUMBER = "1";
	public static final String DEFAULT_PAGE_SIZE = "10";
	
	private static final String NO_REMARKS = "No remarks";

	public static final String SORT_BY_LAST_ACTIVITY = "LAST_ACTIVITY";

	public static final String SORT_BY_TITLE = "TITLE";



	public MangoUtil(MessageSource messageSource){
		this.messageSource = messageSource;
	}

	/**
	 * Convert the name to the proper order of Asian name (LastName FirstName)
	 * 
	 * @param name
	 * @return
	 */
	public String convertProperName(String name) {
		List<String> nameOrder = new ArrayList<String>(Arrays.asList(name.split("\\s+")));
		String lastName = nameOrder.get(nameOrder.size() - 1);
		nameOrder.remove(nameOrder.size() - 1);
		String firstName = String.join("", nameOrder);
		return lastName + " " + firstName;
	}

	public Mango setMangoInfo(MangoInfo mangoInfo) {
		Mango mango = new Mango();
		mango.setMangoTitle(mangoInfo.getData().getMedia().getTitle().getRomaji());
		mango.setImg(mangoInfo.getData().getMedia().getCoverImage().getLarge());
		mango.setBannerImg(mangoInfo.getData().getMedia().getBannerImage());
		mango.setAuthor(convertProperName(mangoInfo.getData().getMedia().getStaff().getNodes().get(0).getName().getFull()));
		if (mangoInfo.getData().getMedia().getLastChapter() != null) {
			mango.setStatus(MangoStatus.FINISHED);
		}else if (mangoInfo.getData().getMedia().getStatus() == MangoStatus.FINISHED && 
				mangoInfo.getData().getMedia().getLastChapter() == null) {
			mango.setStatus(MangoStatus.RELEASING);
		}else {
			mango.setStatus(mangoInfo.getData().getMedia().getStatus());
		}
		mango.setLastChapter(mangoInfo.getData().getMedia().getLastChapter());
		mango.setAnilistId(mangoInfo.getData().getMedia().getAnilistId());

		return mango;
	}

	public Finished generateFinished(FinishedDTO requestData, AppUser user, Mango mango) {
		Finished finishedMango = new Finished();
		finishedMango.setFinishedId(getListId(user.getId(), mango.getMangoId()));
		finishedMango.setCompletionDateTime(requestData.getCompletionDateTime());
		finishedMango.setMango(mango);
		finishedMango.setRemarks(getFinishedMangoRemarks(requestData.getRemarks()));
		finishedMango.setUser(user);
		return finishedMango;
	}

	public CurrentlyReading generateCurrentlyReading(CurrentlyReadingDTO requestData, AppUser user, Mango newMango) {
		CurrentlyReading currentlyReading = new CurrentlyReading();
		currentlyReading.setCurrentlyReadingId(getListId(user.getId(), newMango.getMangoId()));
		currentlyReading.setLastChapterRead(Integer.valueOf(requestData.getLastChapterRead()));
		currentlyReading.setLastReadTime(requestData.getLastReadTime());
		currentlyReading.setMango(newMango);
		currentlyReading.setUser(user);
		return currentlyReading;
	}

	public Backlog generateBacklog(BacklogDTO requestData, AppUser user, Mango newMango) {
		Backlog backlog = new Backlog();
		backlog.setBacklogId(getListId(user.getId(), newMango.getMangoId()));
		backlog.setAddedDateTime(requestData.getAddedDateTime());
		backlog.setMango(newMango);
		backlog.setUser(user);
		return backlog;
	}

	public String getMessage(String message) {
		try {
			return messageSource.getMessage(message, null, new Locale("en"));
		}catch(NoSuchMessageException ex) {
			return String.format("No message is found with message key: %s", message);
		}
		
	}

	private Long getListId(Long userId, Long mangoId) {
		String id = userId + "" + mangoId;
		return Long.valueOf(id);
	}
	
	public String getFinishedMangoRemarks(String requestData) {
		return requestData != null && !requestData.isEmpty() ? requestData
				: NO_REMARKS;
	}
}
