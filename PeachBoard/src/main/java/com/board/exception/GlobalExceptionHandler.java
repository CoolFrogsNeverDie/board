package com.board.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.board.enums.ErrorMessages;

import groovy.util.logging.Slf4j;

/**
 * GlobalExceptionHandler.java
 * 
 * exception 핸들러
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	/**
	 * exception 핸들러
	 * @param ex
	 * @param model
	 * @return errorMsg, error 페이지
	 */
	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex, Model model) 
	{
		LOG.error("==================================================================================");
		LOG.error("==================================================================================");
		LOG.error("handleGeneralException error : {}",  ex.getMessage(), ex);
		LOG.error("==================================================================================");
		LOG.error("==================================================================================");

		model.addAttribute("errorMsg", ex.getMessage());
		return "error/error"; // error 페이지
	}

	/**
	 * NoResourceFoundException 핸들러
	 * @param ex
	 * @param model
	 * @return errorMsg, error 페이지
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResourceFoundException(NoResourceFoundException ex, Model model) 
	{
		LOG.error("==================================================================================");
		LOG.error("==================================================================================");
		LOG.error("handleNoResourceFoundException error : {}", ex.getMessage(), ex);
		LOG.error("==================================================================================");
		LOG.error("==================================================================================");
		model.addAttribute("errorMsg", ErrorMessages.PAGE_NOT_FOUND.getMessage()); // 페이지를 찾지 못했습니다.
		return "error/error"; // error 페이지
	}
}
