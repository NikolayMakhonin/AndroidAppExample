package com.github.nikolaymakhonin.logger;

import com.github.nikolaymakhonin.utils.threads.ThreadUtils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class Log {
    public static final  ILog    logger;
    public static final  String  tagPrefix;
    private static final boolean _debugMode;

    static {
        _debugMode = true;// DeviceUtils.isEmulator() || BuildConfig.DEBUG;
        tagPrefix = "Mika.";
        logger = new LogConsole();
//        try {
//            ClassLoader.getSystemClassLoader().loadClass("android.util.Log");
//            logger = new LogAndroid();
//        } catch (final ClassNotFoundException e) {
//            logger = new LogConsole();
//        }
    }
    
    public static int v(final String tag, final String msg) {
        return v(tag, msg, false);
    }

    public static int v(final String tag, String msg, boolean stackTrace) {
        if (_debugMode) {
            if (stackTrace) msg += "\nStackTrace:\n" + getStackTraceString();
            logger.v(tagPrefix + tag, msg);
            onLog(LogPriority.Verbose, tag, msg, null);
        }
        return 0;
    }
    
    public static int v(final String tag, final String msg, final Throwable tr) {
        if (_debugMode) {
            logger.v(tagPrefix + tag, msg, tr);
            if (tr != null) {
                tr.printStackTrace();
            }
            onLog(LogPriority.Verbose, tag, msg, tr);
        }
        return 0;
    }
    
    public static int d(final String tag, final String msg) {
        return d(tag, msg, false);
    }

    public static int d(final String tag, String msg, boolean stackTrace) {
        if (_debugMode) {
            if (stackTrace) msg += "\nStackTrace:\n" + getStackTraceString();
            logger.d(tagPrefix + tag, msg);
            onLog(LogPriority.Debug, tag, msg, null);
        }
        return 0;
    }
    
    public static int d(final String tag, final String msg, final Throwable tr) {
        if (_debugMode) {
            logger.d(tagPrefix + tag, msg, tr);
            if (tr != null) {
                tr.printStackTrace();
            }
            onLog(LogPriority.Debug, tag, msg, tr);
        }
        return 0;
    }

    public static int i(final String tag, final String msg) {
        return i(tag, msg, false);
    }

    public static int i(final String tag, String msg, boolean stackTrace) {
        if (_debugMode) {
            if (stackTrace) msg += "\nStackTrace:\n" + getStackTraceString();
            logger.i(tagPrefix + tag, msg);
            onLog(LogPriority.Info, tag, msg, null);
        }
        return 0;
    }
    
    public static int i(final String tag, final String msg, final Throwable tr) {
        if (_debugMode) {
            logger.i(tagPrefix + tag, msg, tr);
            if (tr != null) {
                tr.printStackTrace();
            }
            onLog(LogPriority.Info, tag, msg, tr);
        }
        return 0;
    }

    public static int w(final String tag, final String msg) {
        return w(tag, msg, true);
    }

    public static int w(final String tag, String msg, boolean stackTrace) {
        if (stackTrace) {
            msg += "\nStackTrace:\n" + getStackTraceString();
        }
        logger.w(tagPrefix + tag, msg);
        onLog(LogPriority.Warn, tag, msg, null);
        return 0;
    }
    
    public static int w(final String tag, final String msg, final Throwable tr) {
        logger.w(tagPrefix + tag, msg, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        onLog(LogPriority.Warn, tag, msg, tr);
        return 0;
    }
    
    public static int w(final String tag, final Throwable tr) {
        logger.w(tagPrefix + tag, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        onLog(LogPriority.Error, tag, null, tr);
        return 0;
    }

    public static int e(final String tag, final String msg) {
        return e(tag, msg, true);
    }

    public static int e(final String tag, String msg, boolean stackTrace) {
        if (stackTrace) msg += "\nStackTrace:\n" + getStackTraceString();
        logger.e(tagPrefix + tag, msg);
        onLog(LogPriority.Error, tag, msg, null);
        return 0;
    }
    
    public static int e(final String tag, final String msg, final Throwable tr) {
        logger.e(tagPrefix + tag, msg, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        onLog(LogPriority.Error, tag, msg, tr);
        return 0;
    }

    public static int wtf(final String tag, final String msg) {
        return wtf(tag, msg, true);
    }

    public static int wtf(final String tag, String msg, boolean stackTrace) {
        if (stackTrace) msg += "\nStackTrace:\n" + getStackTraceString();
        logger.wtf(tagPrefix + tag, msg);
        onLog(LogPriority.Assert, tag, msg, null);
        return 0;
    }

    public static int wtf(final String tag, final Throwable tr) {
        logger.wtf(tagPrefix + tag, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        onLog(LogPriority.Assert, tag, null, null);
        return 0;
    }
    
    public static int wtf(final String tag, final String msg, final Throwable tr) {
        logger.wtf(tagPrefix + tag, msg, tr);
        if (tr != null) {
            tr.printStackTrace();
        }
        onLog(LogPriority.Assert, tag, msg, tr);
        return 0;
    }

    public static String getStackTraceString() {
        String threadStackTrace = null;
        //noinspection EmptyCatchBlock
        try {
            Thread thread = Thread.currentThread();
            threadStackTrace = ThreadUtils.stackTraceToString(thread.getStackTrace());
        } catch (Exception e2) {

        }
        return threadStackTrace;
    }

    public static String getStackTraceString(final Throwable tr) {
        logger.getStackTraceString(tr);
        return "";
    }

    public static int println(final int priority, final String tag, final String msg) {
        logger.println(priority, tagPrefix + tag, msg);
        onLog(priority, tag, msg, null);
        return 0;
    }

    public static int e(final LogEventInfo logInfo) {
        logger.e(logInfo.tag, logInfo.message, logInfo.exception);
        if (logInfo.exception != null) {
            logInfo.exception.printStackTrace();
        }
        onLog(logInfo);
        return 0;
    }

    //region Log event

    public static void onLog(LogEventInfo logInfo) {
        _logSubject.onNext(logInfo);
    }

    public static void onLog(int priority, String tag, String msg, Throwable tr) {
        if (_logSubject.hasObservers()) {
            _logSubject.onNext(new LogEventInfo(priority, tagPrefix + tag, msg, tr, false));
        }
    }

    private static final Subject<LogEventInfo, LogEventInfo> _logSubject = PublishSubject.create();

    public static Observable<LogEventInfo> getLogObservable() {
        return _logSubject;
    }

    //endregion

    public static final Thread.UncaughtExceptionHandler uncaughtExceptionHandler
        = (thread, e) -> handleUncaughtException(thread, e);

    private static void handleUncaughtException (Thread thread, Throwable e)
    {
        String threadStackTrace = null;
        //noinspection EmptyCatchBlock
        try {
            threadStackTrace = ThreadUtils.stackTraceToString(thread.getStackTrace());
        } catch (Exception e2) {

        }

        LogEventInfo logInfo = new LogEventInfo(LogPriority.Error,
            tagPrefix + "UncaughtException",
            "Thread StackTrace: " + threadStackTrace,
            e,
            _logSubject.hasObservers() && ThreadUtils.isMainThread());

        e(logInfo);
    }
}

