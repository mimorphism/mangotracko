package com.mimorphism.mangotracko;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mimorphism.mangotracko.controller.MangoController;
import com.mimorphism.mangotracko.model.Mango;
import com.mimorphism.mangotracko.model.MangoInfo;
import com.mimorphism.mangotracko.model.OngoingMango;
import com.mimorphism.mangotracko.persistence.MangoRepo;
import com.mimorphism.mangotracko.service.MangoInfoService;
import com.mimorphism.mangotracko.service.MangoService;


@EnableJpaRepositories("com.mimorphism.mangotracko.persistence") 
@EntityScan("com.mimorphism.mangotracko.model")
@ComponentScan
@SpringBootApplication
public class MangotrackoApplication {

	
	private static final Logger log = LoggerFactory.getLogger(MangotrackoApplication.class);
	
	@Autowired
    private MangoService mangoService;
    
    @Autowired
    private MangoInfoService anilist;
	
	
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
	    	List<Mango> mangoes = mangoService.getAllMangoes();
	    	MangoInfo info;
	    	
	    	for(Mango mango: mangoes) 
	    	{
	    		if(mango.getImg() == null && mango.getAuthor() == null) 
	    		{
	    			try 
	    			{
		    			info = anilist.getMangoInfo(mango.getMangoTitle());
		    			mango.setMangoTitle(mango.getMangoTitle().trim());
		    			mango.setImg(info.getCoverImage());
		    			mango.setAuthor(info.getStaff());
		    			mangoService.completedMangoBuilder(mango);
		    			log.info("SAVE MANGO WITH INFO SUCCESS: " + mango.getMangoTitle());

	    			}catch(RuntimeException e)
	    			{
	    				
	    			}
	    	}
	    		}
	    };
	  }

}
