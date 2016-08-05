import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class AOPHanlder implements InvocationHandler {
	
	Object obj = null;

	public AOPHanlder(Object obj){
		this.obj = obj;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("---Before Doingsomething");
		Object result = method.invoke(obj, args);
		System.out.println("---After Doingsomething");
		return result;
	}

}
