/**
 * @Author: patrick-mac
 * @Date: 2018/10/6 12:21
 * @Description:
 *
 * 懒汉式：
 *
 * 懒汉式 1 - 锁粒度过大
 * 懒汉式 2 - 与 懒汉式1 本质相同，锁粒度过大
 * 懒汉式 3 - double check
 *
 */
public class Singleton1 {

    private static Singleton1 singleton = null;

    private Singleton1() {}

    // 懒汉式 1 - 锁粒度过大，效率低
    public static synchronized Singleton1 getInstance1() {
        if (singleton == null) {
            singleton = new Singleton1();
        }
        return singleton;
    }

    // 懒汉式 2 - 本质与 懒汉式1 相同，锁粒度大，效率低
    public static Singleton1 getInstance2() {
        synchronized (Singleton1.class) {
            if (singleton == null) {
                singleton = new Singleton1();
            }
        }
        return singleton;
    }

    // 懒汉式 3 - double check，减小了锁粒度
    public static Singleton1 getInstance3() {
        if (singleton == null) {
            synchronized (Singleton1.class) {
                if (singleton == null) {
                    singleton = new Singleton1();
                }
            }
        }
        return singleton;
    }
}
