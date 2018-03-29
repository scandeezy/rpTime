package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.model.UnlinkedUser;
import com.roosterpark.rptime.service.UnlinkedUserService;

@Controller
public class UnlinkedUserController {
	@Inject
	UnlinkedUserService unlinkedUserService;

	@RequestMapping(value = "/admin/unlinkeduser", method = GET)
	@ResponseBody
	public Collection<UnlinkedUser> get() {
		return unlinkedUserService.getAll();
	}
}
