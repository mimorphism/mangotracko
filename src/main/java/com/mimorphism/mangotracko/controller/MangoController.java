package com.mimorphism.mangotracko.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mimorphism.mangotracko.model.Mango;
import com.mimorphism.mangotracko.persistence.MangoRepo;


@RestController
@RequestMapping("/")
public class MangoController {
	
    @Value("${spring.application.name}")
    String appName;
    
    @Autowired
    private MangoRepo mangoRepo;
    
    private List<Mango> mangoList = new ArrayList<Mango>();
    private final AtomicLong counter = new AtomicLong();
 
    @RequestMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
    
    @GetMapping("/title/{mangoTitle}")
    public Mango findMango(@PathVariable String mangoTitle) {
        return mangoRepo.findByMangoTitle(mangoTitle);
    }
    
    @GetMapping("/mangoes")
    public Iterable<Mango> getMangoes() {
    	return mangoRepo.findAll();	
    }
    
//    @GetMapping("/addmango")
    public String showMangoForm(Model model) 
    {
    	model.addAttribute("appName", appName);
    	return "mangoForm";
    }
    
    @GetMapping("/mango")
    public Mango getMango(@RequestParam(value = "title", defaultValue = "Naruto") String title) 
    {
    	return findMango(title);
    	
    }
    
    @PostMapping("/addmango")
    public Mango newMango(@RequestBody Mango mango) 
    {
      return mangoRepo.save(mango);
    }
    
} 
