package com.yunmel.rps.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.http.Request;
import com.yunmel.rps.model.User;
import com.yunmel.rps.service.UserService;

@Path("sys")
public class SysController {
  @Inject
  private UserService userService;

  @GetRoute("user")
  public String toUser(Request request) {
    request.attribute("users", userService.findAllUser());
    return "sys/user/user-index.html";
  }

  @GetRoute("user/form/:mode")
  public String toForm(Request request) {
    String mode = request.query("mode", "add");
    if ("edit".equals(mode)) {
      // TODO query user by id
    }
    return "sys/user/user-form.html";
  }

  @PostRoute("user/save")
  @JSON
  public Integer saveUser(User user) {
    user.init();
    return userService.saveUser(user);
  }
}
