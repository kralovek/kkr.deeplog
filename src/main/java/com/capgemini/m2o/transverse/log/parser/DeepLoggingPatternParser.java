package com.capgemini.m2o.transverse.log.parser;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;

import com.capgemini.m2o.transverse.log.converter.DeepBridgePatternConverter;

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
