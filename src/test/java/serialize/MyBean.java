package serialize;

/**
 * Created by author on 2017/9/4.
 */
public class MyBean {
    private int a = 0;
    private String b = "test";

    @Override
    public String toString() {
        return "MyBean{" +
                "a=" + a +
                ", b='" + b + '\'' +
                '}';
    }
}
