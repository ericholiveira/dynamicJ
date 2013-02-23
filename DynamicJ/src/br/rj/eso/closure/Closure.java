package br.rj.eso.closure;

import java.lang.reflect.InvocationTargetException;


public class Closure<K> {
	protected static ThreadLocal<Function> lastFunction = new ThreadLocal<Function>();
	private Function targetFunction;
	private Closure(Function targetFunction){
		this.targetFunction = targetFunction;
	}
	
	
	protected static void setLastFunction(Function function){
		lastFunction.set(function);
	}
	
	
	public static <T> T convert(T object) throws InstantiationException, IllegalAccessException{
		return ProxyFactory.create(object);
	}
	public static <T> T convert(Class<T> clazz) throws InstantiationException, IllegalAccessException{
		return ProxyFactory.create(clazz.newInstance());
	}
	
	
	
	
	public static Closure closure(FunctionType type,Object method){
		return new Closure(lastFunction.get());
	}
	public static Closure closureLast(){
		return new Closure(lastFunction.get());
	}
	
	
	
	
	public K call() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (K)targetFunction.call(targetFunction.getMethod().getReturnType());
	}
	public K call(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (K)targetFunction.call(targetFunction.getMethod().getReturnType(),args);
	}
	public Closure<K> setContext(Object context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		targetFunction.setContext(context);
		return this;
	}
	
}

