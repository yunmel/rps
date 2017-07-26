package com.yunmel.rps.service;

import java.util.ArrayList;
import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.yunmel.db.common.IActiveRecord;
import com.yunmel.rps.model.Task;

@Bean
public class TaskService {

  @Inject
  private IActiveRecord dao;

  public Integer saveTask(Task task) {
    try {
      task.setFettle(0);
      return dao.save(task);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public List<Task> findTaskByProject(Long project, Integer fettle) {
    String sql = "select * from t_core_task t where t.project= ? and t.fettle= ?";
    return new ArrayList<>();
  }
}
