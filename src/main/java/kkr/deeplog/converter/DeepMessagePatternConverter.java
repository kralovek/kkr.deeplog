package kkr.deeplog.converter;

import kkr.deeplog.utils.LoggableUtils;

import org.apache.log4j.spi.LoggingEvent;

import com.capgemini.log.Loggable;

public class DeepMessagePatternConverter extends DeepPatternConverter {
	private static final DeepMessagePatternConverter INSTANCE = new DeepMessagePatternConverter();

	public static DeepMessagePatternConverter newInstance(
			final String[] options) {
		return INSTANCE;
	}

	private DeepMessagePatternConverter() {
		super("deepMessage", "deepMessage");
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		if (event.getMessage() != null) {
			Object message = LoggableUtils.adaptLoggable(event.getMessage());
			if (message instanceof Loggable.BASE) {
				message = ((Loggable.BASE)message).getMessage();
			}
			if (message != null) {
				toAppendTo.append(event.getMessage().toString());
			} else {
				// nothing
			}
		} else if (event.getRenderedMessage() != null) {
			toAppendTo.append(event.getRenderedMessage());
		}
	}
}