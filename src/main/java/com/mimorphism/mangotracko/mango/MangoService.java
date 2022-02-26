package com.mimorphism.mangotracko.mango;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mimorphism.mangotracko.mango.mangoinfo.model.anilist.MangoInfo;
import com.mimorphism.mangotracko.util.MangoUtil;

@Service
public class MangoService{

	@Autowired
    private MangoRepo mangoRepo;
	
	@Autowired
	private OngoingMangoRepo ongoingMangoRepo;
	
	@Autowired
    private MangoInfoService mangoInfoService;
	
	public Mango completedMangoBuilder(Mango mango) {
		Mango existingMango = mangoRepo.findByMangoTitle(mango.getMangoTitle());
		
		if(existingMango == null)  
		{
			existingMango = mango;
		}
		existingMango.setOngoingMango(null);
		existingMango.setMangoStatus(MangoStatusType.COMPLETED.getStatus());
		existingMango.setCompletionDateTime(mango.getCompletionDateTime());
		existingMango.setRemarks(mango.getRemarks());
		return mangoRepo.save(existingMango);
	}
	
	public List<Mango> getCompletedMangoes()
	{
		return (List<Mango>) mangoRepo.findByMangoStatus(MangoStatusType.COMPLETED.getStatus());
	}
	
	public List<Mango> getOngoingMangoes()
	{
		return (List<Mango>) mangoRepo.findByMangoStatus(MangoStatusType.ONGOING.getStatus());
	}
	
	public List<Mango> getAllMangoes()
	{
		return (List<Mango>) mangoRepo.findAll();
	}
	
	public OngoingMango newMangoBuilder(OngoingMango mango) {
		
		Mango newMango = new Mango();
		newMango.setMangoTitle(mango.getMangoTitle());
		newMango.setMangoStatus(MangoStatusType.ONGOING.getStatus());
		newMango.setOngoingMango(mango);
		mango.setMango(newMango);
		
		
		MangoInfo mangoInfo = mangoInfoService.getMangoInfo(mango.getMangoTitle());
		
		if(mangoInfo != null) 
		{
			newMango.setImg(mangoInfo.getData().getMedia().getCoverImage().getLarge());
			newMango.setAuthor((MangoUtil.convertProperName(mangoInfo.getData().getMedia().getStaff().getNodes().get(0).getName().getFull())));
		}
		
		mangoRepo.save(newMango);
		
		return ongoingMangoRepo.save(mango);		
	}
	
	public OngoingMango backlogMangoBuilder(OngoingMango mango) {
		
		if(MangoUtil.isMangoExist(mangoRepo, mango.getMangoTitle())) 
		{
			OngoingMango backlog = ongoingMangoRepo.findByMangoTitle(mango.getMangoTitle());
			Mango existingMango = mangoRepo.findByMangoTitle(mango.getMangoTitle());
			mango.setBacklogID(backlog.getBacklogID());
			mango.setMango(existingMango);
			return ongoingMangoRepo.save(mango);
		}
		else {
			throw new ResponseStatusException(
	                 HttpStatus.NOT_FOUND, "Mango Not Found");
		}
	}
	
	public Mango getMango(String mangoTitle) {
		return mangoRepo.findByMangoTitle(mangoTitle);
	}

}
