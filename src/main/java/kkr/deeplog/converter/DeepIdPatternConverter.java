package kkr.deeplog.converter;

import org.apache.log4j.spi.LoggingEvent;

public class DeepIdPatternConverter extends DeepPatternConverter {
	private static final DeepIdPatternConverter INSTANCE = new DeepIdPatternConverter();

	public static DeepIdPatternConverter newInstance(
			final String[] options) {
		return INSTANCE;
	}

	private DeepIdPatternConverter() {
		super("deepId", "deepId");
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		toAppendTo.append(getLayoutId());
	}
}
