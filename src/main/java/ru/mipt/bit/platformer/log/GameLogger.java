package ru.mipt.bit.platformer.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Game logger. */
public class GameLogger {
    /** Log4j logger. */
    private final Logger logger;

    /**
     * @param clazz Clazz.
     */
    public GameLogger(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    /**
     * @param clazz Clazz.
     */
    public static GameLogger getLogger(Class<?> clazz) {
        return new GameLogger(clazz);
    }

    /**
     * @param message Message.
     */
    public void debug(String message) {
        logger.debug(message);
    }

    /**
     * @param message Message.
     * @param params Params.
     */
    public void debug(String message, Object... params) {
        logger.debug(message, params);
    }

    /**
     * @param message Message.
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * @param message Message.
     * @param params Params.
     */
    public void info(String message, Object... params) {
        logger.info(message, params);
    }
}
