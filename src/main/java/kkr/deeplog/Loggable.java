package kkr.deeplog;

import java.util.HashMap;
import java.util.Map;

public interface Loggable {
	static class BASE {
		public static final String MESSAGE = "MESSAGE";
		public static final String FILE = "FILE";
		public static final String LINE = "LINE";
		public static final String CLASS = "CLASS";
		public static final String METHOD = "METHOD";
		private Map<String, String> data = new HashMap<String, String>();

		public BASE() {
		}

		public BASE(Object message) {
			data.put(MESSAGE, message != null ? message.toString() : "");
		}

		public String toString() {
			return data.get(MESSAGE) != null ? data.get(MESSAGE).toString() : this.getClass().getSimpleName();
		}

		public Object getMessage() {
			return data.get(MESSAGE);
		}

		public String getClassname() {
			return data.get(CLASS);
		}

		public BASE addClassname(String classname) {
			data.put(CLASS, classname);
			return this;
		}

		public String getMethod() {
			return data.get(METHOD);
		}

		public BASE addMethod(String method) {
			data.put(METHOD, method);
			return this;
		}

		public String getFile() {
			return data.get(FILE);
		}

		public BASE addFile(String file) {
			data.put(FILE, file);
			return this;
		}

		public String getLine() {
			return data.get(LINE);
		}

		public BASE addLine(String line) {
			data.put(LINE, line);
			return this;
		}
		
		public Map<String, String> getData() {
			return data;
		}
		public BASE addData(Map<String, String> data) {
			this.data = data;
			return this;
		}
	}

	static class INIT_ALL extends BASE {
		public INIT_ALL() {
		}

		public INIT_ALL(Object object) {
			super(object);
		}
	}

	static final INIT_ALL INIT_ALL = new INIT_ALL();

	static class INIT extends BASE {
		public INIT() {
		}

		public INIT(Object object) {
			super(object);
		}
	}

	static final INIT INIT = new INIT();

	static class BEGIN extends BASE {
		public BEGIN() {
		}

		public BEGIN(Object object) {
			super(object);
		}
	}

	static final BEGIN BEGIN = new BEGIN();

	static class END extends BASE {
		public END() {
		}

		public END(Object object) {
			super(object);
		}
	}

	static final END END = new END();

	static class OK extends BASE {
		public OK() {
		}

		public OK(Object object) {
			super(object);
		}
	}

	static final OK OK = new OK();

	static class KO extends BASE {
		public KO() {
		}

		public KO(Object object) {
			super(object);
		}
	}

	static final KO KO = new KO();
}
