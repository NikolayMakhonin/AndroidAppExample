package com.github.nikolaymakhonin.utils.threads;

import android.os.Looper;

import com.github.nikolaymakhonin.utils.CompareUtils;

import java.lang.Thread.State;

public class ThreadUtils {
    public static boolean Join(final Thread _thread, final int timeOut) {
        //noinspection EmptyCatchBlock
        try {
            if (timeOut > 0) {
                _thread.join(timeOut);
            }
        } catch (final InterruptedException e) {}
        return _thread.getState() == State.TERMINATED;
    }

    public static String stackTraceToString(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append(element);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static boolean isMainThread() {
        return CompareUtils.EqualsObjects(Looper.getMainLooper().getThread(), Thread.currentThread());
    }

    public static Thread newThread(Runnable action, String threadName) {
        Thread thread = new Thread(action, threadName);
        return thread;
    }
}
