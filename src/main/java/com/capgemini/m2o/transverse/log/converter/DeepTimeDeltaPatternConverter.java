package com.capgemini.m2o.transverse.log.converter;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.capgemini.log.DeepId;
import com.capgemini.log.DeepLevel;
import com.capgemini.log.Loggable;
import com.capgemini.m2o.transverse.log.utils.LoggableUtils;

public class DeepTimeDeltaPatternConverter extends DeepPatternConverter {

	private int decimalLevel = 0;

	public static DeepTimeDeltaPatternConverter newInstance(
			final String[] options) {
		return new DeepTimeDeltaPatternConverter(options);
	}

	private DeepTimeDeltaPatternConverter(final String[] options) {
		super("deepTimeDelta", "deepTimeDelta");
		if (options != null && options.length > 0) {
			if (options.length > 1) {
				LogLog.warn("PatternConverter " + getName()
						+ ": only first parameter is used");
			}
			try {
				decimalLevel = Integer.parseInt(options[0]);
			} catch (NumberFormatException ex) {
				LogLog.warn("PatternConverter " + getName()
						+ ": the parameter must be a positiv integer");
			}
			if (decimalLevel < 0) {
				LogLog.warn("PatternConverter " + getName()
						+ ": the parameter must be a positiv integer");
				decimalLevel = 0;
			}
		}
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		if (event.getMessage() != null) {
			Object message = LoggableUtils.adaptLoggable(event.getMessage());
			if (message instanceof Loggable.END) {
				DeepId deepId = new DeepId(event.getThreadName(), getLayoutId());
				DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
				long delta = event.getTimeStamp() - deepLevel.getTime();
				if (decimalLevel == 0) {
					toAppendTo.append(delta);
				} else {
					String strDelta = String.valueOf(delta);
					if (strDelta.length() <= decimalLevel) {
						toAppendTo.append(String.format(".%0" + decimalLevel
								+ "d", delta));
					} else {
						toAppendTo
								.append(strDelta.substring(0, strDelta.length()
										- decimalLevel))
								.append('.')
								.append(strDelta.substring(strDelta.length()
										- decimalLevel));
					}
				}
			}
		}
	}
}
