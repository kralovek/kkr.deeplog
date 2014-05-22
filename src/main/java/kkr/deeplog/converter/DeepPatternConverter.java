package kkr.deeplog.converter;

import org.apache.log4j.pattern.LoggingEventPatternConverter;

public abstract class DeepPatternConverter extends LoggingEventPatternConverter {
	private Long layoutId;

	protected DeepPatternConverter(final String name, final String style) {
		super(name, style);
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
}
