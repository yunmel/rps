package com.yunmel.rps.service;

import java.util.List;

import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.jdbc.ActiveRecord;
import com.yunmel.rps.model.User;

@Bean
public class UserService {
	@Inject
	private ActiveRecord activeRecord;
	
	public Long saveUser(User user) {
		Long id = activeRecord.saveOrUpdate(user);
		return id;
	}

	public List<User> findAllUser() {
		String sql = "select * from t_base_user";
		return activeRecord.list(User.class, sql);
	}
}
