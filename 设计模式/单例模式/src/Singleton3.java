/**
 * @Author: patrick-mac
 * @Date: 2018/10/6 12:39
 * @Description:
 *
 * 饿汉式 2 - 静态代码块
 */
public class Singleton3 {

    private static Singleton3 singleton;

    static {
        singleton = new Singleton3();
    }

    private Singleton3() {}

    public static Singleton3 getInstance() {
        return singleton;
    }

}
