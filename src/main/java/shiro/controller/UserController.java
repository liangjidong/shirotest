package shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shiro.common.Constants;
import shiro.common.Response;
import shiro.common.StatusType;
import shiro.entity.User;
import shiro.service.PasswordHelper;
import shiro.service.UserService;
import shiro.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * Created by author on 2017/9/15.
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private CacheManager shiroEhcacheManager;

    @RequestMapping("login")
    @ResponseBody
    public Object login(String username, String password) {
        try {
            //获取session
            Session session = SecurityUtils.getSubject().getSession();
            Map<String, Object> keys = (Map<String, Object>) session.getAttribute(Constants.RSA_KEYS);
            if (keys == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            String privateKey = CipherUtils.getPrivateKey(keys);
            if (privateKey == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //解密数据
            //首先将password使用base64解码
            byte[] base64 = Coder.decryptBASE64(password);
            byte[] bytes = CipherUtils.decryptByPrivateKey(base64, privateKey);
            String pwd = new String(bytes);
            logger.info("password:{}", pwd);
            UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
            //登陆
            StatusType type = null;
            try {
                SecurityUtils.getSubject().login(token);
            } catch (ExcessiveAttemptsException e) {
                type = StatusType.OVER_TIMES;
            } catch (UnknownAccountException e) {
                type = StatusType.LOGIN_FAILD;
            } catch (LockedAccountException e) {
                type = StatusType.USER_LOCK;
            } catch (AuthenticationException e) {
                type = StatusType.LOGIN_FAILD;
            }
            if (type == null) {
                session.setAttribute("username", username);
                //判断用户是否被激活
                User user = userService.findByUsername(username);
                if (user.getActived() == 0) {
                    type = StatusType.NOT_ACTIVE;
                } else
                    type = StatusType.SUCCESS;
            }
            return new Response(type.getVal(), type.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.login出错,username={}", username, e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    @RequestMapping("register")
    @ResponseBody
    public Object register(String username, String password, String email) {
        try {
            //获取session
            Session session = SecurityUtils.getSubject().getSession();
            Map<String, Object> keys = (Map<String, Object>) session.getAttribute(Constants.RSA_KEYS);
            if (keys == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            String privateKey = CipherUtils.getPrivateKey(keys);
            if (privateKey == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //解密数据
            //首先将password使用base64解码
            byte[] base64 = Coder.decryptBASE64(password);
            byte[] bytes = CipherUtils.decryptByPrivateKey(base64, privateKey);
            String pwd = new String(bytes);
            logger.info("password:{}", pwd);
            //注册
            User user = new User(username, pwd);
            user.setEmail(email);
            User regUser = userService.createUser(user);
            if (regUser == null) {
                return new Response(StatusType.REGISTER_FAILD.getVal(), StatusType.REGISTER_FAILD.getMessage());
            }
            //注册成功，发送邮件激活
            sendActivateEmail(regUser);
            //然后返回
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.register出错,user={}", new Object[]{username, password}, e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    @RequestMapping("changePwd")
    @ResponseBody
    public Object changePwd(String username, String originalPwd, String newPwd) {
        try {
            //获取session
            Session session = SecurityUtils.getSubject().getSession();
            Map<String, Object> keys = (Map<String, Object>) session.getAttribute(Constants.RSA_KEYS);
            if (keys == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            String privateKey = CipherUtils.getPrivateKey(keys);
            if (privateKey == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //解密数据
            //首先将password使用base64解码
            byte[] base64 = Coder.decryptBASE64(originalPwd);
            byte[] bytes = CipherUtils.decryptByPrivateKey(base64, privateKey);
            String pwd = new String(bytes);
            base64 = Coder.decryptBASE64(newPwd);
            bytes = CipherUtils.decryptByPrivateKey(base64, privateKey);
            String pwd1 = new String(bytes);
            User user = userService.findByUsername(username);
            if (user == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //判断原密码是否正确
            if (passwordHelper.getHashedPassword(user.getSalt(), pwd) != user.getPassword()) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //进行密码的修改
            userService.changePassword(user.getId(), pwd1);
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.changePwd出错,user={}", username, e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    @RequestMapping("logout")
    @ResponseBody
    public Object logout() {
        try {
            SecurityUtils.getSubject().logout();
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.logout出错", e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    @RequestMapping("getPubKey")
    @ResponseBody
    public Object getPubKey() {
        try {
            //首先获取session
            Session session = SecurityUtils.getSubject().getSession();
            Map<String, Object> initKey = CipherUtils.initKey();
            session.setAttribute(Constants.RSA_KEYS, initKey);
            RSAPublicKey key = (RSAPublicKey) initKey.get(CipherUtils.PUBLIC_KEY);
            String publicKey = CipherUtils.getPublicKey(initKey);
            BigInteger exponent = key.getPublicExponent();
            BigInteger modulus = key.getModulus();
            Map<String, Object> resultMap = new HashMap<>(4);
            resultMap.put("exponent", exponent.toString(16));
            resultMap.put("modulus", modulus.toString(16));
            resultMap.put("publicKey", publicKey);
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage(), resultMap);
        } catch (Exception e) {
            logger.error("调用UserController.getPubKey出错", e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    @RequestMapping("active")
    @ResponseBody
    public Object active(String username, String checkCode) {
        try {
            logger.info("username:{},checkCode:{}", username, checkCode);
            Cache<Object, Object> cache = shiroEhcacheManager.getCache("user_activeCache");
            String randomCode = (String) cache.get(username);
            logger.info("当前激活时使用的randomCode为:{}", randomCode);
            if (randomCode == null) {
                return new Response(StatusType.TIME_INVALID.getVal(), StatusType.TIME_INVALID.getMessage());
            }
            User user = userService.findByUsername(username);
            if (user == null) {
                logger.info("用户{}不存在", username);
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            boolean verifyCheckCode = GenerateLinkUtils.verifyCheckCode(user, randomCode, checkCode);
            logger.info("激活结果:{}", verifyCheckCode);
            if (verifyCheckCode) {
                userService.active(user.getId());
                //激活成功后，将该randomCode移除，下次调用同一个激活链接就失效
                cache.remove(user.getUsername());
                return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
            } else {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
        } catch (Exception e) {
            logger.error("调用UserController.active出错,username={}", username, e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    /**
     * 用户重新申请发送激活链接
     *
     * @return
     */
    @RequestMapping("request_active")
    @ResponseBody
    public Object requestActive() {
        try {
            Session session = SecurityUtils.getSubject().getSession();
            String username = (String) session.getAttribute("username");
            if (username == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            User user = userService.findByUsername(username);
            if (user == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            if (user.getActived() == 1) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //发送邮件
            sendActivateEmail(user);
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.requestActive出错", e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    /**
     * 更新emial
     *
     * @param email
     * @return
     */
    @RequestMapping("emailUpdate")
    @ResponseBody
    public Object emailUpdate(String email) {
        try {
            Session session = SecurityUtils.getSubject().getSession();
            String username = (String) session.getAttribute("username");
            if (username == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            User user = userService.findByUsername(username);
            if (user == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            if (user.getEmail().equals(email)) {
                return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
            }
            user.setEmail(email);
            //邮箱修改后，激活状态为未激活，且发送一个邮件给新邮箱进行激活
            user.setActived((byte) 0);
            userService.changeEmail(user);
            sendActivateEmail(user);
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.emailUpdate,email={}", email, e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    /**
     * 生成验证码
     *
     * @param response
     */
    @RequestMapping("verfiyCode")
    public void verifyCode(HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");

            //生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(6);
            //存入会话session
            Session session = SecurityUtils.getSubject().getSession();
            //删除以前的
            session.removeAttribute("verCode");
            session.setAttribute("verCode", verifyCode.toLowerCase());
            logger.info("当前session:{}对应的验证码为：{}",session.getId(),verifyCode);
            //生成图片
            int w = 100, h = 30;
            out = response.getOutputStream();
            VerifyCodeUtils.outputImage(w, h, out, verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    @RequestMapping("forgotPwd")
    @ResponseBody
    public Object forgotPwd(String email, String code) {
        logger.info("email:{},code:{}",email,code);
        try {
            Session session = SecurityUtils.getSubject().getSession();
            String verCode = (String) session.getAttribute("verCode");
            //验证码
            if (verCode == null || !verCode.equals(code.toLowerCase())) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            if (email == null)
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            User user = userService.findByEmail(email);
            if (user == null) {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            //发送邮件
            sendForgotPwdEmail(user);
            return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("调用UserController.forgotPwd出错", e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }

    /**
     * 邮件中点击链接地址，则使用本接口实现跳转到重设密码页面
     *
     * @param username
     * @param checkCode
     * @return
     */
    @RequestMapping("resetPwd")
    public String toResetPage(HttpServletRequest request, String username, String checkCode) {
        try {
            logger.info("username:{},checkCode:{}", username, checkCode);
            Cache<Object, Object> cache = shiroEhcacheManager.getCache("password_findCache");
            String randomCode = (String) cache.get(username);
            logger.info("当前激活时使用的randomCode为:{}", randomCode);
            if (randomCode == null) {
                return "unauthorized";
            }
            User user = userService.findByUsername(username);
            if (user == null) {
                logger.info("用户{}不存在", username);
                return "unauthorized";
            }
            boolean verifyCheckCode = GenerateLinkUtils.verifyCheckCode(user, randomCode, checkCode);
            logger.info("匹配结果:{}", verifyCheckCode);
            if (verifyCheckCode) {
                request.setAttribute("username", username);
                request.setAttribute("checkCode", checkCode);
                return "changePwdByForgot";
            } else {
                return "unauthorized";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unauthorized";
    }

    @RequestMapping("changePwdByEmial")
    @ResponseBody
    public Object changePwdByEmial(String username, String password,String checkCode) {
        try {
            logger.info("username:{},checkCode:{}", username, checkCode);
            Cache<Object, Object> cache = shiroEhcacheManager.getCache("password_findCache");
            String randomCode = (String) cache.get(username);
            logger.info("当前使用的randomCode为:{}", randomCode);
            if (randomCode == null) {
                return new Response(StatusType.TIME_INVALID.getVal(), StatusType.TIME_INVALID.getMessage());
            }
            User user = userService.findByUsername(username);
            if (user == null) {
                logger.info("用户{}不存在", username);
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
            boolean verifyCheckCode = GenerateLinkUtils.verifyCheckCode(user, randomCode, checkCode);
            if (verifyCheckCode) {
                userService.changePassword(user.getId(), password);
                cache.remove(user.getUsername());//移除这次修改密码的随机码
                return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage());
            } else {
                return new Response(StatusType.OPT_INVALID.getVal(), StatusType.OPT_INVALID.getMessage());
            }
        } catch (Exception e) {
            logger.error("调用UserController.changePwdByEmial出错,username={}", username, e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }


    private void sendActivateEmail(User user) {
        //生成一个随机码，并放入ecache中缓存，用于后续验证
        Cache<Object, Object> cache = shiroEhcacheManager.getCache("user_activeCache");
        String randomCode = UUID.randomUUID().toString();
        cache.put(user.getUsername(), randomCode);
        logger.info("用户:{}的最新randomCode是:{}", user.getUsername(), randomCode);
        //发送邮件激活
        EmailUtils.sendAccountActivateEmail(user, randomCode);
    }

    private void sendForgotPwdEmail(User user) {
        //生成一个随机码，并放入ecache中缓存，用于后续验证
        Cache<Object, Object> cache = shiroEhcacheManager.getCache("password_findCache");
        String randomCode = UUID.randomUUID().toString();
        cache.put(user.getUsername(), randomCode);
        logger.info("用户:{}的最新忘记密码randomCode是:{}", user.getUsername(), randomCode);
        //发送邮件
        EmailUtils.sendForgotPwdEmail(user, randomCode);
    }
}
