package com.yunmel.rps.service;

import java.util.ArrayList;
import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.yunmel.rps.config.IActiveRecord;
import com.yunmel.rps.model.Task;

@Bean
public class TaskService {

  @Inject
  private IActiveRecord activeRecord;

  public Long saveTask(Task task) {
    task.setFettle(0);
    return 1L;
  }

  public List<Task> findTaskByProject(Long project, Integer fettle) {
    String sql = "select * from t_core_task t where t.project= ? and t.fettle= ?";
    return new ArrayList<>();
  }
}
