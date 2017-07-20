package com.yunmel.rps.controller;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Request;

@Path("task")
public class TaskController {
	
	@GetRoute("content")
	public String toTask(){
		return "task/task-index.html";
	}
	
	@GetRoute("content/:id")
	public String toTaskWithProject(Request request){
		Long id = request.pathLong("id");
		request.attribute("project", id);
		return "task/task-index.html";
	}
	
	@GetRoute("form/:mode/:project")
	public String toForm(Request request) {
		String mode = request.pathString("mode");
		Long project = request.pathLong("project");
		request.attribute("project", project);
		return "task/task-form.html";
	}
	
}
