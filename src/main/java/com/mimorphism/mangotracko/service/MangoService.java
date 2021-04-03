package com.mimorphism.mangotracko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mimorphism.mangotracko.enums.MangoStatusType;
import com.mimorphism.mangotracko.exception.MangoNotFoundException;
import com.mimorphism.mangotracko.model.Mango;
import com.mimorphism.mangotracko.model.MangoInfo;
import com.mimorphism.mangotracko.model.OngoingMango;
import com.mimorphism.mangotracko.persistence.MangoRepo;
import com.mimorphism.mangotracko.persistence.OngoingMangoRepo;
import com.mimorphism.mangotracko.util.MangoUtil;

@Service
public class MangoService{

	@Autowired
    private MangoRepo mangoRepo;
	
	@Autowired
	private OngoingMangoRepo ongoingMangoRepo;
	
	@Autowired
    private MangoInfoService anilist;
	
	public Mango completedMangoBuilder(Mango mango) {
		mango.setAuthor(MangoUtil.convertProperName(mango.getAuthor()));
		return mangoRepo.save(mango);
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
		MangoInfo mangoInfo = anilist.getMangoInfo(mango.getMangoTitle());
		newMango.setImg(mangoInfo.getCoverImage());
		newMango.setAuthor((MangoUtil.convertProperName(mangoInfo.getStaff())));
		mangoRepo.save(newMango);
		
		return ongoingMangoRepo.save(mango);		
	}
	
	public OngoingMango saveBacklog(OngoingMango mango) 
	{
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
