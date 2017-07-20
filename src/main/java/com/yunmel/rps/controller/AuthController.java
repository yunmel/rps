package com.yunmel.rps.controller;

import com.blade.kit.StringKit;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.http.Session;
import com.yunmel.rps.Const;
import com.yunmel.rps.model.User;

/**
 * 认证控制器
 *
 * @author biezhi
 *         26/06/2017
 */
@Path
public class AuthController {
	
    @GetRoute({"index","/"})
    public String index(Request request) {
        return "index.html";
    }

    @GetRoute("login")
    public String login() {
        return "login.html";
    }

    @PostRoute("login")
    public String doLogin(User user, Request request, Response response) {

//        if (StringKit.isBlank(user.getUsername())) {
//            request.attribute("error", "用户名不能为空");
//            return "index.html";
//        }
//
//        if (StringKit.isBlank(user.getPassword())) {
//            request.attribute("error", "用户名不能为空");
//            return "login.html";
//        }
//
//        if (!Const.USERNAME.equalsIgnoreCase(user.getUsername()) ||
//                !Const.PASSWORD.equalsIgnoreCase(user.getPassword())) {
//
//            request.attribute("error", "用户名或密码错误");
//            return "login.html";
//        }
//
//        request.session().attribute(Const.LOGIN_SESSION_KEY, user.getUsername());
//        System.out.println("login:" + request);
        response.redirect("/index");

        return null;
    }

    @GetRoute("logout")
    public void logout(Session session, Response response) {
        session.removeAttribute(Const.LOGIN_SESSION_KEY);
        response.redirect("/login");
    }

}
