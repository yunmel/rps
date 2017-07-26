package com.yunmel.rps.service;

import java.util.ArrayList;
import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.yunmel.db.common.IActiveRecord;
import com.yunmel.rps.model.Project;

@Bean
public class ProjectService {

  @Inject
  private IActiveRecord dao;

  // public Paginator<Project> getProjects(int page, int limit){
  // String countSql = "select count(0) from t_core_project";
  // int total = activeRecord.one(Integer.class, countSql);
  // PageRow pageRow = new PageRow(page, limit);
  // Paginator<Project> paginator = new Paginator<>(total, pageRow.getPage(), pageRow.getLimit());
  // String sql = "select * from t_core_project limit " + pageRow.getOffSet() + "," + limit;
  // List<Project> list = activeRecord.list(Project.class, sql);
  // if (null != list) {
  // paginator.setList(list);
  // }
  // return paginator;
  // }
  //
  public List<Project> getProjects() {
    try {
      return dao.findAll(Project.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
