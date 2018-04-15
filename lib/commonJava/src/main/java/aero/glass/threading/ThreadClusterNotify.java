package aero.glass.threading;

/**
 * Thread cluster implementation that uses notification.
 *
 * @author DrakkLord
 */
public class ThreadClusterNotify extends ThreadCluster {

    private final Object monitorObj;
    private volatile boolean bWasSignaled;
    private volatile boolean stopRequested;

    public ThreadClusterNotify(IRunnable codee, Object paramm, int stopTimeoutt) {
        super(codee, paramm, stopTimeoutt);
        monitorObj = new Object();
        bWasSignaled = false;
        stopRequested = false;
    }

    @Override
    public  boolean stopThread(long timeoutMs) {
        stopRequested = true;
        tellThreadToStop();
        return super.stopThread(timeoutMs);
    }

    @Override
    public void run() {
        if (!code.onThreadStart(this)) {
            synchronized (this) {
                thread = null;
            }
            return;
        }

        while (!thread.isInterrupted()) {
            synchronized (monitorObj) {
                while (!bWasSignaled) {
                    try {
                        monitorObj.wait();
                    } catch (InterruptedException e) {
                        thread.interrupt();
                        break;
                    }
                }
                bWasSignaled = false;
            }

            if (thread.isInterrupted() || stopRequested) {
                break;
            }

            if (!code.onThreadExecute(this)) {
                break;
            }
        }

        code.onThreadStop(this);
        synchronized (this) {
            thread = null;
        }
    }

    @Override
    public synchronized boolean notifyThread() {
        if (!isActive()) {
            return true;
        }
        if (Thread.currentThread() == thread) {
            throw new UnsupportedOperationException("A thread can't notify itself!");
        }

        synchronized (monitorObj) {
            if (bWasSignaled) {
                monitorObj.notifyAll();
                return true;
            }
            bWasSignaled = true;
            monitorObj.notifyAll();
        }
        return bWasSignaled;
    }

    @Override
    public ThreadType getThreadType() {
        return ThreadType.NOTIFY;
    }
}
