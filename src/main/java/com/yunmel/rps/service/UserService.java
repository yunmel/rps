package com.yunmel.rps.service;

import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.yunmel.db.common.IActiveRecord;
import com.yunmel.rps.model.User;

@Bean
public class UserService {
  @Inject
  private IActiveRecord dao;

  public Integer saveUser(User user) {
    try {
      return dao.save(user);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  //
  public List<User> findAllUser() {
    try {
      return dao.findAll(User.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
