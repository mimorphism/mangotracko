package com.mimorphism.mangotracko.appuser;

import lombok.AllArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mimorphism.mangotracko.exception.UserNotFoundException;
import com.mimorphism.mangotracko.registration.token.ConfirmationToken;
import com.mimorphism.mangotracko.registration.token.ConfirmationTokenService;
import com.mimorphism.mangotracko.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with username %s not found";

    private final AppUserRepo appUserRepo;
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return appUserRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, username)));
    }
    
    public AppUser getUser(String username) 
    throws UserNotFoundException
    {
    	return appUserRepo.findByUsername(username).
    			orElseThrow(() -> 
    					new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepo
                .findByUsername(appUser.getUsername())
                .isPresent();

        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            throw new IllegalStateException("user already taken");
        }

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepo.save(appUser);

//        String token = UUID.randomUUID().toString();
//
//        ConfirmationToken confirmationToken = new ConfirmationToken(
//                token,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(15),
//                appUser
//        );

      String token = SecurityUtil.generateAccessToken("mangotracko", appUser);
      ConfirmationToken confirmationToken = new ConfirmationToken(
      token,
      LocalDateTime.now(),
      LocalDateTime.now().plusMinutes(10),
      appUser
);

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

//        TODO: SEND EMAIL

        return token;
    }

	public int enableAppUser(String email) {
        return appUserRepo.enableAppUser(email);
    }
    

    
}