/**
 * @Author: patrick-mac
 * @Date: 2018/10/6 12:40
 * @Description:
 *
 * 静态内部类
 *
 */
public class Singleton4 {
    private Singleton4() {}

    private static class SingletonInstance {
        private static final Singleton4 instance = new Singleton4();
    }

    public static Singleton4 getInstance() {
        return SingletonInstance.instance;
    }
}
