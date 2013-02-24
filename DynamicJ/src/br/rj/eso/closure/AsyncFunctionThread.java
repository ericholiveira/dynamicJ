package br.rj.eso.closure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AsyncFunctionThread extends Thread {

	Method method;
	Object context;
	Object[] args;
	
	public AsyncFunctionThread(Method method, Object context, Object[] args) {
		super();
		this.method = method;
		this.context = context;
		this.args = args;
	}

	@Override
	public void run() {
		try {
			method.invoke(context, args);
		} catch (IllegalAccessException e){
		}catch(IllegalArgumentException e){
		}catch(InvocationTargetException e){
		}
	}

}
