package kkr.deeplog.parser;

import kkr.deeplog.converter.DeepBridgePatternConverter;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;

public class DeepLoggingPatternParser extends PatternParser {
	private long layoutId;
	
	public DeepLoggingPatternParser(long layoutId, String conversionPattern) {
		super(conversionPattern);
		this.layoutId = layoutId;
	}

	public PatternConverter parse() {
		return new DeepBridgePatternConverter(layoutId, pattern);
	}
}
