package com.yunmel.rps.service;

import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.jdbc.ActiveRecord;
import com.yunmel.rps.model.Task;

@Bean
public class TaskService {

	@Inject
	private ActiveRecord activeRecord;
	
	public Long saveTask(Task task) {
		Long id = activeRecord.saveOrUpdate(task);
		return id;
	}

	public List<Task> findTaskByProject(Long project,Integer fettle) {
		String sql = "select * from t_core_task t where t.project= ? and t.fettle= ?"; 
		return activeRecord.list(Task.class, sql,project,fettle);
	}
}
