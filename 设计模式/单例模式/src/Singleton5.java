/**
 * @Author: patrick-mac
 * @Date: 2018/10/6 12:43
 * @Description:
 *
 * 内部枚举
 */
public class Singleton5 {

    private enum EnumSingleton {
        INSTANCE;

        private Singleton5 singleton;

        private EnumSingleton() {
            singleton = new Singleton5();
        }

        public Singleton5 getSingleton() {
            return singleton;
        }
    }

    public static Singleton5 getInstance() {
        return EnumSingleton.INSTANCE.getSingleton();
    }
}
