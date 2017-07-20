/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.rps.config;

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
}
