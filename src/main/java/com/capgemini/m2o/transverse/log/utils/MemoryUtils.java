package com.capgemini.m2o.transverse.log.utils;

public class MemoryUtils {

	public static long memory() {
		return Runtime.getRuntime().freeMemory();
	}
}
