package kkr.deeplog.converter;

import kkr.deeplog.data.DeepId;
import kkr.deeplog.data.DeepLevel;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class DeepTabPatternConverter extends DeepPatternConverter {

	private static final String DEFAULT_LEVEL = "    ";

	private String tabLevel;

	public static DeepTabPatternConverter newInstance(final String[] options) {
		return new DeepTabPatternConverter(options);
	}

	private DeepTabPatternConverter(final String[] options) {
		super("deepTab", "deepTab");
		if (options != null && options.length > 0) {
			if (options.length > 1) {
				LogLog.warn("PatternConverter " + getName()
						+ ": only first parameter is used");
			}
			this.tabLevel = options[0];
		} else {
			this.tabLevel = DEFAULT_LEVEL;
		}
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		DeepId deepId = new DeepId(event.getThreadName(), getLayoutId());
		DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
		String value = toStringLevel(deepLevel.getLevel());
		toAppendTo.append(value);
	}

	private String toStringLevel(int level) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < level; i++) {
			buffer.append(tabLevel);
		}
		return buffer.toString();
	}
}
