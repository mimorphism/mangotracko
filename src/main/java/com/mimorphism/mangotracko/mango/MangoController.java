
package com.mimorphism.mangotracko.mango;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.appuser.AppUserService;
import com.mimorphism.mangotracko.exception.AnilistMangoNotFoundException;
import com.mimorphism.mangotracko.exception.InvalidTokenException;
import com.mimorphism.mangotracko.exception.MangoAlreadyExistsException;
import com.mimorphism.mangotracko.exception.MangoNotFoundException;
import com.mimorphism.mangotracko.exception.RecordNotFoundException;
import com.mimorphism.mangotracko.exception.UserNotFoundException;
import com.mimorphism.mangotracko.mango.dto.BacklogDTO;
import com.mimorphism.mangotracko.mango.dto.CurrentlyReadingDTO;
import com.mimorphism.mangotracko.mango.dto.DeleteRecordDTO;
import com.mimorphism.mangotracko.mango.dto.FinishedDTO;
import com.mimorphism.mangotracko.mango.userstats.pojo.UserStats;
import com.mimorphism.mangotracko.payload.PagedResponse;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWT;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTService;
import com.mimorphism.mangotracko.util.MangoUtil;
import com.mimorphism.mangotracko.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class MangoController {

	@Autowired
	private MangoService mangoService;

	@Autowired
	private AppUserService userService;

	@Autowired
	private ActiveJWTService activeJWTService;

	@Autowired
	private MangoUtil mangoUtil;

	private static final String REQUEST_SUCCESFUL = "Controller.REQUEST_SUCCESFUL";

	/**
	 * PagedResponse of Finished records that contains
	 * page information such as:
	 * <p> (1)Total records (2)Total pages (3)Current page
	 * <p> As Pageable's first page starts at 0, the page request
	 * <p> parameter has to be manipulated to subtract 1 inbound
	 * <p> and plus 1 outbound
	 * @param user
	 * @param page
	 * @param size
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/finished")
	public PagedResponse<Finished> getFinished(Principal user,
			@RequestParam(value = "page", defaultValue = MangoUtil.DEFAULT_PAGE_NUMBER) int page,

			@RequestParam(value = "size", defaultValue = MangoUtil.DEFAULT_PAGE_SIZE) int size) {
		PagedResponse<Finished> response = new PagedResponse<>();

		// pagination from the client side starts at 1 while Page starts at 0
		page -= 1;
		// first page
		if (page == 0) {
			Page<Finished> firstPage = mangoService.getFinishedPage(user.getName(), page, size);
			response.setTotalPages(firstPage.getTotalPages());
			response.setContent(firstPage.getContent());
			response.setTotalRecords(firstPage.getTotalElements());
			response.setCurrentPage(page+1);
		} else {
			response.setContent(mangoService.getFinished(user.getName(), page, size));
			response.setCurrentPage(page+1);
		}

		return response;

	}

	/**
	 * PagedResponse of CurrentlyReading records that contains
	 * page information such as:
	 * <p> (1)Total records (2)Total pages (3)Current page
	 * <p> As Pageable's first page starts at 0, the page request
	 * <p> parameter has to be manipulated to subtract 1 inbound
	 * <p> and plus 1 outbound
	 * @param user
	 * @param page
	 * @param size
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/currentlyReading")
	PagedResponse<CurrentlyReading> getCurrentlyReading(Principal user,
			@RequestParam(value = "page", defaultValue = MangoUtil.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = MangoUtil.DEFAULT_PAGE_SIZE) int size) {
		PagedResponse<CurrentlyReading> response = new PagedResponse<>();

		// pagination from the client side starts at 1 while Page starts at 0
		page -= 1;
		// first page
		if (page == 0) {
			Page<CurrentlyReading> firstPage = mangoService.getCurrentlyReadingPage(user.getName(), page, size);
			response.setTotalPages(firstPage.getTotalPages());
			response.setContent(firstPage.getContent());
			response.setTotalRecords(firstPage.getTotalElements());
			response.setCurrentPage(page+1);

		} else {
			response.setContent(mangoService.getCurrentlyReading(user.getName(), page, size));
			response.setCurrentPage(page+1);

		}

		return response;
	}

	/**
	 * PagedResponse of Backlog records that contains
	 * page information such as:
	 * <p> (1)Total records (2)Total pages (3)Current page
	 * <p> As Pageable's first page starts at 0, the page request
	 * <p> parameter has to be manipulated to subtract 1 inbound
	 * <p> and plus 1 outbound
	 * @param user
	 * @param page
	 * @param size
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/backlog")
	PagedResponse<Backlog> getBacklog(Principal user,
			@RequestParam(value = "page", defaultValue = MangoUtil.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = MangoUtil.DEFAULT_PAGE_SIZE) int size) {
		PagedResponse<Backlog> response = new PagedResponse<>();

		// pagination from the client side starts at 1 while Page starts at 0
		page -= 1;
		// first page
		if (page == 0) {
			Page<Backlog> firstPage = mangoService.getBacklogPage(user.getName(), page, size);
			response.setTotalPages(firstPage.getTotalPages());
			response.setContent(firstPage.getContent());
			response.setTotalRecords(firstPage.getTotalElements());
			response.setCurrentPage(page+1);
		} else {
			response.setContent(mangoService.getBacklog(user.getName(), page, size));
			response.setCurrentPage(page+1);
		}
		return response;
	}

	@CrossOrigin
	@PostMapping("/finishMango")
	ResponseEntity<?> finishedMangoBuilder(@Valid @RequestBody FinishedDTO mango) {
//    	try {
		mangoService.generateFinishedRecord(mango);
		return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
//    	} catch (UnknownHostException e) {
//    		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Anilist is down");
//		} 
	}

	@CrossOrigin
	@PutMapping("/updateMango")
	ResponseEntity<?> updateMango(@Valid @RequestBody CurrentlyReadingDTO mango) {
		try {
			mangoService.updateCtlyReadingRecord(mango);
			return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
		} catch (UserNotFoundException ex) {
			return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
		} catch (MangoNotFoundException ex) {
			return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
		}
	}

	@CrossOrigin
	@PutMapping("/updateMangoFinish")
	ResponseEntity<?> updateMangoFinish(@Valid @RequestBody CurrentlyReadingDTO mango) {
		try {
			mangoService.updateCtlyReadingRecordAndFinish(mango);
			return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
		} catch (UserNotFoundException ex) {
			return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
		} catch (MangoNotFoundException ex) {
			return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/newMango/currentlyReading")
	ResponseEntity<?> newCurrentlyReading(@Valid @RequestBody CurrentlyReadingDTO mango) {
		try {
			mangoService.generateCurrentlyReadingRecord(mango);
			return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
		} catch (UserNotFoundException ex) {
			return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
		} catch (MangoAlreadyExistsException ex) {
			return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
		} catch (AnilistMangoNotFoundException e) {
			return ResponseEntity.status(BAD_REQUEST)
					.body(String.format("Mango %s doesn't exist in Anilist", mango.getMangoTitle()));
//		} catch (UnknownHostException e) {
//    		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Anilist is down");
//		} 
		}
	}

	@CrossOrigin
	@PostMapping("/newMango/backlog")
	ResponseEntity<?> newBacklog(@Valid @RequestBody BacklogDTO mango) {
		try {
			mangoService.generateBacklogRecord(mango);
			return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
		} catch (AnilistMangoNotFoundException e) {
			return ResponseEntity.status(BAD_REQUEST)
					.body(String.format("Mango %s doesn't exist in Anilist", mango.getMangoTitle()));
//		} catch (UnknownHostException e) {
//    		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Anilist is down");
//		} 
		}
	}

	@CrossOrigin
	@PutMapping("/updateMangoStatus")
	ResponseEntity<?> updateMangoStatus(@Valid @RequestBody @Min(1) Long anilistId) {
		try {
			mangoService.updateMangoPublicationStatus(anilistId);
			return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
		} catch (AnilistMangoNotFoundException e) {
			return ResponseEntity.status(BAD_REQUEST).body("Mango %s doesn't exist in Anilist");
		}
	}

	@CrossOrigin
	@PutMapping("/deleteRecord")
	ResponseEntity<?> deleteRecord(@Valid @RequestBody DeleteRecordDTO requestData) {

		mangoService.deleteRecord(requestData);
		return ResponseEntity.ok().body(mangoUtil.getMessage(REQUEST_SUCCESFUL));
	}

    @CrossOrigin
    @GetMapping("/existingmangorecords")
    List<MangoRecordType> getExistingMangoRecords(Principal user) {
    	return mangoService.getUserExistingMangoRecords(user.getName());	
    }
    
    @CrossOrigin
    @GetMapping("/userstats")
    ResponseEntity<UserStats> getUserStats(Principal user){
    	return ResponseEntity.ok().body(mangoService.getUserStats(user.getName()));
    }
    
    

	@CrossOrigin
	@GetMapping("/refreshtoken")
	void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authHeader = request.getHeader(AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith(SecurityUtil.BEARER)) {
			try {
				String refreshToken = authHeader.substring(SecurityUtil.BEARER.length());
				JWTVerifier verifier = JWT.require(SecurityUtil.getAlgorithm()).build();
				DecodedJWT decodedJWT = verifier.verify(refreshToken);
				String username = decodedJWT.getSubject();

				Optional<ActiveJWT> userActiveJWT = activeJWTService.getActiveJWTForUser(username);
				if (userActiveJWT.isPresent() && userActiveJWT.get().getRefreshToken().equals(refreshToken)) {
					AppUser user = userService.getUser(username);
					String accessToken = SecurityUtil.generateAccessToken(request.getRequestURL().toString(), user);
					activeJWTService.setActiveJWTForUser(username, accessToken, userActiveJWT.get().getRefreshToken());
					Map<String, String> tokens = new HashMap<>();
					tokens.put("user", user.getUsername());
					tokens.put("accessToken", accessToken);
					tokens.put("refreshToken", refreshToken);
					response.setContentType(APPLICATION_JSON_VALUE);

					new ObjectMapper().writeValue(response.getOutputStream(), tokens);

				} else {
					throw new InvalidTokenException("Invalid refresh token!");
				}
			} catch (InvalidTokenException ex) {
				log.error("Error refreshing token: {}", ex.getMessage());
				SecurityUtil.writeResponse(response, FORBIDDEN, ex.getMessage());
			} catch (UserNotFoundException ex) {
				log.error("Error refreshing token: {}", ex.getMessage());
				SecurityUtil.writeResponse(response, BAD_REQUEST, ex.getMessage());
			}
		} else {
			SecurityUtil.writeResponse(response, FORBIDDEN, "Can't use this without credentials buddy!");
		}
	}
}
