package br.rj.eso.closure;

public class FunctionTypeFactory {
	private static ThreadLocal<FunctionType> lastFunctionType = new ThreadLocal<FunctionType>();
	public static FunctionType sync(){
		lastFunctionType.set(FunctionType.SYNC);
		return lastFunctionType.get();
	}
	public static FunctionType async(){
		lastFunctionType.set(FunctionType.ASYNC);
		return lastFunctionType.get();
	}
	protected static FunctionType getLastFunctionType(){
		FunctionType last = lastFunctionType.get();
		lastFunctionType.set(null);
		return last;
	}
}
