package aero.glass.threading;

/**
 * Exception thrown when the stopThread function fails with a timeout, meaning that the thread
 * may still be running in the background.
 */
public class ThreadStopTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ThreadStopTimeoutException(long timePassed) {
        super("Timeout out after " + timePassed + " ms, in thread stop function");
    }
}
