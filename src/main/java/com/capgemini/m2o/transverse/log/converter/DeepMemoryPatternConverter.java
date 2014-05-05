package com.capgemini.m2o.transverse.log.converter;

import org.apache.log4j.spi.LoggingEvent;

import com.capgemini.m2o.transverse.log.utils.MemoryUtils;

public class DeepMemoryPatternConverter extends DeepPatternConverter {
	private static final DeepMemoryPatternConverter INSTANCE = new DeepMemoryPatternConverter();

	public static DeepMemoryPatternConverter newInstance(
			final String[] options) {
		return INSTANCE;
	}

	private DeepMemoryPatternConverter() {
		super("deepMemory", "deepMemory");
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		toAppendTo.append(MemoryUtils.memory());
	}
}
