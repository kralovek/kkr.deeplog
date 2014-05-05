package kkr.deeplog.converter;

import kkr.deeplog.Loggable;
import kkr.deeplog.data.DeepId;
import kkr.deeplog.data.DeepLevel;
import kkr.deeplog.utils.LoggableUtils;
import kkr.deeplog.utils.MemoryUtils;

import org.apache.log4j.spi.LoggingEvent;

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
