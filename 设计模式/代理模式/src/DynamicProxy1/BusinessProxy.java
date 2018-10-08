import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: patrick-mac
 * @Date: 2018/10/8 14:55
 * @Description:
 */
public class BusinessProxy implements InvocationHandler {

    // 代理对象
    Object bl;

    // 通过构造方法传递进代理对象
    public BusinessProxy(Object bl) {
        this.bl = bl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("我是代理 [proxy]" + proxy.getClass().getName());
        System.out.println("执行 [method]" + method.getName() + "之前，进行数据预处理...\n");

        System.out.println("代理方法开始执行");
        method.invoke(bl, args);
        System.out.println("代理方法执行完毕\n");

        System.out.println("执行 [method]" + method.getName() + "之后，进行数据备份...\n");

        return null;
    }
}
