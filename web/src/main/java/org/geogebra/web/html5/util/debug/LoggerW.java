package org.geogebra.web.html5.util.debug;

import java.util.Date;

import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.util.AppletParameters;

import com.google.gwt.user.client.Window;

import elemental2.dom.DomGlobal;
import jsinterop.base.Js;

/**
 * GeoGebraLogger implementation for the web platform
 * 
 * @author Zoltan Kovacs
 */
public class LoggerW extends Log {

	private String getTimeInfo() {
		Date date = new Date();
		return date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds() + ": ";
	}

	@Override
	public void print(Level level, Object logEntry) {
		if (logEntry instanceof Throwable) {
			printErrorMessage((Throwable) logEntry);
			return;
		}

		switch (level) {
		case INFO:
			DomGlobal.console.info(getTimeInfo(), logEntry);
			break;
		case WARN:
			DomGlobal.console.warn(getTimeInfo(), logEntry);
			break;
		case ERROR:
			DomGlobal.console.error(getTimeInfo(), logEntry);
			break;
		case TRACE:
			DomGlobal.console.trace(getTimeInfo(), logEntry);
			break;
		default:
		case DEBUG:
			DomGlobal.console.log(getTimeInfo(), logEntry);
			break;
		}
	}

	/**
	 * Start logger if parameters allow it.
	 * 
	 * @param article
	 *            parameters
	 */
	public static void startLogger(AppletParameters article) {
		if (article.getDataParamShowLogging()) {
			Log.setLogger(new LoggerW());
			Log.setLogDestination(LogDestination.CONSOLE);
			Log.setLogLevel(Window.Location.getParameter("logLevel"));
		}
	}

	private void printErrorMessage(Throwable t) {
		// This contains the stacktrace in gwt dev mode.
		Object backingJsObject = Js.asPropertyMap(t).nestedGet("backingJsObject.stack");
		DomGlobal.console.error(t, backingJsObject);
	}

	public static void loaded(String string) {
		debug("Loaded: " + string);
	}
}
