package gov.nih.nci.cadsr.service.restControllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/**
 * This is a logger interceptor for RESTful calls
 * @author asafievan
 *
 */
@Component("restUsageInterceptor")
public class RestUsageInterceptor extends HandlerInterceptorAdapter
{
	private static final Logger logger = LogManager.getLogger(RestUsageInterceptor.class.getName());
	
	protected static Level useLevel = Level.INFO;//consider to make it parameter
	public static final String REST_CONTROLLER_USAGE_PREFIX = "CDE_BROWSER_REST_CONTROLLER_USAGE";
    public static final String LOG_DATE_TIME_FORMAT = "YYYY:MM:dd:HH:mm";
	public static final String REST_USAGE_LOG_REQUEST_FORMAT = "[%s][Request %s][%s][%s][%s]";//prefix time method URI query
	public static final String REST_USAGE_LOG_RESPONSE_FORMAT = "[%s][Response %s][response code %d][%s][%s][%s]";//prefix time code method URI query
	//prefix method URI query Date
	//we can consider to add log request parameters for POST requests
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		LocalDateTime now = LocalDateTime.now();
		String formattedCurrentDate = now.format( DateTimeFormatter.ofPattern( LOG_DATE_TIME_FORMAT));
		String toLog = String.format(REST_USAGE_LOG_REQUEST_FORMAT, REST_CONTROLLER_USAGE_PREFIX, formattedCurrentDate, request.getMethod(), request.getRequestURI(),request.getQueryString());
		logger.log(useLevel, toLog);
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		int responseCode = response.getStatus();
		LocalDateTime now = LocalDateTime.now();
		String formattedCurrentDate = now.format( DateTimeFormatter.ofPattern( LOG_DATE_TIME_FORMAT));
		String toLog = String.format(REST_USAGE_LOG_RESPONSE_FORMAT, REST_CONTROLLER_USAGE_PREFIX, formattedCurrentDate, responseCode, request.getMethod(), request.getRequestURI(),request.getQueryString());
		logger.log(useLevel, toLog);
	}

	public static Level getUseLevel() {
		return useLevel;
	}

	public static void setUseLevel(Level useLevel) {
		RestUsageInterceptor.useLevel = useLevel;
	}
	
}