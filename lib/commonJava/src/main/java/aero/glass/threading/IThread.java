package aero.glass.threading;

/**
 * Thread interface that is the basis of the platform specific threading implementation.
 *
 * @author DrakkLord
 *
 */
public interface IThread {

    /**
     * Start thread, if already running this function does nothing.
     * @return {@code TRUE} if the thread just started, {@code FALSE} if it's already running.
     */
    boolean startThread();

    /**
     * Stop thread, if already stopped this function does nothing.
     * @return {@code TRUE} if the thread just stopped,{@code FALSE} if it's already stopped.
     */
    boolean stopThread();

    /**
     * Stop thread, if already stopped this function does nothing.
     * @param timeoutMs the amount of time in ms to wait for the thread to stop NOTE:
     * this WILL override the thread's internal stop timeout!
     * @return {@code TRUE} if the thread just stopped,{@code FALSE} if it's already stopped.
     */
    boolean stopThread(long timeoutMs);

    /**
     * Wait for the thread to stop with an optional timeout.
     * @param timeoutMs amount of time in ms to wait before returning,
     * use 0 to do a non-blocking check.
     * @return {@code TRUE} if the thread stopped {@code FALSE} if the thread is still
     * running or the wait timed out ( also meaning that the thread is still running ).
     */
    boolean waitForCompletion(long timeoutMs);

    /**
     * Function to tell if the thread is currently active.
     * @return {@code TRUE} if the thread's main function is active, {@code FALSE} if
     * the thread already finished ( stopped ).
     */
    boolean isActive();

    /**
     * Function used by the runnable to detect if a stop signal is waiting.
     * @return {@code TRUE} if there is an active stop signal meaning that the thread must.
     * stop executing and return immediately.
     */
    boolean shouldStop();

    /**
     * Sleep for the specified amount of time.
     * @param timeout amount of time in ms to wait
     * @return {@code TRUE} if the sleep was successful, so at least timeout amount of time passed,
     * {@code FALSE} if the sleep was interrupted.
     */
    boolean sleep(long timeout);

    /**
     * Notify the thread to wake up.
     * NOTE : only works for threads ( see {@link ThreadType} ) that use a notification scheme.
     * @return {@code TRUE} if the notification is still pending since the last call to
     * this function, {@code FALSE} if the notification is not pending.
     */
    boolean notifyThread();

    /**
     * Get the type of the thread object.
     * @return {@code ThreadType} which denotes the type of this thread object.
     */
    ThreadType getThreadType();

    /**
     * Get the therad's name.
     * @return {@code STRING} that is the thread's name
     */
    String getThreadName();

    /**
     * Return the user param.
     * @return {@code Object} that is sent trough object creation.
     */
    Object getParam();

    /**
     * Get the IRunnable interface that is wrapped into this thread.
     * @return {@code IRunnable} that is used to create this thread.
     */
    IRunnable getRunnable();
}
