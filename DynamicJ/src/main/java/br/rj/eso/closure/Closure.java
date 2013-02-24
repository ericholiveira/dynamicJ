package br.rj.eso.closure;

import java.lang.reflect.InvocationTargetException;

import br.rj.eso.closure.exception.ClosureException;
import br.rj.eso.closure.exception.ClosureIllegalAccessException;
import br.rj.eso.closure.exception.ClosureIllegalArgumentException;
import br.rj.eso.closure.exception.ClosureInstantiationException;
import br.rj.eso.closure.exception.ClosureInvocationTargetException;

public class Closure<K> {
	protected static ThreadLocal<Function> lastFunction = new ThreadLocal<Function>();
	private Function targetFunction;

	private Closure(Function targetFunction) {
		this.targetFunction = targetFunction;
	}

	protected static void setLastFunction(Function function) {
		lastFunction.set(function);
	}

	public static <T> T convert(T object)   throws ClosureIllegalAccessException , ClosureInstantiationException {
		try {
			return ProxyFactory.create(object);
			} catch (InstantiationException e) {
				throw new ClosureInstantiationException(e);
			} catch (IllegalAccessException e) {
				throw new ClosureIllegalAccessException(e);
			}
	}

	public static <T> T convert(Class<T> clazz)  throws ClosureIllegalAccessException , ClosureInstantiationException{
		try {
			return ProxyFactory.create(clazz.newInstance());
		} catch (InstantiationException e) {
			throw new ClosureInstantiationException(e);
		} catch (IllegalAccessException e) {
			throw new ClosureIllegalAccessException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Closure closure(FunctionTypeFactory.FunctionType type, Object method) {
		return closure(type,method,null,null);
	}
	@SuppressWarnings("rawtypes")
	public static Closure closureLast() {
		return closure(null,null,null,null);
	}
	@SuppressWarnings("rawtypes")
	public static Closure closure(FunctionTypeFactory.FunctionType type, Object method, Closure onSuccess , Closure onError) {
		Function function = lastFunction.get();
		function.setOnSuccess(onSuccess);
		function.setOnError(onError);
		return new Closure(function);
	}
	@SuppressWarnings("rawtypes")
	public static Closure closureLast(Closure onSuccess , Closure onError) {
		return closure(null,null,onSuccess,onError);
	}
	
	
	@SuppressWarnings("unchecked")
	public K call(Object... args) throws ClosureIllegalAccessException,
			ClosureIllegalArgumentException, ClosureInvocationTargetException, ClosureException {
		try {
			return (K) targetFunction.call(targetFunction.getMethod()
					.getReturnType(), args);
		} catch (IllegalAccessException e) {
			throw new ClosureIllegalAccessException(e);
		} catch (IllegalArgumentException e) {
			throw new ClosureIllegalArgumentException(e);
		} catch (InvocationTargetException e) {
			throw new ClosureInvocationTargetException(e);
		}
	}

	public Closure<K> setContext(Object context)
			throws ClosureIllegalAccessException,
			ClosureIllegalArgumentException, ClosureInvocationTargetException {
		try {
			targetFunction.setContext(context);
			return this;
		} catch (IllegalArgumentException e) {
			throw new ClosureIllegalArgumentException(e);
		}
	}

}
