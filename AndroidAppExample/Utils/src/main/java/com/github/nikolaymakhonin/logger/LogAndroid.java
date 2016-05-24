package com.github.nikolaymakhonin.logger;

public class LogAndroid implements ILog {   
    @Override
    public int v(final String tag, final String msg) {
        android.util.Log.v(tag, msg);
        return 0;
    }
    
    @Override
    public int v(final String tag, final String msg, final Throwable tr) {
        android.util.Log.v(tag, msg, tr);
        return 0;
    }
    
    @Override
    public int d(final String tag, final String msg) {
        android.util.Log.d(tag, msg);
        return 0;
    }
    
    @Override
    public int d(final String tag, final String msg, final Throwable tr) {
        android.util.Log.d(tag, msg, tr);
        return 0;
    }
    
    @Override
    public int i(final String tag, final String msg) {
        android.util.Log.i(tag, msg);
        return 0;
    }
    
    @Override
    public int i(final String tag, final String msg, final Throwable tr) {
        android.util.Log.i(tag, msg, tr);
        return 0;
    }
    
    @Override
    public int w(final String tag, final String msg) {
        android.util.Log.w(tag, msg);
        return 0;
    }
    
    @Override
    public int w(final String tag, final String msg, final Throwable tr) {
        android.util.Log.w(tag, msg, tr);
        return 0;
    }
    
    @Override
    public int w(final String tag, final Throwable tr) {
        android.util.Log.w(tag, tr);
        return 0;
    }
    
    @Override
    public int e(final String tag, final String msg) {
        android.util.Log.e(tag, msg);
        return 0;
    }
    
    @Override
    public int e(final String tag, final String msg, final Throwable tr) {
        android.util.Log.e(tag, msg, tr);
        return 0;
    }
    
    @Override
    public int wtf(final String tag, final String msg) {
        android.util.Log.wtf(tag, msg);
        return 0;
    }
    
    @Override
    public int wtf(final String tag, final Throwable tr) {
        android.util.Log.wtf(tag, tr);
        return 0;
    }
    
    @Override
    public int wtf(final String tag, final String msg, final Throwable tr) {
        android.util.Log.wtf(tag, msg, tr);
        return 0;
    }
    
    @Override
    public String getStackTraceString(final Throwable tr) {
        android.util.Log.getStackTraceString(tr);
        return "";
    }
    
    @Override
    public int println(final int priority, final String tag, final String msg) {
        android.util.Log.println(priority, tag, msg);
        return 0;
    }
}
