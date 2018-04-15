package aero.glass.threading;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic thread cluster implementation for the thread manager.
 *
 * @author DrakkLord
 *
 */
public class ThreadCluster implements IThread, Runnable {
    public static final int               THREAD_BASE_PRIORITY = Thread.NORM_PRIORITY;

    protected final IRunnable code;
    protected final Object param;
    protected final int stopTimeout;
    protected String name;

    protected volatile Thread thread;
    protected volatile boolean bLazyStop = false;

    private static final Logger LOG = Logger.getLogger(ThreadCluster.class.getName());

    public ThreadCluster(IRunnable codee, Object paramm, int stopTimeoutt) {
        code = codee;
        param = paramm;
        stopTimeout = stopTimeoutt;
        name = code.getThreadName();

        // If no name is set try to figure out one
        if (name == null) {
            name = code.getClass().getSimpleName();
        }
        if (name.length() == 0) {
            name = code.getClass().getName();
        }

        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("ThreadCluster_Android | Created new thread object [" + name + "]");
        }
    }

    protected synchronized void createThreadObject() {
        thread = new Thread(this, "#" + name);

        // setup the priority
        int thrPRI = THREAD_BASE_PRIORITY + code.getThreadPriorityOffset();
        if (thrPRI < Thread.MIN_PRIORITY) {
            thrPRI = Thread.MIN_PRIORITY;
        }
        if (thrPRI > Thread.MAX_PRIORITY) {
            thrPRI = Thread.MAX_PRIORITY;
        }

        thread.setPriority(thrPRI);
    }

    @Override
    public synchronized boolean startThread() {
        if (isActive()) {
            return false;
        }
        createThreadObject();
        bLazyStop = false;
        thread.start();

        // Wait until the thread starts
        // NOTE : this seems to work, using a monitor object just makes this hang
        // FIXME: don't sleep here
        while (!isActive()) {
            sleep(3);
        }

        return isActive();
    }

    @Override
    public boolean stopThread() {
        return stopThread(stopTimeout);
    }

    @Override
    public boolean stopThread(long timeoutMs) {
        // TODO: it's not the best, but others are not working (AG-828)
        final Thread savedThread;
        synchronized (this) {
            savedThread = thread;
        }
        if (savedThread == null || !isActive()) {
            return false;
        }

        // special case when the thread is stopping itself
        if (Thread.currentThread() == savedThread) {
            savedThread.interrupt();
            return isActive();
        }

        // also wake up the thread and wait until it responds
        tellThreadToStop();

        // if the stop timeout is 0, then we will use a lazy stop, meaning that
        // the notification is sent and we assume the thread will exit later.
        if (timeoutMs == 0) {
            bLazyStop = true;
            return true;
        }

        // try to wait for the execution to end
        // if the timeout is negative then wait for infinity
        long now = System.currentTimeMillis();
        try {
            if (timeoutMs < 0) {
                while (isActive()) {
                    savedThread.join(1000);
                    tellThreadToStop();
                }
            } else {
                // split time up and make sure that the thread is notified multiple times!
                long timeFraction = timeoutMs / 4;
                for (int i = 0; isActive() && i < 4; i++) {
                    savedThread.join(timeFraction);
                    tellThreadToStop();
                }
            }
        } catch (InterruptedException e) {
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning("ThreadCluster_Android::stopThread | Interrupted while stopping ["
                                + name + "]");
                e.printStackTrace();
            }
        }

        if (isActive()) {
            throw new ThreadStopTimeoutException(System.currentTimeMillis() - now);
        }

        return true;
    }

    protected synchronized void tellThreadToStop() {
        if (thread == null) {
            return;
        }
        thread.interrupt();
        while (!notifyThread()) {
            sleep(100);
        }
    }

    @Override
    public void run() {
        if (!code.onThreadStart(this)) {
            return;
        }
        code.onThreadExecute(this);
        code.onThreadStop(this);
        synchronized (this) {
            thread = null;
        }
    }

    @Override
    public synchronized boolean isActive() {
        if (thread == null || bLazyStop) {
            return false;
        }
        return thread.isAlive();
    }

    @Override
    public synchronized boolean shouldStop() {
        if (!isActive()) {
            return true;
        }

        boolean bResult;
        bResult = thread.isInterrupted();
        return bResult;
    }

    @Override
    public  boolean waitForCompletion(long timeoutMs) {
        if (!isActive()) {
            return true;
        }
        final Thread savedThread;
        synchronized (this) {
            savedThread = thread;
        }
        if (savedThread == null) {
            return true;
        }
        if (Thread.currentThread() == savedThread) {
            throw new UnsupportedOperationException("A thread can't wait on itself!");
        }

        try {
            savedThread.join(timeoutMs);
        } catch (InterruptedException e) {
            return !isActive();
        }
        return !isActive();
    }

    @Override
    public synchronized boolean sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }

    @Override
    public synchronized boolean notifyThread() {
        return true;
    }

    @Override
    public synchronized ThreadType getThreadType() {
        return ThreadType.ONE_SHOT;
    }

    @Override
    public final String getThreadName() {
        return name;
    }

    @Override
    public final Object getParam() {
        return param;
    }

    @Override
    public final IRunnable getRunnable() {
        return code;
    }

    @Override
    public final synchronized  String toString() {
        if (thread == null) {
            return "Thread doesn't exist";
        } else {
            return "Thread name " + thread.getName() + ", id " + thread.getId()
                    + ", isAlive : " + thread.isAlive();
        }
    }
}
