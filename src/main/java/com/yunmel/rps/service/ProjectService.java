package com.yunmel.rps.service;

import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.jdbc.ActiveRecord;
import com.blade.jdbc.model.PageRow;
import com.blade.jdbc.model.Paginator;
import com.yunmel.rps.model.Project;

@Bean
public class ProjectService {
	
	@Inject
	private ActiveRecord activeRecord;
	
	public Paginator<Project> getProjects(int page, int limit){
		String countSql = "select count(0) from t_core_project";
		int total = activeRecord.one(Integer.class, countSql);
		PageRow pageRow = new PageRow(page, limit);
        Paginator<Project> paginator = new Paginator<>(total, pageRow.getPage(), pageRow.getLimit());
        String sql = "select * from t_core_project limit " + pageRow.getOffSet() + "," + limit;
        List<Project> list = activeRecord.list(Project.class, sql);
        if (null != list) {
            paginator.setList(list);
        }
        return paginator;
	}
	
	
	public List<Project> getProjects(){
		String sql = "select * from t_core_project";
		return activeRecord.list(Project.class, sql);
	}
	
}
