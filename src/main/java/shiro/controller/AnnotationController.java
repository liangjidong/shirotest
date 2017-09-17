package shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by author on 2017/8/30.
 */
@Controller
public class AnnotationController {
    @RequestMapping("/login")
    public String login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (UnknownAccountException e) {
            request.setAttribute("message", "账号不存在");
            return "login";
        } catch (LockedAccountException e) {
            request.setAttribute("message", "账号锁定");
            return "login";
        } catch (AuthenticationException e) {
            request.setAttribute("message", "用户名或密码错误");
            return "login";
        }
        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "login";
    }

    @RequestMapping("/hello1")
    public String hello1() {
        SecurityUtils.getSubject().checkRole("admin");
        return "success";
    }

    @RequiresRoles("admin")
    @RequestMapping("/hello2")
    public String hello2() {
        return "success";
    }
}
