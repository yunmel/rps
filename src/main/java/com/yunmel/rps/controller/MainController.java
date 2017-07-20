package com.yunmel.rps.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Request;
import com.yunmel.rps.service.ProjectService;

@Path("main")
public class MainController {
	@Inject
	private ProjectService projectService;
	
	@GetRoute("content")
	public String getContent(Request request){
		request.attribute("projects", projectService.getProjects());
		return "main/main-content.html";
	}
}
