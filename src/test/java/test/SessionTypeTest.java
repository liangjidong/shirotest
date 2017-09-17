package test;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 * Created by author on 2017/8/30.
 */
public class SessionTypeTest {
    public static void main(String[] args) {
        Session session = new SimpleSession();
        System.out.println(session.getClass());
        System.out.println(Session.class);
    }
}
