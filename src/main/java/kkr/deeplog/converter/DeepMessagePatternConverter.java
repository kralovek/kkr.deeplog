package kkr.deeplog.converter;

import kkr.deeplog.Loggable;
import kkr.deeplog.utils.LoggableUtils;

import org.apache.log4j.spi.LoggingEvent;

public class DeepMessagePatternConverter extends DeepPatternConverter {
	// private static final DeepMessagePatternConverter INSTANCE = new DeepMessagePatternConverter();

	public static DeepMessagePatternConverter newInstance(
			final String[] options) {
		return new DeepMessagePatternConverter();
	}

	private DeepMessagePatternConverter() {
		super("deepMessage", "deepMessage");
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		if (event.getMessage() != null) {
			Object message = event.getMessage();
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
