package br.rj.eso.closure;

public class FunctionTypeFactory {
	private static ThreadLocal<FunctionType> lastFunctionType = new ThreadLocal<FunctionType>();
	public static FunctionType empty(){
		lastFunctionType.set(FunctionType.EMPTY);
		return lastFunctionType.get();
	}
	public static FunctionType partial(){
		lastFunctionType.set(FunctionType.PARTIAL);
		return lastFunctionType.get();
	}
	protected static FunctionType getLastFunctionType(){
		FunctionType last = lastFunctionType.get();
		lastFunctionType.set(null);
		return last;
	}
}
