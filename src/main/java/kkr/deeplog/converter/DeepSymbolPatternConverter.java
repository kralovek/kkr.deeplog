package kkr.deeplog.converter;

import kkr.deeplog.Loggable;
import kkr.deeplog.data.DeepId;
import kkr.deeplog.data.DeepLevel;

import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class DeepSymbolPatternConverter extends DeepPatternConverter implements
		DeepParameters {

	private static String SYM_BEGIN = "-->";
	private static String SYM_ENDOK = "<--";
	private static String SYM_ENDERR = "<#-";
	private static String SYM_OK = " o ";
	private static String SYM_ERROR = " # ";
	private static String SYM_FATAL = "###";
	private static String SYM_WARNING = " W ";
	private static String SYM_MESSAGE = " - ";

	private String symBegin = SYM_BEGIN;
	private String symEndok = SYM_ENDOK;
	private String symEnderr = SYM_ENDERR;
	private String symOk = SYM_OK;
	private String symError = SYM_ERROR;
	private String symFatal = SYM_FATAL;
	private String symWarning = SYM_WARNING;
	private String symMessage = SYM_MESSAGE;

	public static DeepSymbolPatternConverter newInstance(final String[] options) {
		return new DeepSymbolPatternConverter(options);
	}

	private DeepSymbolPatternConverter(final String[] options) {
		super("deepSymbol", "deepSymbol");
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

				if (NAME_BEGIN.equals(name)) {
					symBegin = value;
				} else if (NAME_ENDOK.equals(name)) {
					symEndok = value;
				} else if (NAME_ENDERR.equals(name)) {
					symEnderr = value;
				} else if (NAME_OK.equals(name)) {
					symOk = value;
				} else if (NAME_ERROR.equals(name)) {
					symError = value;
				} else if (NAME_FATAL.equals(name)) {
					symFatal = value;
				} else if (NAME_WARNING.equals(name)) {
					symWarning = value;
				} else if (NAME_MESSAGE.equals(name)) {
					symMessage = value;
				} else {
					LogLog.warn("PatternConverter " + getName()
							+ ": ignored parameter {" + name + "}");
				}
			}
		}
	}

	public void format(LoggingEvent event, StringBuffer toAppendTo) {
		if (event.getMessage() != null) {
			Object message = event.getMessage();
			if (message instanceof Loggable.BEGIN) {
				toAppendTo.append(symBegin);
				return;
			} else if (message instanceof Loggable.END) {
				DeepId deepId = new DeepId(event.getThreadName(), getLayoutId());
				DeepLevel deepLevel = DeepLevel.getLastLevel(deepId);
				toAppendTo.append(deepLevel.isOk() ? symEndok : symEnderr);
				return;
			} else if (message instanceof Loggable.OK) {
				toAppendTo.append(symOk);
				return;
			} else if (message instanceof Loggable.KO) {
				toAppendTo.append(symError);
				return;
			}
		} 
		if (Level.WARN.equals(event.getLevel())) {
			toAppendTo.append(symWarning);
		} else if (Level.ERROR.equals(event.getLevel())) {
			toAppendTo.append(symError);
		} else if (Level.FATAL.equals(event.getLevel())) {
			toAppendTo.append(symFatal);
		} else {
			toAppendTo.append(symMessage);
		}
	}
}
