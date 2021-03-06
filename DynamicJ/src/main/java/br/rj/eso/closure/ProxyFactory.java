package br.rj.eso.closure;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class ProxyFactory<T> implements MethodInterceptor {
	
	T originalObject;
	private ProxyFactory(T originalObject){
		this.originalObject = originalObject;
	}
	@SuppressWarnings("unchecked")
	protected static <T> T create(T object) throws InstantiationException, IllegalAccessException{
		Enhancer enhancer = new Enhancer();
		if(object.getClass().isAnonymousClass()){
			enhancer.setSuperclass(object.getClass().getSuperclass());
		}else{
			enhancer.setSuperclass(object.getClass());
		}
		enhancer.setInterfaces(object.getClass().getInterfaces());
		enhancer.setCallback(new ProxyFactory<T>(object));
		return (T)enhancer.create();
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args,
			MethodProxy arg3) throws Throwable {
		FunctionTypeFactory.FunctionType lastFunctionType = FunctionTypeFactory.getLastFunctionType();
		if(lastFunctionType!=null){
			Function function = new Function(originalObject,method,lastFunctionType,args);
			Closure.setLastFunction(function);
			return null;
		}else{
			return method.invoke(originalObject, args);
		}
		
	}

}
