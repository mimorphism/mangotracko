package com.mimorphism.mangotracko.mango;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MangoController {
	
    @Autowired
    private MangoService mangoService;
    
    
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
    @GetMapping("/findMango/{mangoTitle}")
    Mango getMango(@PathVariable String mangoTitle) {
    	return mangoService.getMango(mangoTitle);	
    }
} 
