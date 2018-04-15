package aero.glass.threading;

/**
 * Interface to run threaded code via the thread manager.
 * The Runnable interface supplies the encapsulation for the code that should be executed.
 *
 * @author DrakkLord
 *
 */
public interface IRunnable {

    /**
     * Called when there is work that needs to be done.
     * @param owner thread that owns this runnable
     * @return {@code TRUE} to keep executing or {@code FALSE} to stop executing and exit the
     * thread loop. The return value is only meaningful for threads that use a loop, so it's
     * ignored for {@link ThreadType#ONE_SHOT}!
     */
    boolean onThreadExecute(IThread owner);

    /**
     * Called when the thread is started and about the enter it's loop.
     * @param owner thread that owns this runnable
     * @return {@code TRUE} if startup was successful, {@code FALSE} if the thread should bail
     * out from the startup sequence because the startup failed.
     */
    boolean onThreadStart(IThread owner);

    /**
     * Called when the thread just exited it's loop and needs to clean up.
     * @param owner thread that owns this runnable
     */
    void onThreadStop(IThread owner);

    /**
     * Get the thread rate, which is the interval of how often the {@link #onThreadExecute}
     * is called.
     * This function is only meaningful for thread types that use
     * timing ( i.e. {@link ThreadType#TIMED } )
     * @param owner thread that owns this runnable
     * @return larger or equal to zero to use the return value as the next sleep time, return
     * less than zero to use the thread's default interval.
     */
    long getThreadRate(IThread owner);

    /**
     * Function called to propagate the name of the runnable to the thread.
     * @return String which is the Runnable's name
     */
    String getThreadName();

    /**
     * Get the offset in thread priority against the main thread priority set for the
     * thread manager.
     * @return offset for thread priority, negative means less than main positive value
     * means higher priority than main. Return 0 for the same priority as main. Values
     * calculated from this conform the Thread rules by limiting the priority between
     * {@link Thread#MAX_PRIORITY} and {@link Thread#MIN_PRIORITY} respectively.
     */
    int getThreadPriorityOffset();
}
