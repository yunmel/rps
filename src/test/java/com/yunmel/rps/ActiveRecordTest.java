/*
 * Copyright ©2016 四川云麦尔科技 Inc. All rights reserved.
 */
package com.yunmel.rps;

import com.yunmel.rps.config.IActiveRecord;
import com.yunmel.rps.config.impl.ActiveRecord;
import com.yunmel.rps.model.User;

/**
 * TODO : 该类的描述信息
 * @author xuyq(pazsolr@gmail.com)
 * @time 2017年7月21日 - 上午12:06:37
 */
public class ActiveRecordTest {
  
  public static void main(String[] args) {
    IActiveRecord ar = new ActiveRecord();
    try {
      User user = ar.findById(User.class, 2L);
      System.out.println(user.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
