package com.yunmel.rps.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.jdbc.model.Paginator;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Path;
import com.yunmel.rps.model.Project;
import com.yunmel.rps.service.ProjectService;

@Path("project")
public class ProjectController {
	
	@Inject
	private ProjectService projectService;
	
	@GetRoute("list")
	@JSON
	public Paginator<Project> listProject(){
		return projectService.getProjects(1, 10);
	}
}
