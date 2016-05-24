package com.github.nikolaymakhonin.utils.time;

import com.github.nikolaymakhonin.utils.CompareUtils;

public class TimeSpan implements Comparable<TimeSpan> {
    public final long Ticks;
    public static final long TicksPerMillisecond = 10000L;
    public static final long TicksPerSecond = TicksPerMillisecond * 1000L;
    public static final long TicksPerMinute = TicksPerSecond * 60L;
    public static final long TicksPerHour = TicksPerMinute * 60L;
    public static final long TicksPerDay = TicksPerHour * 24L;
    public static final long TicksPerWeek = TicksPerDay * 7L;
    public static final long TicksPerMonth = TicksPerDay * 30L;
    public static final long TicksPerYear = TicksPerMonth * 12L;

    public TimeSpan() {
        Ticks = 0;
    }
    
    public TimeSpan(final long ticks) {
        Ticks = ticks;
    }
    
    public TimeSpan(final int hours, final int minutes, final int seconds) {
        this(0, hours, minutes, seconds, 0);
    }
    
    public TimeSpan(final int days, final int hours, final int minutes, final int seconds) {
        this(days, hours, minutes, seconds, 0);
    }
    
    public TimeSpan(final int days, final int hours, final int minutes, final int seconds, final int milliseconds) {
        final long num = ((((((days * 0xe10L) * 0x18L) + (hours * 0xe10L)) + (minutes * 60L)) + seconds) * 0x3e8L) + milliseconds;
        if ((num > 0x346dc5d638865L) || (num < -922337203685477L))
        {
            throw new IndexOutOfBoundsException("Overflow_TimeSpanTooLong");
        }
        Ticks = num * 0x2710L;
    }
    
    public double TotalMilliseconds() {
        return Ticks / (double)TicksPerMillisecond;
    }
    
    public double TotalSeconds() {
        return Ticks / 1000.0 / TicksPerMillisecond;
    }
    
    public double TotalMinutes() {
        return (Ticks / (double)TicksPerMinute);
    }
    
    public double TotalHours() {
        return (Ticks / (double)TicksPerHour);
    }
    
    public double TotalDays() {
        return (Ticks / (double)TicksPerDay);
    }
    
    public double TotalWeeks() {
        return (Ticks / (double)TicksPerWeek);
    }
    
    public double TotalMonths() {
        return (Ticks / (double)TicksPerMonth);
    }
    
    public double TotalYears() {
        return (Ticks / (double)TicksPerYear);
    }

    public static TimeSpan FromWeeks(final double weeks) {
        return new TimeSpan((long)(weeks * 7 * 24 * 60 * 60 * 1000 * TicksPerMillisecond));
    }

    public static TimeSpan FromDays(final double days) {
        return new TimeSpan((long)(days * 24 * 60 * 60 * 1000 * TicksPerMillisecond));
    }

    public static TimeSpan FromHours(final double hours) {
        return new TimeSpan((long)(hours * 60 * 60 * 1000 * TicksPerMillisecond));
    }

    public static TimeSpan FromMinutes(final double minutes) {
        return new TimeSpan((long)(minutes * 60 * 1000 * TicksPerMillisecond));
    }

    public static TimeSpan FromSeconds(final double seconds) {
        return new TimeSpan((long)(seconds * 1000 * TicksPerMillisecond));
    }

    public static TimeSpan FromMilliseconds(final double milliseconds) {
        return new TimeSpan((long)(milliseconds * TicksPerMillisecond));
    }
    
    public TimeSpan Add(final long ticks) {
        return new TimeSpan(Ticks + ticks);
    }
    
    public TimeSpan Add(final TimeSpan timeSpan) {
        return new TimeSpan(Ticks + timeSpan.Ticks);
    }
    
    public int Tick() {
        return (int)(Ticks % TicksPerMillisecond);
    }
    
    public int Millisecond() {
        return (int)((Ticks / TicksPerMillisecond) % 1000L);
    }
    
    public int Second() {
        return (int)((Ticks / TicksPerSecond) % 60L);
    }
    
    public int Minute() {
        return (int)((Ticks / TicksPerMinute) % 60L);
    }
    
    public int Hour() {
        return (int)((Ticks / TicksPerHour) % 24L);
    }
    
    public int DayOfMonth() {
        return (int)((Ticks / TicksPerDay) % 30L);
    }
    
    public int Day() {
        return (int)(Ticks / TicksPerDay);
    }
    
    public int DayOfWeek() {
        return (int)((Ticks / TicksPerDay) % 7L);
    }
    
    public int Week() {
        return (int)(Ticks / TicksPerWeek);
    }
    
    public int Month() {
        return (int)((Ticks / TicksPerMonth) % 12L);
    }
    
    public int Year() {
        return (int)(Ticks / TicksPerYear);
    }
    
    public static final TimeSpan MinValue = new TimeSpan(-9223372036854775808L);
    public static final TimeSpan Zero = new TimeSpan(0L);
    public static final TimeSpan MaxValue = new TimeSpan(9223372036854775807L);

    @Override
    public int compareTo(final TimeSpan timeSpan) {      
        return CompareUtils.Compare(Ticks, timeSpan.Ticks);
    }
    
    @Override
    public int hashCode() {
        //noinspection PointlessBitwiseExpression
        return (int)((Ticks ^ (Ticks >>> 32)) & 0xFFFFFFFF);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof TimeSpan)) {
            return false;
        }
        return Ticks == ((TimeSpan)o).Ticks;
    }
    
    @Override
    public String toString() {
        return Long.toString(Ticks / (TicksPerMillisecond * 1000));
    }
}