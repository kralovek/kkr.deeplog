package kkr.deeplog.layout;

import kkr.deeplog.Loggable;
import kkr.deeplog.data.DeepId;
import kkr.deeplog.data.DeepLevel;
import kkr.deeplog.parser.DeepLoggingPatternParser;
import kkr.deeplog.utils.LoggableUtils;
import kkr.deeplog.utils.MemoryUtils;

import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;

public class DeepPatternLayout extends EnhancedPatternLayout implements
		OptionHandler {
	private final long layoutId = System.nanoTime();

	private boolean acceptBegin = true;
	private boolean acceptEnd = true;
	private boolean acceptOk = true;

	public DeepPatternLayout() {
		super();
	}

	public DeepPatternLayout(String pattern) {
		super(pattern);
	}

	public String format(LoggingEvent event) {
		if (event == null) {
			return "null";
		}
		if (event.getMessage() != null) {
			Object message = LoggableUtils.adaptLoggable(event.getMessage());

			if (message instanceof Loggable.BASE) {
				event = LoggableUtils.modifyLoggingEvent(
						(Loggable.BASE) message, event);
			}

			if (message instanceof Loggable.INIT_ALL) {
				DeepLevel.initAll();
				return super.format(event);
			} else if (message instanceof Loggable.INIT) {
				DeepId deepId = new DeepId(event.getThreadName(), layoutId);
				DeepLevel.init(deepId);
				return super.format(event);
			} else if (message instanceof Loggable.BEGIN) {
				DeepId deepId = new DeepId(event.getThreadName(), layoutId);
				DeepLevel.addNewLevel(deepId, event.getTimeStamp(),
						MemoryUtils.memory());
				return acceptBegin ? super.format(event) : "";
			} else if (message instanceof Loggable.END) {
				String text = acceptEnd ? super.format(event) : "";
				DeepId deepId = new DeepId(event.getThreadName(), layoutId);
				DeepLevel.removeLastLevel(deepId);
				return text;
			} else if (message instanceof Loggable.OK) {
				DeepId deepId = new DeepId(event.getThreadName(), layoutId);
				DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
				deepLevel.setOk(true);
				return acceptOk ? super.format(event) : "";
			} else if (message instanceof Loggable.KO) {
				DeepId deepId = new DeepId(event.getThreadName(), layoutId);
				DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
				deepLevel.setOk(false);
				return acceptOk ? super.format(event) : "";
			}
			/*
			 * if (LoggableUtils.instanceOfLoggable(event.getMessage(),
			 * Loggable.BEGIN.class)) { DeepId deepId = new
			 * DeepId(event.getThreadName(), layoutId);
			 * DeepLevel.addNewLevel(deepId, event.getTimeStamp()); return
			 * acceptBegin ? super.format(event) : ""; } else if
			 * (LoggableUtils.instanceOfLoggable(event.getMessage(),
			 * Loggable.END.class)) { String text = acceptEnd ?
			 * super.format(event) : ""; DeepId deepId = new
			 * DeepId(event.getThreadName(), layoutId);
			 * DeepLevel.removeLastLevel(deepId); return text; } else if
			 * (LoggableUtils.instanceOfLoggable(event.getMessage(),
			 * Loggable.OK.class)) { DeepId deepId = new
			 * DeepId(event.getThreadName(), layoutId); DeepLevel deepLevel =
			 * DeepLevel.getLastLevel(deepId); deepLevel.setOk(true); return
			 * acceptOk ? super.format(event) : ""; } else if
			 * (LoggableUtils.instanceOfLoggable(event.getMessage(),
			 * Loggable.KO.class)) { DeepId deepId = new
			 * DeepId(event.getThreadName(), layoutId); DeepLevel deepLevel =
			 * DeepLevel.getLastLevel(deepId); deepLevel.setOk(false); return
			 * acceptOk ? super.format(event) : ""; }
			 */
		}
		return super.format(event);
	}

	protected PatternParser createPatternParser(String pattern) {
		return new DeepLoggingPatternParser(layoutId, pattern);
	}

	public boolean isAcceptBegin() {
		return acceptBegin;
	}

	public void setAcceptBegin(boolean acceptBegin) {
		this.acceptBegin = acceptBegin;
	}

	public boolean isAcceptEnd() {
		return acceptEnd;
	}

	public void setAcceptEnd(boolean acceptEnd) {
		this.acceptEnd = acceptEnd;
	}

	public boolean isAcceptOk() {
		return acceptOk;
	}

	public void setAcceptOk(boolean acceptOk) {
		this.acceptOk = acceptOk;
	}
}
