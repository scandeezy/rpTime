package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.users.UserService;

@Controller
public class ErrorController {

	@Inject
	UserService userService;

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

	@RequestMapping(value = "/security/anyone", method = GET)
	@ResponseBody
	public Object getAnyone(HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("user", userService.getCurrentUser());
		result.put("isAdmin", userService.isUserAdmin());
		result.put("msg", "security anyone ok");
		return result;
	}

	@RequestMapping(value = "/security/admin", method = GET)
	@ResponseBody
	public Object getAdmin(HttpServletRequest request) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("user", userService.getCurrentUser());
		result.put("isAdmin", userService.isUserAdmin());
		result.put("msg", "security admin ok");
		return result;
	}

}