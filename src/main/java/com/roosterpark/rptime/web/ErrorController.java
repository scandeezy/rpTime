package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorController {

	@RequestMapping(value = "/error", method = { GET })
	@ResponseBody
	public Map<String, Object> handle(HttpServletRequest request) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", request.getAttribute("javax.servlet.error.status_code"));
		map.put("reason", request.getAttribute("javax.servlet.error.message"));
		map.put("abc", request.getAttribute("abc"));
		map.put("wnfe", request.getAttribute("wnfe"));

		return map;
	}

}