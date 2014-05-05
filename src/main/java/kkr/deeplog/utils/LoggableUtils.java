package kkr.deeplog.utils;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import com.capgemini.log.Loggable;

public class LoggableUtils {

	public static Object adaptLoggable(Object object) {
		if (object == null || object instanceof Loggable.BASE) {
			return object;
		}
		if (object instanceof String) {
			String string = (String) object;
			String name;
			if (string.startsWith(name = "BEGIN")) {
				return string.length() > name.length() + 2 ? new Loggable.BEGIN(string.substring(name.length() + 1)) : Loggable.BEGIN;
			} else if (string.startsWith(name = "END")) {
				return string.length() > name.length() + 2 ? new Loggable.END(string.substring(name.length() + 1)) : Loggable.END;
			} else if (string.startsWith(name = "OK")) {
				return string.length() > name.length() + 2 ? new Loggable.OK(string.substring(name.length() + 1)) : Loggable.OK;
			} else if (string.startsWith(name = "KO")) {
				return string.length() > name.length() + 2 ? new Loggable.KO(string.substring(name.length() + 1)) : Loggable.KO;
			} else if (string.startsWith(name = "INIT_ALL")) {
				return string.length() > name.length() + 2 ? new Loggable.INIT_ALL(string.substring(name.length() + 1)) : Loggable.INIT_ALL;
			} else if (string.startsWith(name = "INIT")) {
				return string.length() > name.length() + 2 ? new Loggable.INIT(string.substring(name.length() + 1)) : Loggable.INIT;
			}
		} else {
			Loggable.BASE BASE = null;
			if (object.getClass().getSimpleName()
					.equals(Loggable.INIT_ALL.class.getSimpleName())) {
				BASE = new Loggable.INIT_ALL();
			} else if (object.getClass().getSimpleName()
					.equals(Loggable.INIT.class.getSimpleName())) {
				BASE = new Loggable.INIT();
			} else if (object.getClass().getSimpleName()
					.equals(Loggable.BEGIN.class.getSimpleName())) {
				BASE = new Loggable.BEGIN();
			} else if (object.getClass().getSimpleName()
					.equals(Loggable.END.class.getSimpleName())) {
				BASE = new Loggable.END();
			} else if (object.getClass().getSimpleName()
					.equals(Loggable.OK.class.getSimpleName())) {
				BASE = new Loggable.OK();
			} else if (object.getClass().getSimpleName()
					.equals(Loggable.KO.class.getSimpleName())) {
				BASE = new Loggable.KO();
			} else if (object.getClass().getSimpleName()
					.equals(Loggable.BASE.class.getSimpleName())) {
				BASE = new Loggable.BASE();
			}
			if (BASE == null) {
				return object;
			}
			Map<String, String> data = null;
			try {
				Method method = object.getClass().getMethod("getData");
				data = (Map<String, String>) method.invoke(object);
			} catch (Exception ex) {
				return object;
			}
			if (data != null) {
				BASE.addData(data);
			}
			return BASE;
		}
		return object;
	}

	public static LoggingEvent modifyLoggingEvent(Loggable.BASE BASE,
			LoggingEvent event) {
		if (BASE.getData() == null || BASE.getData().isEmpty()) {
			return event;
		}
		LocationInfo newLocationInfo = new LocationInfo( //
				BASE.getFile() != null ? BASE.getFile() : event
						.getLocationInformation().getFileName(), //
				BASE.getClassname() != null ? BASE.getClassname() : event
						.getLocationInformation().getClassName(), //
				BASE.getMethod() != null ? BASE.getMethod() : event
						.getLocationInformation().getMethodName(), //
				BASE.getLine() != null ? BASE.getLine() : event
						.getLocationInformation().getLineNumber());
		LoggingEvent newEvent = new LoggingEvent(event.getFQNOfLoggerClass(),
				event.getLogger(), event.getTimeStamp(), event.getLevel(),
				event.getMessage(), event.getThreadName(),
				event.getThrowableInformation(), event.getNDC(),
				newLocationInfo, event.getProperties());
		return newEvent;
	}
}
