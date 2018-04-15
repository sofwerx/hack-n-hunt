package aero.glass.threading;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Utility class for thread creation and destruction.
 *
 *  created by: Zoltan Pillio 2016.08.04
 */
public final class ThreadClusterFactory {

    private static final Logger LOG = Logger.getLogger(ThreadClusterFactory.class.getName());

    public static final int               DEFAULT_STOP_TIMEOUT  = 5000;
    public static final int               INFINITE_STOP_TIMEOUT = -1;
    public static final int               DEFAULT_RATE          = 1000;
    private static final AtomicInteger    THREAD_COUNT          = new AtomicInteger(0);
    private static final StringHolder     EXEPTION_LOG          = new StringHolder("History:\n");

    /**
     * String holder.
     */
    static class StringHolder {
        private String stringBuffer;

        StringHolder(String s) {
            append(s);
        }

        public String get() {
            return (stringBuffer == null) ? "" : stringBuffer;
        }

        public final void append(String s) {
            if (stringBuffer == null) {
                stringBuffer = s;
            } else {
                stringBuffer = stringBuffer + s;
            }
        }
    }

    private ThreadClusterFactory() {
    }

    // Type - One Shot
    /**
     * <p>Create a one shot thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#ONE_SHOT}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @return the thread freshly created
     */
    public static IThread createThreadOneShot(IRunnable code) {
        return createThreadOneShot(code, null, DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a one shot thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#ONE_SHOT}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @return the thread freshly created
     */
    public static IThread createThreadOneShot(IRunnable code, Object param) {
        return createThreadOneShot(code, param, DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a one shot thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#ONE_SHOT}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @return the thread freshly created
     */
    public static IThread createThreadOneShot(IRunnable code, Object param,
            int stopTimeout) {
        return createThreadOneShot(code, param, stopTimeout, false);
    }

    /**
     * <p>Create a one shot thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#ONE_SHOT}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @param bNoAutoStart if set to {@code TRUE} the thread object is not started automatically
     * so the {@link IThread#startThread()} must called explicitly.
     * @return the thread freshly created
     */
    public static IThread createThreadOneShot(IRunnable code, Object param, int stopTimeout,
                                       boolean bNoAutoStart) {
        return createThread(code, ThreadType.ONE_SHOT, param, stopTimeout, 0,
                false, bNoAutoStart);
    }

    // Type - Notify
    /**
     * <p>Create a notify based thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#NOTIFY}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @return the thread freshly created
     */
    public static IThread createThreadNotify(IRunnable code) {
        return createThreadNotify(code, null, DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a notify based thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#NOTIFY}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @return the thread freshly created
     */
    public static IThread createThreadNotify(IRunnable code, Object param) {
        return createThreadNotify(code, param, DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a notify based thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#NOTIFY}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @return the thread freshly created
     */
    public static IThread createThreadNotify(IRunnable code, Object param,
            int stopTimeout) {
        return createThreadNotify(code, param, stopTimeout, false);
    }

    /**
     * <p>Create a notify based thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#NOTIFY}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @param bNoAutoStart if set to {@code TRUE} the thread object is not started automatically
     * so the {@link IThread#startThread()} must called explicitly.
     * @return the thread freshly created
     */
    public static IThread createThreadNotify(IRunnable code, Object param, int stopTimeout,
                                      boolean bNoAutoStart) {
        return createThread(code, ThreadType.NOTIFY, param, stopTimeout, 0,
                false, bNoAutoStart);
    }

    // Type - Timed
    /**
     * <p>Create a timed thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#TIMED}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @return the thread freshly created
     */
    public static IThread createThreadTimed(IRunnable code) {
        return createThreadTimed(code, null, DEFAULT_RATE, false,
                DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a timed thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#TIMED}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @return the thread freshly created
     */
    public static IThread createThreadTimed(IRunnable code, Object param) {
        return createThreadTimed(code, param, DEFAULT_RATE, false,
                DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a timed thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#TIMED}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param defaultRate for timed threads the amount of time in ms to wait between calls to
     * {@link IRunnable#onThreadExecute(IThread owner)}.
     * @return the thread freshly created
     */
    public static IThread createThreadTimed(IRunnable code, Object param,
            long defaultRate) {
        return createThreadTimed(code, param, defaultRate, false,
                DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a timed thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#TIMED}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param defaultRate for timed threads the amount of time in ms to wait between calls to
     * {@link IRunnable#onThreadExecute(IThread owner)}.
     * @param bStrictRate if set to {@code TRUE} then the rate specifies the minimal time that
     * should elapse between calls to {@link IRunnable#onThreadExecute(IThread owner)} which
     * includes the time the function call takes, if {@code FALSE} the time is fixed between
     * calls to the execute function meaning that the actual time the function takes is ignored.

     * @return the thread freshly created
     */
    public static IThread createThreadTimed(IRunnable code, Object param,
            long defaultRate, boolean bStrictRate) {
        return createThreadTimed(code, param, defaultRate, bStrictRate,
                DEFAULT_STOP_TIMEOUT, false);
    }

    /**
     * <p>Create a timed thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#TIMED}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param defaultRate for timed threads the amount of time in ms to wait between calls to
     * {@link IRunnable#onThreadExecute(IThread owner)}.
     * @param bStrictRate if set to {@code TRUE} then the rate specifies the minimal time that
     * should elapse between calls to {@link IRunnable#onThreadExecute(IThread owner)} which
     * includes the time the function call takes, if {@code FALSE} the time is fixed between
     * calls to the execute function meaning that the actual time the function takes is ignored.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @return the thread freshly created
     */
    public static IThread createThreadTimed(IRunnable code, Object param,
            long defaultRate, boolean bStrictRate, int stopTimeout) {
        return createThreadTimed(code, param, defaultRate, bStrictRate,
                stopTimeout, false);
    }

    /**
     * <p>Create a timed thread wrapper.</p>
     * <p>See : {@link createThread} and {@link ThreadType#TIMED}</p>
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param param user parameter that is available trough the thread object.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @param defaultRate for timed threads the amount of time in ms to wait between calls to
     * {@link IRunnable#onThreadExecute(IThread owner)}.
     * @param bStrictRate if set to {@code TRUE} then the rate specifies the minimal time that
     * should elapse between calls to {@link IRunnable#onThreadExecute(IThread owner)} which
     * includes the time the function call takes, if {@code FALSE} the time is fixed between
     * calls to the execute function meaning that the actual time the function takes is ignored.
     * @param bNoAutoStart if set to {@code TRUE} the thread object is not started automatically
     * so the {@link IThread#startThread()} must called explicitly.
     * @return the thread freshly created
     */
    public static IThread createThreadTimed(IRunnable code, Object param,
            long defaultRate, boolean bStrictRate, int stopTimeout, boolean bNoAutoStart) {
        return createThread(code, ThreadType.TIMED, param, stopTimeout,
                defaultRate, bStrictRate, bNoAutoStart);
    }

    // Generic
    /**
     * Create a thread object that wraps an {@link IRunnable} interface into a thread.
     * The type specifies how the thread behaves other parameters are interpreted as they
     * needed based on the type.
     * @param code the {@link IRunnable} that will be wrapped into and executed by the thread.
     * @param type type of the thread based on {@link ThreadType}.
     * @param param user parameter that is available trough the thread object.
     * @param stopTimeout amount of time in ms to wait for the thread to stop, if this is
     * set to 0 then "lazy stopping" is used which assumes the thread stopped ( even tho
     * only the stop request is sent ) and proceeds without blocking
     * @param defaultRate for timed threads the amount of time in ms to wait between calls to
     * {@link IRunnable#onThreadExecute(IThread owner)}.
     * @param bStrictRate if set to {@code TRUE} then the rate specifies the minimal time that
     * should elapse between calls to {@link IRunnable#onThreadExecute(IThread owner)} which
     * includes the time the function call takes, if {@code FALSE} the time is fixed between
     * calls to the execute function meaning that the actual time the function takes is ignored.
     * @param bNoAutoStart if set to {@code TRUE} the thread object is not started automatically
     * so the {@link IThread#startThread()} must called explicitly.
     * @return the thread freshly created
     */
    public static IThread createThread(IRunnable code, ThreadType type, Object param,
            int stopTimeout, long defaultRate, boolean bStrictRate,
            boolean bNoAutoStart) {

        if (code == null) {
            throw new IllegalArgumentException("Runnable is null");
        }

        long baseDefaultRate = defaultRate;
        if (baseDefaultRate < 0) {
            baseDefaultRate = 0;
            errorLog("ThreadClusterFactory::createThread | default rate is less than zero!");
        }

        String threadName = code.getThreadName();
        if (threadName == null || threadName.length() == 0) {
            threadName = code.getClass().getSimpleName();
        }
        if (threadName == null
                || threadName != null && threadName.length() == 0) {
            threadName = code.getClass().getName();
        }
        if (threadName == null) {
            threadName = "unknown";
        }

        normalLog("ThreadClusterFactory::createThread | creating thread object for ["
                        + threadName + "]");

        IThread thread = createThreadObject(code, type, param, stopTimeout,
                baseDefaultRate, bStrictRate);

        if (!bNoAutoStart) {
            thread.startThread();
        }
        return thread;
    }

    private static IThread createThreadObject(IRunnable code, ThreadType type, Object param,
                                         int stopTimeout, long defaultRate, boolean bStrictRate) {
        ThreadCluster thread;
        switch (type) {

            case ONE_SHOT:
                thread = new ThreadCluster(code, param, stopTimeout);
                break;
            case NOTIFY:
                thread = new ThreadClusterNotify(code, param, stopTimeout);
                break;
            case TIMED:
                thread = new ThreadClusterTimed(code, param, stopTimeout, defaultRate, bStrictRate);
                break;
            default:
                throw new IllegalArgumentException("Invalid ThreadType : " + type);
        }
        normalLog("created thread: " + thread.name + ", total " + THREAD_COUNT.incrementAndGet());
        return thread;
    }

    public static void destroyThread(IThread thread) {
        final String threadToString = thread.toString();
        thread.stopThread();
        normalLog("destroyed thread: " + threadToString + ", remaining: "
                + THREAD_COUNT.decrementAndGet());
    }

    public static void logThreadNum() {
        normalLog("active thread num: " + THREAD_COUNT.get());
    }

    public static void finalCheck() {
        if (THREAD_COUNT.get() != 0) {
            synchronized (EXEPTION_LOG) {
                errorLog("Remaining thread num: " + THREAD_COUNT.get());
                for (String string : EXEPTION_LOG.get().split("\n")) {
                    errorLog(string);
                }
            }
        }
    }

    /* Logging */
    private static void normalLog(String str) {
        synchronized (EXEPTION_LOG) {
            EXEPTION_LOG.append(System.currentTimeMillis() + " | " + str + "\n");
        }
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info(str);
        }
    }

    private static void errorLog(String str) {
        synchronized (EXEPTION_LOG) {
            EXEPTION_LOG.append(System.currentTimeMillis() + " | " + str + "\n");
        }
        if (LOG.isLoggable(Level.SEVERE)) {
            LOG.severe(str);
        }
    }
    /* Logging */
}
