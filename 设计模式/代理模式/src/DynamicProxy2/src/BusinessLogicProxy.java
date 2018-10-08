import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: patrick-mac
 * @Date: 2018/10/8 16:26
 * @Description:
 */
public class BusinessLogicProxy implements MethodInterceptor {

    // 代理对象
    Object bl;

    // 类似 JDK 动态代理中的绑定
    public Object getInstance(Object target) {
        this.bl  = target;

        // 创建加强器，用来创建代理类
        Enhancer enhancer = new Enhancer();
        // 为加强器指定要代理的类（即，为动态生成的代理类指定父类）
        enhancer.setSuperclass(this.bl.getClass());
        // 设置回调：对于动态代理类上所有方法的调用，都会回调 CallBack，而 CallBack 则需要实现 intercept() 方法
        enhancer.setCallback(this);
        // 创建动态代理类并返回
        return enhancer.create();
    }

    // 回调方法
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行业务逻辑之前");
        // 调用父类方法
        methodProxy.invokeSuper(o, objects);
        System.out.println("执行业务逻辑之后");
        return null;
    }
}
