package kkr.deeplog.rollingpolicy;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.pattern.DatePatternConverter;
import org.apache.log4j.pattern.ExtrasFormattingInfo;
import org.apache.log4j.pattern.ExtrasPatternParser;
import org.apache.log4j.pattern.IntegerPatternConverter;
import org.apache.log4j.pattern.PatternConverter;
import org.apache.log4j.rolling.RollingPolicy;
import org.apache.log4j.rolling.RolloverDescription;
import org.apache.log4j.rolling.RolloverDescriptionImpl;
import org.apache.log4j.rolling.helper.Action;
import org.apache.log4j.rolling.helper.FileRenameAction;
import org.apache.log4j.rolling.helper.GZCompressAction;
import org.apache.log4j.rolling.helper.ZipCompressAction;
import org.apache.log4j.spi.OptionHandler;

public final class SizeRollingPolicy implements RollingPolicy, OptionHandler {

	private static final String COMPRESS_METHOD_ZIP = "zip";
	private static final String COMPRESS_METHOD_GZ = "gz";
	private static final String COMPRESS_METHOD_NONE = "none";

	protected String compressMethod;

	private String fileNamePatternStr;
	private PatternConverter[] patternConverters;
	private ExtrasFormattingInfo[] patternFields;

	public SizeRollingPolicy() {
	}

	public RolloverDescription initialize(String file, boolean append) {
		return new RolloverDescriptionImpl(file, append, null, null);
	}

	private File fileToCompressedFile(File file) {
		if (COMPRESS_METHOD_ZIP.equals(compressMethod)) {
			if (file.getParentFile() != null) {
				return new File(file.getParentFile(), file.getName() + ".zip");
			} else {
				return new File(file.getName() + ".zip");
			}
		}
		if (COMPRESS_METHOD_GZ.equals(compressMethod)) {
			if (file.getParentFile() != null) {
				return new File(file.getParentFile(), file.getName() + ".gz");
			} else {
				return new File(file.getName() + ".gz");
			}
		}
		return file;
	}

	public RolloverDescription rollover(String currentFileName) {
		Date date = new Date();

		File fileTarget = null;
		File fileTargetZip = null;
		for (int i = 0;; i++) {
			fileTarget = formatFileName(i, date);
			fileTargetZip = fileToCompressedFile(fileTarget);
			if (!fileTarget.exists() && !fileTargetZip.exists()) {
				break;
			}
		}

		Action compressAction = null;
		if (compressMethod != null) {
			if (COMPRESS_METHOD_ZIP.equals(compressMethod)) {
				compressAction = new ZipCompressAction(fileTarget,
						fileTargetZip, true);
			} else if (COMPRESS_METHOD_GZ.equals(compressMethod)) {
				compressAction = new GZCompressAction(fileTarget,
						fileTargetZip, true);
			}
		}

		FileRenameAction renameAction = new FileRenameAction(new File(
				currentFileName), fileTarget, false);

		return new RolloverDescriptionImpl(currentFileName, false,
				renameAction, compressAction);
	}

	public String getCompressMethod() {
		return compressMethod;
	}

	public void setCompressMethod(String compressMethod) {
		this.compressMethod = compressMethod;
	}

	public void setFileNamePattern(String fnp) {
		this.fileNamePatternStr = fnp;
	}

	public String getFileNamePattern() {
		return this.fileNamePatternStr;
	}

	protected final void parseFileNamePattern() {
		List<Object> converters = new ArrayList<Object>();
		List<Object> fields = new ArrayList<Object>();

		ExtrasPatternParser.parse(this.fileNamePatternStr, converters, fields,
				null, ExtrasPatternParser.getFileNamePatternRules());

		this.patternConverters = new PatternConverter[converters.size()];
		this.patternConverters = ((PatternConverter[]) (PatternConverter[]) converters
				.toArray(this.patternConverters));

		this.patternFields = new ExtrasFormattingInfo[converters.size()];
		this.patternFields = ((ExtrasFormattingInfo[]) (ExtrasFormattingInfo[]) fields
				.toArray(this.patternFields));
	}

	public void activateOptions() {
		if (this.compressMethod != null) {
			if (COMPRESS_METHOD_NONE.equals(this.compressMethod)) {
				this.compressMethod = null;
			} else if (!COMPRESS_METHOD_ZIP.equals(this.compressMethod)
					&& !COMPRESS_METHOD_GZ.equals(this.compressMethod)) {
				throw new IllegalStateException("The CompressMethod must be '"
						+ COMPRESS_METHOD_ZIP + "' od '" + COMPRESS_METHOD_GZ
						+ "': " + this.compressMethod);
			}
		}
		if (this.fileNamePatternStr != null) {
			parseFileNamePattern();
			String lc = this.fileNamePatternStr.toLowerCase();
			if (lc.endsWith(".gz") || lc.endsWith(".zip")) {
				throw new IllegalStateException(
						"The FileNamePattern option must not be terminated by '.gz' or '.zip'. Use CompressMethode instead.");
			}
		} else {
			LogLog.warn("The FileNamePattern option must be set before using RollingPolicy. ");
			LogLog.warn("See also http://logging.apache.org/log4j/codes.html#tbr_fnp_not_set");
			throw new IllegalStateException(
					"The FileNamePattern option must be set before using RollingPolicy. See also http://logging.apache.org/log4j/codes.html#tbr_fnp_not_set");
		}
		PatternConverter itc = getIntegerPatternConverter();

		if (itc == null)
			throw new IllegalStateException("FileNamePattern ["
					+ getFileNamePattern()
					+ "] does not contain a valid integer format specifier");
	}

	protected final PatternConverter getDatePatternConverter() {
		for (int i = 0; i < this.patternConverters.length; ++i) {
			if (this.patternConverters[i] instanceof DatePatternConverter) {
				return this.patternConverters[i];
			}
		}
		return null;
	}

	protected final PatternConverter getIntegerPatternConverter() {
		for (int i = 0; i < this.patternConverters.length; ++i) {
			if (this.patternConverters[i] instanceof IntegerPatternConverter) {
				return this.patternConverters[i];
			}
		}
		return null;
	}

	protected final File formatFileName(Integer index, Date date) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < this.patternConverters.length; ++i) {
			int fieldStart = buf.length();
			if (this.patternConverters[i] instanceof IntegerPatternConverter) {
				this.patternConverters[i].format(index, buf);
			} else if (this.patternConverters[i] instanceof DatePatternConverter) {
				this.patternConverters[i].format(date, buf);
			} else {
				this.patternConverters[i].format(index, buf);
			}

			if (this.patternFields[i] != null)
				this.patternFields[i].format(fieldStart, buf);
		}
		File file = new File(buf.toString());
		return file;
	}
}