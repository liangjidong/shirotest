package shiro.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiro.entity.User;

/**
 * Created by author on 2017/9/16.
 */
public class GenerateLinkUtils {

    private static final Logger logger = LoggerFactory.getLogger(GenerateLinkUtils.class);


    private static final String CHECK_CODE = "checkCode";

    /**
     * 生成激活链接
     *
     * @param user
     * @param randomCode
     * @return
     * @throws Exception
     */
    public static String generateActiveLink(User user, String randomCode) throws Exception {
        return "http://localhost:8080/shirotest/user/active.do?username=" + user.getUsername() + "&" + CHECK_CODE + "=" + generateCheckCode(user, randomCode);
    }

    /**
     * 生成重置密码链接
     * @param user
     * @param randomCode
     * @return
     * @throws Exception
     */
    public static String generateResetPwdLink(User user,String randomCode) throws Exception{
        return "http://localhost:8080/shirotest/user/resetPwd.do?username=" + user.getUsername() + "&" + CHECK_CODE + "=" + generateCheckCode(user, randomCode);
    }

    /**
     * 生成校验码
     *
     * @param user
     * @param randomCode
     * @return
     * @throws Exception
     */
    private static String generateCheckCode(User user, String randomCode) throws Exception {
        return Coder.encryptBASE64UrlSafe(Coder.encryptMD5((user.getUsername() + ":" + randomCode).getBytes()));
    }



    /**
     * 验证给定的校验码是否正确
     *
     * @param user
     * @param randomCode
     * @param checkCode
     * @return
     * @throws Exception
     */
    public static boolean verifyCheckCode(User user, String randomCode, String checkCode) throws Exception {
        String s = Coder.encryptBASE64UrlSafe(Coder.encryptMD5((user.getUsername() + ":" + randomCode).getBytes()));
        logger.info("checkCode:{},s:{}", checkCode, s);
        return s.equals(checkCode);
    }

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setUsername("test");
        String randomCode = "c728e995-e5ec-48a9-b416-5d8e39a7b1b5";
        String checkCode = generateCheckCode(user, randomCode);
        System.out.println(checkCode);
        System.out.println(verifyCheckCode(user, randomCode, checkCode));
    }
}
