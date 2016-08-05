import java.lang.reflect.Proxy;


public class Run {

	/**
	 * @Title  :一个测试JDK动态代理的DEMO
	 * @Auther :刘双源
	 * @Date   :2016年8月5日
	 * @Return :void
	 */
	public static void main(String[] args) {
		
		//创建一个实例继承一个接口
		MyInterfaces myinterfaces = new Entity();
		//创建一个切面
		AOPHanlder handler = new AOPHanlder(myinterfaces);
		//动态生成代理实例
		MyInterfaces proxyEntity = (MyInterfaces) Proxy.newProxyInstance(myinterfaces.getClass().getClassLoader(),new Class[]{MyInterfaces.class}, handler);
		//运行方法
		String result = proxyEntity.doingSomething();
		
		System.out.println(result);

	}

}
