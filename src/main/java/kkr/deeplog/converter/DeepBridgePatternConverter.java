package kkr.deeplog.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.pattern.FormattingInfo;
import org.apache.log4j.pattern.LiteralPatternConverter;
import org.apache.log4j.pattern.LoggingEventPatternConverter;
import org.apache.log4j.pattern.PatternConverter;
import org.apache.log4j.pattern.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

public class DeepBridgePatternConverter extends
		org.apache.log4j.helpers.PatternConverter {

	private static final Map<String, Class<? extends PatternConverter>> PATTERN_LAYOUT_RULES;

	static {
		PATTERN_LAYOUT_RULES = new HashMap<String, Class<? extends PatternConverter>>();
		PATTERN_LAYOUT_RULES.put("deepTab", DeepTabPatternConverter.class);
		PATTERN_LAYOUT_RULES
				.put("deepSymbol", DeepSymbolPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("deepTimeDelta",
				DeepTimeDeltaPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("deepMemoryDelta",
				DeepMemoryDeltaPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("deepMemory",
				DeepMemoryPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("deepLevel", DeepLevelPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("deepMark", DeepMarkPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("deepId", DeepIdPatternConverter.class);
		PATTERN_LAYOUT_RULES.put("m", DeepMessagePatternConverter.class);
		PATTERN_LAYOUT_RULES.put("message", DeepMessagePatternConverter.class);
	}

	/**
	 * Pattern converters.
	 */
	private LoggingEventPatternConverter[] patternConverters;

	/**
	 * Field widths and alignment corresponding to pattern converters.
	 */
	private FormattingInfo[] patternFields;

	/**
	 * Does pattern process exceptions.
	 */
	private boolean handlesExceptions;

	private long layoutId;

	/**
	 * Create a new instance.
	 * 
	 * @param pattern
	 *            pattern, may not be null.
	 */
	public DeepBridgePatternConverter(long layoutId, final String pattern) {
		this.layoutId = layoutId;
		next = null;
		handlesExceptions = false;

		List<PatternConverter> converters = new ArrayList<PatternConverter>();
		List<FormattingInfo> fields = new ArrayList<FormattingInfo>();

		PatternParser.parse(pattern, converters, fields, PATTERN_LAYOUT_RULES,
				PatternParser.getPatternLayoutRules());

		patternConverters = new LoggingEventPatternConverter[converters.size()];
		patternFields = new FormattingInfo[converters.size()];

		int i = 0;
		Iterator<PatternConverter> converterIter = converters.iterator();
		Iterator<FormattingInfo> fieldIter = fields.iterator();

		while (converterIter.hasNext()) {
			PatternConverter converter = converterIter.next();

			if (converter instanceof DeepPatternConverter) {
				DeepPatternConverter deepPatternConverter = (DeepPatternConverter) converter;
				deepPatternConverter.setLayoutId(layoutId);
			}

			if (converter instanceof LoggingEventPatternConverter) {
				patternConverters[i] = (LoggingEventPatternConverter) converter;
				handlesExceptions |= patternConverters[i].handlesThrowable();
			} else {
				patternConverters[i] = new LiteralPatternConverter("");
			}

			if (fieldIter.hasNext()) {
				patternFields[i] = fieldIter.next();
			} else {
				patternFields[i] = FormattingInfo.getDefault();
			}

			i++;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected String convert(final LoggingEvent event) {
		//
		// code should be unreachable.
		//
		StringBuffer sbuf = new StringBuffer();
		format(sbuf, event);

		return sbuf.toString();
	}

	/**
	 * Format event to string buffer.
	 * 
	 * @param sbuf
	 *            string buffer to receive formatted event, may not be null.
	 * @param e
	 *            event to format, may not be null.
	 */
	public void format(final StringBuffer sbuf, final LoggingEvent e) {
		for (int i = 0; i < patternConverters.length; i++) {
			int startField = sbuf.length();
			patternConverters[i].format(e, sbuf);
			patternFields[i].format(startField, sbuf);
		}
	}

	/**
	 * Will return false if any of the conversion specifiers in the pattern
	 * handles {@link Exception Exceptions}.
	 * 
	 * @return true if the pattern formats any information from exceptions.
	 */
	public boolean ignoresThrowable() {
		return !handlesExceptions;
	}
}
