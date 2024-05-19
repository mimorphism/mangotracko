package com.mimorphism.mangotracko.security.config;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.mimorphism.mangotracko.appuser.AppUserRole;
import com.mimorphism.mangotracko.appuser.AppUserService;
import com.mimorphism.mangotracko.security.CustomLogoutHandler;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTRepository;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTService;
import com.mimorphism.mangotracko.security.filter.CustomAuthenticationFilter;
import com.mimorphism.mangotracko.security.filter.CustomAuthorizationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomLogoutHandler customLogoutHandler;
	private final ActiveJWTService activeJWTService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                	.antMatchers("/**").permitAll()
//                	.antMatchers("/login").permitAll()
//                    .antMatchers("/static/css/**", "/static/js/**", "/static/img/**", "**/favicon.ico").permitAll()
//                .anyRequest()
//                .authenticated().and()
//                .formLogin()
//                .loginPage("/login.html")
//    	.logout() // (6)
//        .permitAll() 
//        .and()
//      .httpBasic(); // (7)
    	http
        .headers()
        .xssProtection()
        .and()
        .contentSecurityPolicy("script-src 'self'");
    	http.cors().configurationSource(request -> {
    	      var cors = new CorsConfiguration();
//    	      cors.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:80", "http://example.com"));//EXAMPLE OF ALLOWED ORIGINS
    	      cors.setAllowedOrigins(List.of("http://192.168.0.2:3000", "http://localhost:3000", "https://mangotracko.mimorphism.cc"));
    	      cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE"));
    	      cors.setAllowedHeaders(List.of("*"));
    	      return cors;
    	    }).and()
    	.csrf().disable();
    	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	http.authorizeRequests().antMatchers("/auth/login", "/auth/registration/**", "/api/user/refreshtoken", "/*").permitAll();
    	http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority(AppUserRole.USER.role).and().addFilterBefore(new CustomAuthorizationFilter(activeJWTService), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), appUserService, activeJWTService));
        http.logout().logoutUrl("/logout").addLogoutHandler(customLogoutHandler).permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
    
    
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
    	return super.authenticationManagerBean();
    }
}