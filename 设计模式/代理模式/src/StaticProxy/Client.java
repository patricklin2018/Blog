/**
 * @Author: patrick-mac
 * @Date: 2018/10/8 16:50
 * @Description:
 */
public class Client {

    public static void main(String[] args) {
        BusinessLogicImpl bl = new BusinessLogicImpl();
        BusinessLogicProxy businessLogicProxy = new BusinessLogicProxy(bl);
        businessLogicProxy.businessLogic1();
        businessLogicProxy.businessLogic2();
    }

}
