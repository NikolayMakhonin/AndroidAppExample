package com.github.nikolaymakhonin.android_app_example.utils;

public class CompareUtils {

    /**
     * Null-safe equivalent of {@code a.equals(b)}.
     */
    public static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}
