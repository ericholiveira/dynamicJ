package br.rj.eso.closure;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

class Function {
	Object context;
	Method method;
	Object[] args;
	FunctionType type;

	protected Function(Object context, Method method, FunctionType type,
			Object... args) {
		super();
		this.context = context;
		this.method = method;
		this.args = args;
		this.type = type;
	}

	protected Object getContext() {
		return context;
	}

	protected void setContext(Object context) {
		this.context = context;
	}

	protected Method getMethod() {
		return method;
	}

	protected void setMethod(Method method) {
		this.method = method;
	}

	protected Object[] getArgs() {
		return args;
	}

	protected void setArgs(Object[] args) {
		this.args = args;
	}

	@SuppressWarnings("unchecked")
	protected <K> K call(Class<K> returnType, Object...args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		switch(type){
			case SYNC:
				return (K)method.invoke(context, applyParameters(method.getParameterTypes(),method.isVarArgs(),args));
			default :
				AsyncFunctionThread asyncFunction = new AsyncFunctionThread(method, context, applyParameters(method.getParameterTypes(),method.isVarArgs(),args));
				asyncFunction.start();
				return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object[] applyParameters(Class<?>[] parameterTypes,
			boolean isVarArgs, Object... newArgs) {
		Class<?> varArgsType = isVarArgs ? parameterTypes[parameterTypes.length - 1]
				: null;
		int paramLengthWithoutVarargs = isVarArgs ? args.length - 1
				: args.length;
		Object[] methodArgs = new Object[args.length];
		int j = 0;
		for (int i = 0; i < paramLengthWithoutVarargs; i++) {
			if (args[i] == null) {
				if (newArgs != null
						&& j < newArgs.length
						&& parameterTypes[i].isAssignableFrom(newArgs[j]
								.getClass())) {
					methodArgs[i] = newArgs[j];
				} else {
					methodArgs[i] = null;
				}
				j++;
			} else {
				methodArgs[i] = args[i];
			}
		}

		if (varArgsType != null) {
			// If is varargs the last parameter is an Object array
			Object[] defaultVarArgs = (Object[]) args[args.length - 1];
			try {
				Vector v = new Vector();

				int i = 0;
				for (; i < defaultVarArgs.length; i++) {
					v.add(defaultVarArgs[i]);
				}
				for (; j < newArgs.length; i++, j++) {
					if (varArgsType.getComponentType().isAssignableFrom(
							newArgs[j].getClass())) {
						v.add(newArgs[j]);
					}

				}
				methodArgs[paramLengthWithoutVarargs] = v
						.toArray((Object[]) Array.newInstance(Class
								.forName(varArgsType.getCanonicalName()
										.substring(
												0,
												varArgsType.getCanonicalName()
														.length() - 2)), 0));
			} catch (ClassNotFoundException e) {
				// Suppress
			}

		}
		return methodArgs;
	}
}
