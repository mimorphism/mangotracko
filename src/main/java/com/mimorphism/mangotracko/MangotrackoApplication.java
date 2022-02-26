package com.mimorphism.mangotracko;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.mimorphism.mangotracko.mango.MangoRepo;


@SpringBootApplication
public class MangotrackoApplication {

	
	private static final Logger log = LoggerFactory.getLogger(MangotrackoApplication.class);
	
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
//	    	
//	    	for(Mango mango: mangoes) 
//	    	{
//	    		if(mango.getImg() == null && mango.getAuthor() == null) 
//	    		{
//	    			try 
//	    			{
//		    			info = anilist.getMangoInfo(mango.getMangoTitle());
//		    			mango.setMangoTitle(mango.getMangoTitle().trim());
//		    			mango.setImg(info.getCoverImage());
//		    			mango.setAuthor(info.getStaff());
//		    			mangoService.completedMangoBuilder(mango);
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
//}
