package shiro;

import org.apache.shiro.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by author on 2017/8/30.
 */
public class RoleTest {
    @Test
    public void testHasRole() {
        Util.login("classpath:shiro.ini", "zhang", "123");
        Assert.assertTrue(SecurityUtils.getSubject().hasRole("role1"));
    }
}
