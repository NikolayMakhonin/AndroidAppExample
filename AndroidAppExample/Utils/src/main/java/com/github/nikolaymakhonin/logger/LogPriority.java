package com.github.nikolaymakhonin.logger;

public class LogPriority {
    public static final int Verbose = 1;
    public static final int Debug = 2;
    public static final int Info = 3;
    public static final int Warn = 4;
    public static final int Error = 5;
    public static final int Assert = 6;

    public static String toString(int priority) {
        switch (priority) {
            case Verbose:
                return "Verbose";
            case Debug:
                return "Debug";
            case Info:
                return "Info";
            case Warn:
                return "Warn";
            case Error:
                return "Error";
            case Assert:
                return "Assert";
            default:
                return Integer.toString(priority);
        }
    }
}
