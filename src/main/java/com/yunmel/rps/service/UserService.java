package com.yunmel.rps.service;

import java.util.ArrayList;
import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.yunmel.rps.db.IActiveRecord;
import com.yunmel.rps.model.User;

@Bean
public class UserService {
	@Inject
	private IActiveRecord activeRecord;
	
	public Long saveUser(User user) {
//		Long id = activeRecord.saveOrUpdate(user);
		return 1L;
	}
//
	public List<User> findAllUser() {
	  return new ArrayList<>();
	}
}
