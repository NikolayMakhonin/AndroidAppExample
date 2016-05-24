package com.github.nikolaymakhonin.utils.time;

import com.github.nikolaymakhonin.utils.CompareUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTime implements Comparable<DateTime> {
    private static final long _baseStartTicks = 621357696000000000L;//new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).Ticks + 2 days
    private static long _startTicks;
    private static final long _ticksPerMilliseconds = 10000L;
    public final long Ticks;

    static {
        _startTicks = calcStartTicks();
    }
    
    private static long calcStartTicks() {
        final long milliTime0 = System.currentTimeMillis();
        do {
            final long milliTime = System.currentTimeMillis(); 
            final long nanoTime = System.nanoTime(); 
            if (milliTime != milliTime0) {
                return calcStartTicks(milliTime, nanoTime);
            }
        } while(true);        
    }
    
    private static long calcStartTicks(final long milliTime, final long nanoTime) {
        return milliTime * _ticksPerMilliseconds - nanoTime / 100;
    }
    
    public DateTime() {
        final long milliTime = System.currentTimeMillis();
        final long nanoTime = System.nanoTime();
        if (Math.abs(calcStartTicks(milliTime, nanoTime) - _startTicks) > 100 * _ticksPerMilliseconds) {
            _startTicks = calcStartTicks();
        }
        Ticks = _baseStartTicks + _startTicks + nanoTime / 100;
    }
    
    public DateTime(final long ticks) {
        Ticks = ticks;
    }
    
    public DateTime(final Date date) {
        Ticks = date.getTime() * _ticksPerMilliseconds + _baseStartTicks;
    }
    
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    
    public DateTime(final int year, final int month, final int day) {
        final Calendar calendar = Calendar.getInstance(UTC);
        calendar.set(year, month - 1, day, 0, 0, 0);
        long milliseconds = calendar.getTimeInMillis();
        if (milliseconds % 1000 != 0) {
            milliseconds = (milliseconds / 1000) * 1000;
            if (milliseconds < 0) {
                milliseconds -= 1000;
            }
        }
        Ticks = milliseconds * _ticksPerMilliseconds + _baseStartTicks;
    }
    
    public double TotalMilliseconds() {
        return Ticks / (double)_ticksPerMilliseconds;
    }
    
    public double TotalSeconds() {
        return Ticks / 1000.0 / _ticksPerMilliseconds;
    }
    
    public static DateTime FromSeconds(final double seconds) {
        return new DateTime((long)(seconds * 1000 * _ticksPerMilliseconds));
    }
    
    public static DateTime FromDays(final double days) {
        return new DateTime((long)(days * 24 * 3600 * 1000 * _ticksPerMilliseconds));
    }
    
    public static DateTime FromMilliseconds(final double milliseconds) {
        return new DateTime((long)(milliseconds * _ticksPerMilliseconds));
    }

    public Date toDate() {
        return new Date((Ticks - _baseStartTicks)  / _ticksPerMilliseconds);
    }

    public static Date toDate(DateTime dateTime) {
        return dateTime == null ? null : dateTime.toDate();
    }

    public DateTime AddTicks(final long ticks) {
        return new DateTime(Ticks + ticks);
    }
    
    public DateTime Add(final TimeSpan timeSpan) {
        return new DateTime(Ticks + timeSpan.Ticks);
    }
    
    public static final DateTime MinValue = new DateTime(0L);
    public static final DateTime MaxValue = new DateTime(3155378975999999999L);

    @Override
    public int compareTo(final DateTime dateTime) {      
        return CompareUtils.Compare(Ticks, dateTime.Ticks);
    }
    
    @Override
    public int hashCode() {
        //noinspection PointlessBitwiseExpression
        return (int)((Ticks ^ (Ticks >>> 32)) & 0xFFFFFFFF);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DateTime)) {
            return false;
        }
        return Ticks == ((DateTime)o).Ticks;
    }
    
    @Override
    public String toString() {
        return toDate().toString();
    }
}