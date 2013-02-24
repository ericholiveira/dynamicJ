package br.rj.eso.closure;

import java.lang.reflect.Method;

import br.rj.eso.closure.exception.ClosureException;

public class AsyncFunctionThread extends Thread {

	Method method;
	Object context;
	Object[] args;
	Closure<?> onSuccess;
	Closure<?> onError;

	public AsyncFunctionThread(Method method, Object context, Object[] args,
			Closure<?> onSuccess, Closure<?> onError) {
		super();
		this.method = method;
		this.context = context;
		this.args = args;
		this.onSuccess = onSuccess;
		this.onError = onError;
	}

	@Override
	public void run() {
		Object returnObject = null;
		boolean success = false;
		try {
			returnObject = method.invoke(context, args);
			success = true;
		} catch (Throwable t) {
			handleException(t);
		}
		if (success && this.onSuccess != null) {
			try {
				this.onSuccess.call(returnObject);
			} catch (ClosureException e) {
			}
		}
	}

	private void handleException(Throwable t) {
		if (this.onError != null) {
			try {
				this.onError.call(t);
			} catch (ClosureException e) {
			}
		}
	}

}
