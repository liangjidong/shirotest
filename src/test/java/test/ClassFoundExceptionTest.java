package test;

import java.util.List;

/**
 * Created by author on 2017/9/1.
 */
public class ClassFoundExceptionTest {
    public static void main(String[] args) throws ClassNotFoundException {
//        Test1 t = new Test1();
        Thread.currentThread().getContextClassLoader().loadClass("test.Test1");

    }

    void printList(List<?> list){
        for (Object o : list) {

        }
    }
}

class Test1 {

}
