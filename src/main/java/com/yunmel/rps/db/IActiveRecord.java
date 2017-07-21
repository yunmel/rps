/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.rps.db;

import java.util.List;
import java.util.Map;

/**
 * TODO : 该类的描述信息
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月20日 - 下午11:08:51
 */
public interface IActiveRecord {
  public <T> void save(T entity) throws Exception;
  public <T> void update(T entity) throws Exception;
  public <T> void delete(T entity) throws Exception;
  public <T> T findById(Class<T> clazz, Object object) throws Exception;
  public <T> List<T> findAll(Class<T> clazz) throws Exception;
  public <T> List<T> findByParams(Class<T> clazz,Map<String, Object> params)throws Exception;
}
