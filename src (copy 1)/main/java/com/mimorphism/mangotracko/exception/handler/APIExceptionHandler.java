package com.mimorphism.mangotracko.exception.handler;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static com.mimorphism.mangotracko.exception.handler.ResponseHandler.buildResponseEntity;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.mimorphism.mangotracko.exception.AnilistMangoNotFoundException;
import com.mimorphism.mangotracko.exception.MangoAlreadyExistsException;
import com.mimorphism.mangotracko.exception.MangoInfoServiceProviderUnreachableException;
import com.mimorphism.mangotracko.exception.BadRequestException;
import com.mimorphism.mangotracko.exception.UserNotFoundException;
import com.mimorphism.mangotracko.util.MangoUtil;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
		
	@Autowired
	private MangoUtil mangoUtil;
	
	private static final String HTTP_MSG_NOT_READABLE_XCEPTION_MSG = "APIExceptionHandler.HTTP_MESSAGE_NOT_READABLE_EXCEPTION";
	private static final String ANILIST_MANGO_NOT_FOUND="MangoService.ANILIST_MANGO_NOT_FOUND";
    private static final String REQUEST_FAILED_SERVER_ERROR = "Server error: %s";


   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mangoUtil.getMessage(HTTP_MSG_NOT_READABLE_XCEPTION_MSG)));
   }
   
   @ExceptionHandler(BadRequestException.class)
   protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
	   
	   if(!ex.getErrorMsgs().isEmpty()) {
	       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getErrorMsgs()));

	   }else {
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
   }}
   
   @ExceptionHandler(AnilistMangoNotFoundException.class)
   protected ResponseEntity<Object> handleAnilistMangoNotFound(AnilistMangoNotFoundException ex) {
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mangoUtil.getMessage(ANILIST_MANGO_NOT_FOUND)));
       
   }
   
   @ExceptionHandler(UserNotFoundException.class)
   protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
       
   }
   
   @ExceptionHandler(MangoAlreadyExistsException.class)
   protected ResponseEntity<Object> handleMangoAlreadyExists(MangoAlreadyExistsException ex) {
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
       
   }
   
   @ExceptionHandler(MangoInfoServiceProviderUnreachableException.class)
   protected ResponseEntity<Object> handleMangoInfoServiceProviderUnreachable(MangoInfoServiceProviderUnreachableException ex) {
       return buildResponseEntity(new ApiError(HttpStatus.SERVICE_UNAVAILABLE, mangoUtil.getMessage(ex.getMessage())));
       
   }
   
   @Override
   public ResponseEntity<Object> handleMethodArgumentNotValid(
		   MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	   List<String> errorMsg = new ArrayList<>();
       ex.getBindingResult().getAllErrors().forEach((error) -> {
           String errorMessage = ((FieldError) error).getField() + ":" +error.getDefaultMessage();
           errorMsg.add(errorMessage);
       });
	   return buildResponseEntity(new ApiError(BAD_REQUEST, errorMsg));

   }
   
   @ExceptionHandler(ConstraintViolationException.class)
   protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
	   List<String> errorMsg = new ArrayList<>();
      ex.getConstraintViolations().forEach((error) -> {
           String errorMessage = ((FieldError) error).getField() + ":" +error.getMessage();
           errorMsg.add(errorMessage);
       });
      
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mangoUtil.getMessage(ex.getMessage())));
       
   }
   
   @ExceptionHandler(Exception.class)
   public ResponseEntity<Object> exception(Exception ex) {
	   return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, String.format(REQUEST_FAILED_SERVER_ERROR, ex.getMessage())));
     
   }
   
}