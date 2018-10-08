import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Author: patrick-mac
 * @Date: 2018/10/8 15:01
 * @Description:
 */
public class Client {

    public static void main(String[] args) {
        BusinessLogicInter bl = new BusinessImpl();

        InvocationHandler handler = new BusinessProxy(bl);

        // 寻找代理
        BusinessLogicInter proxySeller = (BusinessLogicInter) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                bl.getClass().getInterfaces(),
                handler);

        System.out.println(proxySeller.getClass().getName());

        proxySeller.businessLogic1();
        proxySeller.businessLogic2();
    }

}
