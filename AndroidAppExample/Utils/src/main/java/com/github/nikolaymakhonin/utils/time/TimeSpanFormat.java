package com.github.nikolaymakhonin.utils.time;

import com.github.nikolaymakhonin.logger.Log;

public class TimeSpanFormat {
    private final TimeSpanPart[] _parts;
    private final String[] _patterns;
    private final boolean _skipZeroValues;

    public TimeSpanFormat(final String[] patterns, final TimeSpanPart[] parts)
    {
        this(patterns, parts, true);
    }

    public TimeSpanFormat(final String[] patterns, final TimeSpanPart[] parts, boolean skipZeroValues) {
        _parts = parts;
        _patterns = patterns;
        _skipZeroValues = skipZeroValues;
    }

    public String format(TimeSpan timeSpan) {
        final boolean negative = timeSpan.Ticks < 0;
        timeSpan = new TimeSpan(Math.abs(timeSpan.Ticks));
        final int countValues = _parts.length;
        final Object[] values = new Object[countValues];
        for (int i = 0; i < countValues; i++) {
            final TimeSpanPart part = _parts[i];
            switch(part) {
                case Year: 
                    values[i] = timeSpan.TotalYears() >= 1 ? timeSpan.Year() : null;
                    break;
                case Month: 
                    values[i] = timeSpan.TotalMonths() >= 1 ? timeSpan.Month() : null;
                    break;
                case Week: 
                    values[i] = timeSpan.TotalWeeks() >= 1 ? timeSpan.Week() : null;
                    break;
                case Day: 
                    values[i] = timeSpan.TotalDays() >= 1 ? timeSpan.Day() : null;
                    break;
                case DayOfMonth: 
                    values[i] = timeSpan.TotalDays() >= 1 ? timeSpan.DayOfMonth() : null;
                    break;
                case DayOfWeek: 
                    values[i] = timeSpan.TotalDays() >= 1 ? timeSpan.DayOfWeek() : null;
                    break;
                case Hour: 
                    values[i] = timeSpan.TotalHours() >= 1 ? timeSpan.Hour() : null;
                    break;
                case Minute: 
                    values[i] = timeSpan.TotalMinutes() >= 1 ? timeSpan.Minute() : null;
                    break;
                case Second: 
                    values[i] = timeSpan.TotalSeconds() >= 1 ? timeSpan.Second() : null;
                    break;
                case Millisecond: 
                    values[i] = timeSpan.TotalMilliseconds() >= 1 ? timeSpan.Millisecond() : null;
                    break;
                case Tick: 
                    values[i] = timeSpan.Tick();
                    break;
                default:
                    Log.e("TimeSpanFormat", "format, unknown part: " + part);
                    break;
            }
        }

        final StringBuilder sb = new StringBuilder();
        if (negative) {
            sb.append('-');
        }
        final int count = _patterns.length;
        int notNullCount = 0;
        for (int i = 0; i < count; i++) {
            final Object value = values[i];
            if (!_skipZeroValues || value != null) {
                final String pattern = _patterns[i];
                sb.append(String.format(pattern, value == null ? 0 : value));
                notNullCount++;
            }
        }
        if (notNullCount == 0) {
            sb.append('0');
        }
        
        return sb.toString();
    }
}
