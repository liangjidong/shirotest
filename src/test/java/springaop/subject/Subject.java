package springaop.subject;

import org.springframework.stereotype.Component;

/**
 * Created by author on 2017/9/1.
 */
@Component
public class Subject {
    public void test() throws Exception {
        System.out.println("-----test-----");
        throw new Exception("");
    }

}
