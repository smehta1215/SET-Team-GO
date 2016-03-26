package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
	private static Logger logger = Logger.getLogger("Logging");;
	private FileHandler debugFileHandler;
	private FileHandler exceptionFileHandler;

	public Logging() {
		try {
			debugFileHandler = new FileHandler("data/debug.log");
			exceptionFileHandler = new FileHandler("data/error.log");
			debugFileHandler.setLevel(Level.ALL);
			exceptionFileHandler.setLevel(Level.SEVERE);
			SimpleFormatter formatter = new SimpleFormatter();

			logger.addHandler(debugFileHandler);
			debugFileHandler.setFormatter(formatter);

			logger.addHandler(exceptionFileHandler);
			exceptionFileHandler.setFormatter(formatter);

		} catch (IOException ex) {
			debug(ex, null);
		} catch (SecurityException ex) {
			debug(ex, null);
		}
	}

	/**
	 * logs debug message to file
	 * 
	 * @param level
	 * @param msg
	 *            debug message
	 */
	public static void debug(Level level, String msg) {
		if (logger != null) {
			logger.log(level, msg);
		}
	}

	/**
	 * logs error message to file
	 * 
	 * @param exception
	 * @param msg
	 *            error message
	 */
	public static void debug(Throwable exception, String msg) {
		if (logger != null) {
			logger.log(Level.SEVERE, msg, exception);
		}
	}
}