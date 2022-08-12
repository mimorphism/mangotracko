package com.mimorphism.mangotracko;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mimorphism.mangotracko.mango.Mango;
import com.mimorphism.mangotracko.mango.MangoInfoService;
import com.mimorphism.mangotracko.mango.MangoRepo;
import com.mimorphism.mangotracko.mango.MangoService;
import com.mimorphism.mangotracko.mango.MangoStatusType;
import com.mimorphism.mangotracko.mango.OngoingMango;
import com.mimorphism.mangotracko.mango.mangoinfo.model.anilist.MangoInfo;


@SpringBootApplication
public class MangotrackoApplication {

	
	private static final Logger log = LoggerFactory.getLogger(MangotrackoApplication.class);
	
	@Autowired
    private MangoService mangoService;
	
	@Autowired
    private MangoInfoService mangoInfoService;
	
	public static void main(String[] args) {
		SpringApplication.run(MangotrackoApplication.class, args);
	}
	
	@Bean
	  public CommandLineRunner demo(MangoRepo repository) {
	    return (args) -> {
	    	
//	    	List<OngoingMango> mangoes = mangoService.getOngoingMangoes();
//	    	for(OngoingMango mango: mangoes) 
//	    	{
//    			mango.setMangoTitle(mango.getMangoTitle().trim());
//    			mangoService.saveBacklog(mango);

//	    	}
//	    	List<Mango> mangoes = mangoService.getAllMangoes();
//	    	MangoInfo info;
	    	
//	    	for(Mango mango: mangoes) 
//	    	{
//	    		if(mango.getImg() == null && mango.getAuthor() == null) 
//	    		if(mango.getBannerImg() == null) 
//	    		if(mango.getCompletionDateTime() == null && mango.getRemarks() == null)

//	    		{
//	    			try 
//	    			{
//		    			info = mangoInfoService.getMangoInfo(mango.getMangoTitle());
//		    			mango.setBannerImg(info.getData().getMedia().getBannerImage());
//	    				recorrectOngoingMangoWiped(mango);
//		    			mangoService.saveMango(mango);
//		    			log.info("SAVE MANGO WITH INFO SUCCESS: " + mango.getMangoTitle());
//
//	    			}catch(RuntimeException e)
//	    			{
//	    				
//	    			}
//	    	}
//	    		}
	    };
	  }

	private void recorrectOngoingMangoWiped(Mango mango) {
		OngoingMango ongoingMango = mangoService.getOngoingMango(mango.getMangoTitle());
		mango.setMangoStatus(MangoStatusType.ONGOING.getStatus());
		mango.setOngoingMango(ongoingMango);
		ongoingMango.setMango(mango);
		mangoService.saveMango(mango);
		mangoService.saveOngoingMango(ongoingMango);
	}
	    }
//}
