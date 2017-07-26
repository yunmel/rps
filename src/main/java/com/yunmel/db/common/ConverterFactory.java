/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.db.common;

/**
 * TODO : 该类的描述信息
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月25日 - 下午10:44:07
 */
public class ConverterFactory {
  private static final ConverterFactory _INSTANCE = new ConverterFactory();

  public static ConverterFactory getInstance() {
    return _INSTANCE;
  }

  public boolean needConvert(Class<?> sourceType, Class<?> targetType) {
    return !(targetType.isAssignableFrom(sourceType));
  }
}
