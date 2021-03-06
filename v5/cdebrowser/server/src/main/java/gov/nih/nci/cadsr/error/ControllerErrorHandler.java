package gov.nih.nci.cadsr.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerErrorHandler
{
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody String handleException(RestControllerException ex) 
	{
		String errorMessage = ex.getMessage();
		return errorMessage;
	}  
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody String handleException(AutheticationFailureException ex) 
	{
		String errorMessage = ex.getMessage();
		return errorMessage;
	}
	
}