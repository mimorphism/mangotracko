package com.mimorphism.mangotracko.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mimorphism.mangotracko.model.Mango;
import com.mimorphism.mangotracko.model.MangoInfo;
import com.mimorphism.mangotracko.model.OngoingMango;
import com.mimorphism.mangotracko.persistence.MangoRepo;
import com.mimorphism.mangotracko.service.MangoInfoService;
import com.mimorphism.mangotracko.service.MangoService;


@RestController
@RequestMapping("/")
public class MangoController {
	
    @Value("${spring.application.name}")
    String appName;
    
    @Autowired
    private MangoService mangoService;
    
    @Autowired
    private MangoInfoService anilist;
 
    @RequestMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
    
//    @GetMapping("/title/{mangoTitle}")
//    public Mango findMango(@PathVariable String mangoTitle) {
//        return mangoRepo.findByMangoTitle(mangoTitle);
//    }
    
    @CrossOrigin
    @GetMapping("/completedMangoes")
    List<Mango> getMangoes() {
    	return mangoService.getCompletedMangoes();	
    }
    
    @CrossOrigin
    @GetMapping("/ongoingMangoes")
    List<Mango> getOngoingMangoes() {
    	return mangoService.getOngoingMangoes();	
    }
    
//    @GetMapping("/addmango")
//    public String showMangoForm(Model model) 
//    {
//    	model.addAttribute("appName", appName);
//    	return "mangoForm";
//    }
    
//    @GetMapping("/mango")
//    @CrossOrigin
//    public Mango getMango(@RequestParam(value = "title", defaultValue = "Naruto") String title) 
//    {
//    	return findMango(title);
//    	
//    }
    @CrossOrigin
    @PostMapping("/completedMango")
    Mango completedMango(@RequestBody Mango mango) 
    {
       return mangoService.completedMangoBuilder(mango);
    }
    
    @CrossOrigin
    @PutMapping("/updateBacklog")
    OngoingMango updateBacklog(@RequestBody OngoingMango mango) 
    {
       return mangoService.backlogMangoBuilder(mango);
    }
    
    @CrossOrigin
    @PostMapping("/newMango")
    OngoingMango newMango(@RequestBody OngoingMango mango) 
    {
       return mangoService.newMangoBuilder(mango);
    }
    
    
    @CrossOrigin
    @GetMapping("/getMangoInfo/{mangoTitle}")
    void updateBacklog(@PathVariable String mangoTitle) 
    {
    	anilist.getMangoInfo(mangoTitle);
    }
    
} 
