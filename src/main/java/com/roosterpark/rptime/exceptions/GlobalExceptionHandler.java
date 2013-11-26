package com.roosterpark.rptime.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public @ResponseBody
	ModelAndView handleBusinessException(final WorkerNotFoundException wnfe) {
		ModelAndView mav = new ModelAndView("error");
		mav.addObject("wnfe", wnfe);
		return mav;
	}

}
