/**
 * @Author: patrick-mac
 * @Date: 2018/10/8 16:48
 * @Description:
 */
public class BusinessLogicProxy implements BusinessLogic {

    // 代理类
    private BusinessLogicImpl businessLogic;

    public BusinessLogicProxy(BusinessLogicImpl businessLogic) {
        this.businessLogic = businessLogic;
    }

    @Override
    public void businessLogic1() {
        System.out.println("方法执行之前");

        businessLogic.businessLogic1();

        System.out.println("方法执行之后");
    }

    @Override
    public void businessLogic2() {
        System.out.println("方法执行之前");

        businessLogic.businessLogic2();

        System.out.println("方法执行之后");
    }
}
