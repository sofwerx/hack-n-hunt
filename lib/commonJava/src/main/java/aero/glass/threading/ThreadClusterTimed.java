package aero.glass.threading;

/**
 * Thread cluster implementation that uses timing.
 *
 * @author DrakkLord
 *
 */
public class ThreadClusterTimed extends ThreadCluster {

    protected long defaultRate;
    protected boolean bStrictRate;

    public ThreadClusterTimed(IRunnable codee, Object paramm, int stopTimeoutt, long defaultRatee,
                              boolean bStrictRatee) {
        super(codee, paramm, stopTimeoutt);
        defaultRate = defaultRatee;
        bStrictRate = bStrictRatee;
    }

    @Override
    public void run() {
        if (!code.onThreadStart(this)) {
            synchronized (this) {
                thread = null;
            }
            return;
        }

        long pauseTime = 0;
        if (!bStrictRate) {
            while (!thread.isInterrupted()) {

                if (!code.onThreadExecute(this)) {
                    break;
                }

                if (thread.isInterrupted()) {
                    break;
                }

                pauseTime = getPauseTime();
                if (pauseTime > 0) {
                    try {
                        Thread.sleep(pauseTime);
                    } catch (InterruptedException e) {
                        thread.interrupt();
                    }
                }
            }
        } else {
            long now, timeDiff;

            while (!thread.isInterrupted()) {

                now = System.currentTimeMillis();

                if (!code.onThreadExecute(this)) {
                    break;
                }

                if (thread.isInterrupted()) {
                    break;
                }

                pauseTime = getPauseTime();
                timeDiff = System.currentTimeMillis() - now;
                if (timeDiff < 0) {
                    timeDiff = 0;
                }

                if (timeDiff < pauseTime) {
                    pauseTime -= timeDiff;
                    if (pauseTime > 0) {
                        try {
                            Thread.sleep(pauseTime);
                        } catch (InterruptedException e) {
                            thread.interrupt();
                        }
                    }
                }
            }
        }

        code.onThreadStop(this);
        synchronized (this) {
            thread = null;
        }
    }

    @Override
    public ThreadType getThreadType() {
        return ThreadType.TIMED;
    }

    private long getPauseTime() {
        long time = code.getThreadRate(this);
        if (time < 0) {
            time = defaultRate;
        }
        return time;
    }
}
