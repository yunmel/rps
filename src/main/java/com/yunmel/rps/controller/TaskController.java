package com.yunmel.rps.controller;

import java.util.List;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.http.Request;
import com.yunmel.rps.model.Task;
import com.yunmel.rps.service.TaskService;

@Path("task")
public class TaskController {
  @Inject
  private TaskService taskService;

  @GetRoute("content")
  public String toTask() {
    return "task/task-index.html";
  }

  @GetRoute("content/:project")
  public String toTaskWithProject(Request request) {
    Long project = request.pathLong("project");
    request.attribute("project", project);
    List<Task> tasks = taskService.findTaskByProject(project, 0);
    request.attribute("nomalTasks", tasks);
    return "task/task-index.html";
  }

  @GetRoute("form/:mode/:project/:classify")
  public String toForm(Request request) {
    String mode = request.pathString("mode");
    Long project = request.pathLong("project");
    Integer classify = request.pathInt("classify");
    request.attribute("classify", classify);
    request.attribute("project", project);
    return "task/task-form.html";
  }


  @PostRoute("save")
  @JSON
  public Integer saveUser(Task task) {
    task.init();
    return taskService.saveTask(task);
  }

}
