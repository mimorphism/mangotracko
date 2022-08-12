package com.mimorphism.mangotracko.registration;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mimorphism.mangotracko.exception.handler.ApiError;
import com.mimorphism.mangotracko.exception.handler.ResponseHandler;
import com.mimorphism.mangotracko.util.MangoUtil;

@RestController
@RequestMapping(path = "/auth/registration")
@AllArgsConstructor
public class RegistrationController {
	
	private static final String USERNAME_ALREADY_TAKEN = "RegistrationController.USERNAME_ALREADY_TAKEN";

    private final RegistrationService registrationService;
    
    @Autowired
    private MangoUtil mangoUtil;

    @PostMapping
    ResponseEntity<?> register(@Valid @RequestBody  RegistrationRequest request) {
    	try 
    	{
    		return ResponseEntity.ok().body(registrationService.register(request));

    	}catch(IllegalStateException ex) 
    	{
    		return ResponseHandler.buildResponseEntity(new ApiError(BAD_REQUEST, String.format(mangoUtil.getMessage(USERNAME_ALREADY_TAKEN), request.getUsername())));    				
    	}
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}