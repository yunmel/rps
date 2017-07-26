/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.db.orm;

import java.util.HashMap;
import java.util.Map;

import com.yunmel.db.anno.Table;
import com.yunmel.db.utils.StringUtils;

/**
 * TODO : 该类的描述信息
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月26日 - 下午8:34:32
 */
public class MappingCache {

  private static final MappingCache _INSTANCE = new MappingCache();
  
  private Map<Class<?>, String> tableMaps = null;
  
  public static final MappingCache getIt() {
    return _INSTANCE;
  }
  
  private MappingCache() {
    this.tableMaps = new HashMap<>();
  }
  
  public String getTableName(Class<?> clazz) {
    String tableName = tableMaps.get(clazz);
    if(StringUtils.isBlank(tableName)) {
      Table table = clazz.getAnnotation(Table.class);
      if (null != table) {
        tableName = table.name();
      }
      if (tableName == null) {
        tableName = clazz.getSimpleName().toUpperCase();
      }
      tableMaps.put(clazz, tableName);
    }
    return tableName;
  }
  
}
