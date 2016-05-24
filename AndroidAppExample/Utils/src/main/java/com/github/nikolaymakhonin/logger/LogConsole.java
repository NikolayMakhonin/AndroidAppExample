package com.github.nikolaymakhonin.logger;

public class LogConsole implements ILog {   
    @Override
    public int v(String tag, String msg) {
        log("Log.v", tag, msg);
        return 0;
    }
    
    @Override
    public int v(String tag, String msg, Throwable tr) {
        log("Log.v", tag, msg, tr);
        return 0;
    }
    
    @Override
    public int d(String tag, String msg) {
        log("Log.d", tag, msg);
        return 0;
    }
    
    @Override
    public int d(String tag, String msg, Throwable tr) {
        log("Log.d", tag, msg, tr);
        return 0;
    }
    
    @Override
    public int i(String tag, String msg) {
        log("Log.i", tag, msg);
        return 0;
    }
    
    @Override
    public int i(String tag, String msg, Throwable tr) {
        log("Log.i", tag, msg, tr);
        return 0;
    }
    
    @Override
    public int w(String tag, String msg) {
        log("Log.w", tag, msg);
        return 0;
    }
    
    @Override
    public int w(String tag, String msg, Throwable tr) {
        log("Log.w", tag, msg, tr);
        return 0;
    }
    
    @Override
    public int w(String tag, Throwable tr) {
        log("Log.w", tag, tr);
        return 0;
    }
    
    @Override
    public int e(String tag, String msg) {
        log("Log.e", tag, msg);
        return 0;
    }
    
    @Override
    public int e(String tag, String msg, Throwable tr) {
        log("Log.e", tag, msg, tr);
        return 0;
    }
    
    @Override
    public int wtf(String tag, String msg) {
        log("Log.wtf", tag, msg);
        return 0;
    }
    
    @Override
    public int wtf(String tag, Throwable tr) {
        log("Log.wtf", tag, tr);
        return 0;
    }
    
    @Override
    public int wtf(String tag, String msg, Throwable tr) {
        log("Log.wtf", tag, msg, tr);
        return 0;
    }
    
    @Override
    public String getStackTraceString(Throwable tr) {
        log("Log.getStackTraceString", tr);
        return "";
    }
    
    @Override
    public int println(int priority, String tag, String msg) {
        log("Log.println", priority, tag, msg);
        return 0;
    }
    
    private void log(String funcName, Object... objects) {
        System.err.print("------------ ");
        System.err.print(funcName);
        System.err.println(" ------------");
        for (Object object : objects) {
            if (object instanceof Throwable) {
                ((Throwable)object).printStackTrace(System.err);
            } else {
                System.err.println(object);
            }
        }
    }
}
