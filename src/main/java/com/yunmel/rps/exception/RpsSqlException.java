/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.rps.exception;

/**
 * TODO : 该类的描述信息
 * 
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月20日 - 下午11:48:32
 */
public class RpsSqlException extends RuntimeException {
  private static final long serialVersionUID = -2573335158994638840L;

  public RpsSqlException() {}

  public RpsSqlException(String message) {
    super(message);
  }

  public RpsSqlException(String message, Throwable cause) {
    super(message, cause);
  }

  public RpsSqlException(Throwable cause) {
    super(cause);
  }
}
