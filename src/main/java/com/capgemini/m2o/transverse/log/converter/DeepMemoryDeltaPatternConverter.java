package com.capgemini.m2o.transverse.log.converter;

import org.apache.log4j.spi.LoggingEvent;

import com.capgemini.log.DeepId;
import com.capgemini.log.DeepLevel;
import com.capgemini.log.Loggable;
import com.capgemini.m2o.transverse.log.utils.LoggableUtils;
import com.capgemini.m2o.transverse.log.utils.MemoryUtils;

public class DeepMemoryDeltaPatternConverter extends DeepPatternConverter {
	private static final DeepMemoryDeltaPatternConverter INSTANCE = new DeepMemoryDeltaPatternConverter();

	public static DeepMemoryDeltaPatternConverter newInstance(
			final String[] options) {
		return INSTANCE;
	}

	private DeepMemoryDeltaPatternConverter() {
		super("deepMemoryDelta", "deepMemoryDelta");
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		if (event.getMessage() != null) {
			Object message = LoggableUtils.adaptLoggable(event.getMessage());
			if (message instanceof Loggable.END) {
				DeepId deepId = new DeepId(event.getThreadName(), getLayoutId());
				DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
				long delta = MemoryUtils.memory() - deepLevel.getMemory();
				toAppendTo.append(delta);
			}
		}
	}
}
