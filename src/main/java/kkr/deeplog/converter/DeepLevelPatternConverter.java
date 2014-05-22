package kkr.deeplog.converter;

import kkr.deeplog.data.DeepId;
import kkr.deeplog.data.DeepLevel;

import org.apache.log4j.spi.LoggingEvent;

public class DeepLevelPatternConverter extends DeepPatternConverter {
	// private static final DeepLevelPatternConverter INSTANCE = new DeepLevelPatternConverter();

	public static DeepLevelPatternConverter newInstance(
			final String[] options) {
		DeepLevelPatternConverter newInstance = new DeepLevelPatternConverter();
		return newInstance;
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
