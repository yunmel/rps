package com.yunmel.rps;

import com.blade.Blade;
import com.blade.mvc.WebContext;
import com.blade.mvc.view.template.JetbrickTemplateEngine;
import com.yunmel.rps.db.impl.ActiveRecord;

/**
 * Hello world!
 */
public class Application {

    public static void main(String[] args) {
    	Blade blade = Blade.me();
    	blade.register(new ActiveRecord());
    	WebContext.init(blade, "",false);
//        blade.before("/*", ((request, response) -> {
//            String uri = request.uri();
//            if("/index".equals(uri)){
//                String username = request.session().attribute(Const.LOGIN_SESSION_KEY);
//                if (StringKit.isBlank(username)) {
//                    response.redirect("/login");
//                    return;
//                }
//            }
//        })).event(EventType.SERVER_STARTED, (e) -> {
//            Environment environment = e.blade.environment();
//            Const.USERNAME = environment.get("app.username").get();
//            Const.PASSWORD = environment.get("app.password").get();
//        })
    	  blade.templateEngine(new JetbrickTemplateEngine())
          .start(Application.class);

    }
}
