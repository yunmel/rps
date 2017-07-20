package com.yunmel.rps.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.Path;
import com.yunmel.rps.service.ProjectService;

@Path("project")
public class ProjectController {
	@Inject
	private ProjectService projectService;
	
}
