package drupalfit;

public final class Log8 {
    private static final String TAG = "Log8";
    private static String sTag = TAG;

    private static boolean sEnabled = true;

    public static boolean enabledI = sEnabled;
    public static boolean enabledD = sEnabled;
    public static boolean enabledV = sEnabled;
    public static boolean enabledW = sEnabled;
    public static boolean enabledE = true;

    public static boolean printStackTrace = false;

    /**
     * <pre>
     * Log8.v("test");
     * Log8.v("a=", a, "b=", b);
     * Log8.v("This is an array:", array);
     * </pre>
     */
    public static String getMsg(Object... arr) {
        StackTraceElement call = Thread.currentThread().getStackTrace()[4];
        String className = call.getClassName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return className + "."
            + call.getMethodName() + ":"
            + call.getLineNumber() + ": "
            + java.util.Arrays.deepToString(arr);
    }

    public static boolean isEnabled() {
        return sEnabled;
    }

    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
        updateEnabled();
    }

    public static void updateEnabled() {
        enabledI = sEnabled;
        enabledD = sEnabled;
        enabledV = sEnabled;
        enabledW = sEnabled;
    }

    public static void setTag(String tag) {
        sTag = tag;
    }

    public static String getTag() {
        return sTag;
    }

    public static void resetTag() {
        sTag = TAG;
    }

    public static int v(Object... arr) {
        if (!enabledV) return 0;
        String stackTraceString = "";
        if (printStackTrace) {
            stackTraceString = android.util.Log.getStackTraceString(new Exception());
        }
        return android.util.Log.v(sTag, getMsg(arr) + stackTraceString);
    }

    public static int d(Object... arr) {
        if (!enabledD) return 0;
        return android.util.Log.d(sTag, getMsg(arr));
    }

    public static int i(Object... arr) {
        if (!enabledI) return 0;
        return android.util.Log.i(sTag, getMsg(arr));
    }

    public static int w(Object... arr) {
        if (!enabledW) return 0;
        return android.util.Log.w(sTag, getMsg(arr));
    }

    public static int e(Object... arr) {
        //if (!sEnabled) return 0;
        if (!enabledE) return 0;
        return android.util.Log.e(sTag, getMsg(arr));
    }

    /*
    public static int wtf(Object... arr) {
        //if (!sEnabled) return 0;
        return android.util.Log.wtf(sTag, getMsg(arr));
    }
    */
}

