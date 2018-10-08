/**
 * @Author: patrick-mac
 * @Date: 2018/10/8 16:37
 * @Description:
 */
public class Client {

    public static void main(String[] args) {
        BusinessLogicImpl blimpl = new BusinessLogicImpl();
        BusinessLogicProxy businessLogicProxy = new BusinessLogicProxy();

        BusinessLogicImpl blproxy = (BusinessLogicImpl) businessLogicProxy.getInstance(blimpl);

        blproxy.businessLogic1();
        blproxy.businessLogic2();
    }

}
