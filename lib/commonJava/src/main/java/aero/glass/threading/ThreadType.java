package aero.glass.threading;

/**
 * Thread type enumerator, these types are analogus to the implementations on the platform,
 * see thread clusters!
 *
 * @author DrakkLord
 *
 */
public enum ThreadType {

    /**
     * Goes trough all methods once and exits.
     */
    ONE_SHOT,

    /**
     * Uses a built in delay ( {@code Sleep()} ) to execute the main function with an interval.
     */
    TIMED,

    /**
     * Uses a notification scheme where the thread must be notified ( waken up ) if work needs
     * to be done, otherwise the thread just waits for the notification.
     */
    NOTIFY
}
