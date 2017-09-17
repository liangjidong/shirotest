package shiro;

import org.apache.shiro.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by author on 2017/8/30.
 */
public class PermissionTest {
    @Test
    public void testIsPermitted() {
        Util.login("classpath:shiro.ini", "zhang", "123");
        Assert.assertTrue(SecurityUtils.getSubject().isPermitted("user:create"));
        Assert.assertTrue(SecurityUtils.getSubject().isPermitted("user:view"));
    }
}
