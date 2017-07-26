/**
 * Copyright 2010-2016 the original author or authors.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.yunmel.db.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.yunmel.db.common.DatasourceConfig;

public class PropertyHolderUtil {
  private final static String CONFIG_FILE_PATH = "app.properties";
  private static Properties props = null;

  static {
    props = new Properties();
    try {
      InputStream input = PropertyHolderUtil.class.getResourceAsStream("/" + CONFIG_FILE_PATH);
      if (input == null) {
        ClassLoader classLoader = PropertyHolderUtil.class.getClassLoader();
        input = PropertyHolderUtil.class.getResourceAsStream("" + classLoader.getResource(CONFIG_FILE_PATH));
      }
      if (input == null) {
        String path = System.getProperty("user.dir") + "/" + CONFIG_FILE_PATH;
        input = new FileInputStream(path);
      }
      props.load(input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private PropertyHolderUtil() {
    super();
  }

  public static String getProperty(String propName) {
    return props.getProperty(propName);
  }


  public static DatasourceConfig getDsConfig() {
    DatasourceConfig config = new DatasourceConfig();
    String driverClass = props.getProperty("jdbc.driverClass");
    String jdbcUrl = props.getProperty("jdbc.url");
    String username = props.getProperty("jdbc.user");
    String password = props.getProperty("jdbc.password");
    config.setDriverClass(driverClass);
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);
    
    String poolType = props.getProperty("jdbc.pool.type");
    if (StringUtils.isNotBlank(poolType)) {
      Map<String, String> poolPerperties = new HashMap<>();
      poolPerperties.put(DatasourceConfig._POOL_TYPE, poolType);
      config.setPoolPerperties(poolPerperties);
    }
    
    return config;
  }
}
