package com.github.nikolaymakhonin.utils;

import java.lang.reflect.Array;

public class ArrayUtilsExt {

    public static <T> T[] copyOf(final T[] original, final int allocateLength, final int usageLength) {
        return (T[]) copyOf(original, allocateLength, usageLength, original.getClass());
    }

    public static <T, U> T[] copyOf(final U[] original, final int allocateLength, final int usageLength, final Class<? extends T[]> newItemType) {
        final T[] copy = createArray(newItemType, allocateLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, usageLength));
        return copy;
    }

    public static <T> T[] createArray(Class<? extends T[]> itemType, int allocateLength) {
        T[] array = (itemType == Object[].class)
            ? (T[]) new Object[allocateLength]
            : (T[]) Array.newInstance(itemType.getComponentType(), allocateLength);

        return array;
    }
}
