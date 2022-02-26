package com.mimorphism.mangotracko;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {

	
	 @Value("${spring.application.name}")
	    String appName;
	 
	 @RequestMapping("/")
	    public ModelAndView homePage() {
	        ModelAndView homePage = new ModelAndView();
	        homePage.setViewName("ongoingMango.html");
	        return homePage;
	    }
	    
	    
}
