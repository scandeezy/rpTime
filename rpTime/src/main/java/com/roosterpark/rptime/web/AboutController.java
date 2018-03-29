package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.model.GitRepositoryState;

@Controller
@RequestMapping(value = "/about")
public class AboutController {

	@Inject
	GitRepositoryState state;

	@RequestMapping(method = { GET })
	@ResponseBody
	public GitRepositoryState get() {
		return state;
	}

}
