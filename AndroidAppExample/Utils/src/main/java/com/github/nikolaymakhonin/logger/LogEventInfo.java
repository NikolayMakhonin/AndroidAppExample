package com.github.nikolaymakhonin.logger;

public class LogEventInfo {
    /** @see LogPriority */
    public final int priority;
    public final String tag;
    public final String message;
    public final Throwable exception;
    public final boolean terminateApplication;

    public LogEventInfo(int priority, String tag, String message, Throwable exception, boolean terminateApplication) {
        this.priority = priority;
        this.tag = tag;
        this.message = message;
        this.exception = exception;
        this.terminateApplication = terminateApplication;
    }
}
