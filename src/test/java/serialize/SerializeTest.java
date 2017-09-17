package serialize;

import org.apache.shiro.session.mgt.SimpleSession;
import shiro.util.SerializeUtils;

import java.util.Arrays;

/**
 * Created by author on 2017/9/4.
 */
public class SerializeTest {
    public static void main(String[] args) {
//        SimpleSession session = new SimpleSession();
//        session.setId("shiro-session6a9cbd2f-0997-4dd5-81b1-cc6d11a22cfe");
//        byte[] serialize = SerializeUtils.serialize(session);
//        System.out.println(Arrays.toString(serialize));
//        System.out.println(SerializeUtils.deserialize(serialize, session.getClass()));
        MyBean bean = new MyBean();
        byte[] s = SerializeUtils.serialize(bean);
        System.out.println(Arrays.toString(s));
        System.out.println(SerializeUtils.deserialize(s, MyBean.class));
    }
}
