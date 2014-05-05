package com.capgemini.m2o.transverse.log.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class DeepMarkPatternConverter extends DeepPatternConverter
		implements DeepParameters {

	private List<Object[]> data = new ArrayList<Object[]>();

	public static DeepMarkPatternConverter newInstance(
			final String[] options) {
		return new DeepMarkPatternConverter(options);
	}

	private DeepMarkPatternConverter(final String[] options) {
		super("deepMark", "deepMark");

		if (options != null) {
			for (String option : options) {
				String[] parts = option.split("=");
				if (parts.length != 2) {
					LogLog.warn("PatternConverter "
							+ getName()
							+ ": the parameter definition is not in the format {parameter=value}");
				}
				String name = parts[0];
				String value = parts[1]; // no trim !!!
				Pattern pattern = createPattern(value);
				data.add(new Object[] { name, pattern });
			}
		}
	}
	
	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		event.getLogger().getName();
		for (Object[] item : data) {
			String name = (String) item[0];
			Pattern patern = (Pattern) item[1];
			if (patern.matcher(event.getLogger().getName()).matches()) {
				toAppendTo.append(name);
				break;
			}
		}
	}
	
	private Pattern createPattern(String value) {
		return Pattern.compile(value.replace(".", "\\.").replace("*", ".*"));
	}
}
