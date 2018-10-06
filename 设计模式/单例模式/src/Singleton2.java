/**
 * @Author: patrick-mac
 * @Date: 2018/10/6 12:31
 * @Description:
 *
 * 饿汉式 1 - 静态常量
 */
public class Singleton2 {

    private static Singleton2 singleton = new Singleton2();

    private Singleton2() {}

    public static Singleton2 getInstance() {
        return singleton;
    }

}
