package kkr.deeplog.utils;

public class MemoryUtils {

	public static long memory() {
		return Runtime.getRuntime().freeMemory();
	}
}
