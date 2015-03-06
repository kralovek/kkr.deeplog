package kkr.deeplog.listener;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

public class Log4jListener implements BuildLogger {
	private static final String LOG_ANT = "org.apache.tools.ant";
	private static final Logger loggerAnt = Logger.getLogger(LOG_ANT);
	private static final Logger loggerProject = Logger.getLogger(Project.class);
	private static final Logger loggerTarget = Logger.getLogger(Target.class);
	private static final Logger loggerTask = Logger.getLogger(Task.class);

	protected PrintStream out;
	protected PrintStream err;
	protected int msgOutputLevel = 0;
	protected boolean emacsMode = false;

	public Log4jListener() {
		Logger rootLog = Logger.getRootLogger();
		boolean initialized = !(rootLog.getAllAppenders() instanceof NullEnumeration);
		if (!initialized) {
			loggerAnt.error(Log4jListener.class.getSimpleName()
					+ ": No log4j.properties in build area");
		}
	}

	public void setMessageOutputLevel(int level) {
		this.msgOutputLevel = level;
	}

	public void setOutputPrintStream(PrintStream output) {
		this.out = new PrintStream(output, true);
	}

	public void setErrorPrintStream(PrintStream err) {
		this.err = new PrintStream(err, true);
	}

	public void setEmacsMode(boolean emacsMode) {
		this.emacsMode = emacsMode;
	}

	public void buildStarted(BuildEvent event) {
		loggerProject
				.info("####################################################################################################");
		loggerProject.info("BEGIN");
	}

	public void buildFinished(BuildEvent event) {
		try {
			if (event.getException() == null) {
				loggerProject.info("OK");
			} else {
				loggerProject.info("Build finished with errors: "
						+ event.getException().getMessage());
			}
		} finally {
			loggerProject.info("END");
			loggerProject
					.info("####################################################################################################");
		}
	}

	public void targetStarted(BuildEvent event) {
		loggerTarget.info("BEGIN: " + event.getTarget().getName());
	}

	public void targetFinished(BuildEvent event) {
		try {
			if (event.getException() == null) {
				loggerTarget.info("Target finished OK: "
						+ event.getTarget().getName());
				loggerTarget.info("OK");
			} else {
				loggerTarget.error("Target finished with errors: "
						+ event.getTarget().getName());
			}
		} finally {
			loggerTarget.info("END: " + event.getTarget().getName());
		}
	}

	public void taskStarted(BuildEvent event) {
		int priority = event.getPriority();
		if (priority > this.msgOutputLevel)
			return;

		loggerTask.info("BEGIN: " + event.getTask().getTaskName());
	}

	public void taskFinished(BuildEvent event) {
		int priority = event.getPriority();
		if (priority > this.msgOutputLevel)
			return;

		try {
			if (event.getException() == null) {
				loggerTask.info("Task finished OK: "
						+ event.getTask().getTaskName());
				loggerTask.info("OK");
			} else {
				loggerTask.error("Task finished with errors: "
						+ event.getTask().getTaskName());
			}
		} finally {
			loggerTask.info("END: " + event.getTask().getTaskName());
		}
	}

	public void messageLogged(BuildEvent event) {
		int priority = event.getPriority();
		if (priority > this.msgOutputLevel)
			return;

		Object categoryObject = event.getTask();
		if (categoryObject == null) {
			categoryObject = event.getTarget();
			if (categoryObject == null) {
				categoryObject = event.getProject();
			}
		}

		Logger log = Logger.getLogger(categoryObject.getClass().getName());
		switch (event.getPriority()) {
		case Project.MSG_ERR:
			log.error(event.getMessage());
			break;
		case Project.MSG_WARN:
			log.warn(event.getMessage());
			break;
		case Project.MSG_INFO:
			log.info(event.getMessage());
			break;
		case Project.MSG_VERBOSE:
			log.debug(event.getMessage());
			break;
		case Project.MSG_DEBUG:
			log.debug(event.getMessage());
			break;
		default:
			log.error(event.getMessage());
			break;
		}
	}
}
