package com.capgemini.m2o.transverse.log.converter;

import org.apache.log4j.spi.LoggingEvent;

import com.capgemini.log.DeepId;
import com.capgemini.log.DeepLevel;

public class DeepLevelPatternConverter extends DeepPatternConverter {
	private static final DeepLevelPatternConverter INSTANCE = new DeepLevelPatternConverter();

	public static DeepLevelPatternConverter newInstance(
			final String[] options) {
		return INSTANCE;
	}

	private DeepLevelPatternConverter() {
		super("deepLevel", "deepLevel");
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		DeepId deepId = new DeepId(event.getThreadName(), getLayoutId());
		DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
		toAppendTo.append(deepLevel.getLevel());
	}
}
