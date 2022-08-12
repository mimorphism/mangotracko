package com.mimorphism.mangotracko;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mimorphism.mangotracko.exception.AnilistMangoNotFoundException;
import com.mimorphism.mangotracko.mango.Mango;
import com.mimorphism.mangotracko.mango.MangoInfoService;
import com.mimorphism.mangotracko.mango.MangoRepo;
import com.mimorphism.mangotracko.mango.MangoService;
import com.mimorphism.mangotracko.mango.MangoStatus;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
@EnableAsync
public class MangotrackoApplication {
	
//	@Autowired
//    private MangoRepo mangoRepo;
//	
//	@Autowired
//    private MangoInfoService mangoInfoService;
//	
	public static void main(String[] args) {
		SpringApplication.run(MangotrackoApplication.class, args);
	}
	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasenames("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setDefaultLocale(new Locale("en"));
	    return messageSource;
	}
	
	@Bean
	  public CommandLineRunner demo(MangoRepo repository) {
	    return (args) -> {
	    	
	    	log.info("WE STARTING");
	    	
	    	
//	    	List<Mango> mangoes = mangoRepo.findAll();
//	    	
//	    	for(Mango mango: mangoes) {
//	    		try {
//	    		MangoInfo mangoInfo = mangoInfoService.getMangoInfo(mango.getMangoTitle());
//	    		if(mangoInfo.getData().getMedia().getStatus() != null) {
//	    			mango.setStatus(mangoInfo.getData().getMedia().getStatus());
//	    			if(mangoInfo.getData().getMedia().getStatus() == MangoStatus.FINISHED &&
//	    					mangoInfo.getData().getMedia().getLastChapter() != null) {
//	    				mango.setLastChapter(mangoInfo.getData().getMedia().getLastChapter());
//	    			}
//	    			mango.setAnilistId(mangoInfo.getData().getMedia().getAnilistId());
//	    			mangoRepo.save(mango);
//	    			log.info("FINISHED PROCESSING: " + mango.getMangoTitle());
//	    			log.info("REHAT SEBENTAR BERSAMA KITKAT");
//	    			Thread.sleep(2000);
//	    			
//	    		}
//	    		}catch(AnilistMangoNotFoundException ex) 
//	    		{
//	    			log.error("FINISHED PROCESSING: "+mango.getMangoTitle() + " BUT GOT ERROR BIYOTCH! MANGO NOT ON ANILIST");
//	    		}
//	    		
//	    		
//	    	}
//	    	
//	    	mangoInfoService.getMangoInfo("122131231");
	    	
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
}

//	private void recorrectOngoingMangoWiped(Mango mango) {
//		OngoingMango ongoingMango = mangoService.getOngoingMango(mango.getMangoTitle());
////		mango.setMangoStatus(MangoStatusType.ONGOING.getStatus());
////		mango.setOngoingMango(ongoingMango);
//		ongoingMango.setMango(mango);
//		mangoService.saveMango(mango);
//		mangoService.saveOngoingMango(ongoingMango);
//	}
//	    }
//}
