package com.github.nikolaymakhonin.utils;

import java.lang.reflect.Array;
import java.util.Comparator;

@SuppressWarnings({ "JavadocReference", "SuspiciousSystemArraycopy", "RedundantCast" })
public class ArrayUtils {
    public static int indexOf(final char[] array, final char item) {
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (array[i] == item) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(final T[] array, final T item) {
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (CompareUtils.EqualsObjects(array[i], item)) {
                return i;
            }
        }
        return -1;
    }

    // http://www.cs.cmu.edu/afs/cs.cmu.edu/project/listen/StoryCode/stanford-parser-2008-10-26/src/edu/stanford/nlp/util/ArrayUtils.java
    public static float[] toPrimitive(final Float[] in) {
        return toPrimitive(in, 0f);
    }
    
    public static double[] toPrimitive(final Double[] in) {
        return toPrimitive(in, 0d);
    }
    
    public static long[] toPrimitive(final Long[] in) {
        return toPrimitive(in, 0L);
    }
    
    public static int[] toPrimitive(final Integer[] in) {
        return toPrimitive(in, 0);
    }
    
    public static short[] toPrimitive(final Short[] in) {
        return toPrimitive(in, (short) 0);
    }
    
    public static char[] toPrimitive(final Character[] in) {
        return toPrimitive(in, (char) 0);
    }
    
    public static float[] toPrimitive(final Float[] in, final float valueForNull) {
        if (in == null) { return null; }
        final float[] out = new float[in.length];
        for (int i = 0; i < in.length; i++) {
            final Float b = in[i];
            out[i] = (b == null ? valueForNull : b);
        }
        return out;
    }
    
    public static double[] toPrimitive(final Double[] in, final double valueForNull) {
        if (in == null) { return null; }
        final double[] out = new double[in.length];
        for (int i = 0; i < in.length; i++) {
            final Double b = in[i];
            out[i] = (b == null ? valueForNull : b);
        }
        return out;
    }
    
    public static long[] toPrimitive(final Long[] in, final long valueForNull) {
        if (in == null) { return null; }
        final long[] out = new long[in.length];
        for (int i = 0; i < in.length; i++) {
            final Long b = in[i];
            out[i] = (b == null ? valueForNull : b);
        }
        return out;
    }
    
    public static int[] toPrimitive(final Integer[] in, final int valueForNull) {
        if (in == null) { return null; }
        final int[] out = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            final Integer b = in[i];
            out[i] = (b == null ? valueForNull : b);
        }
        return out;
    }
    
    public static short[] toPrimitive(final Short[] in, final short valueForNull) {
        if (in == null) { return null; }
        final short[] out = new short[in.length];
        for (int i = 0; i < in.length; i++) {
            final Short b = in[i];
            out[i] = (b == null ? valueForNull : b);
        }
        return out;
    }
    
    public static char[] toPrimitive(final Character[] in, final char valueForNull) {
        if (in == null) { return null; }
        final char[] out = new char[in.length];
        for (int i = 0; i < in.length; i++) {
            final Character b = in[i];
            out[i] = (b == null ? valueForNull : b);
        }
        return out;
    }
        
    public static <T> T[] copyOf(final T[] original, final int allocateLength, final int usageLength) {
        return (T[]) copyOf(original, allocateLength, usageLength, original.getClass());
    }

    public static <T, U> T[] copyOf(final U[] original, final int allocateLength, final int usageLength, final Class<? extends T[]> newType) {
        final T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[allocateLength] : (T[]) Array
            .newInstance(newType.getComponentType(), allocateLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, usageLength));
        return copy;
    }

    /** Copies the specified array, truncating or padding with nulls (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length is greater than that of the original array. The
     * resulting array is of exactly the same class as the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static <T> T[] copyOf(final T[] original, final int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }
    
    /** Copies the specified array, truncating or padding with nulls (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length is greater than that of the original array. The
     * resulting array is of the class <tt>newType</tt>.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @param newType
     *            the class of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @throws ArrayStoreException
     *             if an element copied from <tt>original</tt> is not of a runtime type that can be stored in an array
     *             of class <tt>newType</tt>
     * @since 1.6 */
    public static <T, U> T[] copyOf(final U[] original, final int newLength, final Class<? extends T[]> newType) {
        final T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[newLength] : (T[]) Array
            .newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with nulls (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length is greater than that of the original array. The
     * resulting array is of the class <tt>newType</tt>.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @param newType
     *            the class of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @throws ArrayStoreException
     *             if an element copied from <tt>original</tt> is not of a runtime type that can be stored in an array
     *             of class <tt>newType</tt>
     * @since 1.6 */
    public static Object[] copyOf(final long[] original, final int newLength, final Class<? extends Object[]> newType) {
        final Object[] copy = (newType == Object[].class) ? (Object[]) new Object[newLength] : (Object[]) Array
            .newInstance(newType.getComponentType(), newLength);
        int i = 0;
        for (final long value : original) {
            copy[i++] = value;
        }
        return copy;
    }
    
    public static Object[] copyOf(final int[] original, final int newLength, final Class<? extends Object[]> newType) {
        final Object[] copy = (newType == Object[].class) ? (Object[]) new Object[newLength] : (Object[]) Array
            .newInstance(newType.getComponentType(), newLength);
        int i = 0;
        for (final long value : original) {
            copy[i++] = value;
        }
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>(byte)0</tt>.
     * Such indices will exist if and only if the specified length is greater than that of the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static byte[] copyOf(final byte[] original, final int newLength) {
        final byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>(short)0</tt>.
     * Such indices will exist if and only if the specified length is greater than that of the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static short[] copyOf(final short[] original, final int newLength) {
        final short[] copy = new short[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>0</tt>. Such
     * indices will exist if and only if the specified length is greater than that of the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static int[] copyOf(final int[] original, final int newLength) {
        final int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>0L</tt>. Such
     * indices will exist if and only if the specified length is greater than that of the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static long[] copyOf(final long[] original, final int newLength) {
        final long[] copy = new long[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with null characters (if necessary) so the copy has the
     * specified length. For all indices that are valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but not the original, the copy will contain
     * <tt>'\\u000'</tt>. Such indices will exist if and only if the specified length is greater than that of the
     * original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with null characters to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static char[] copyOf(final char[] original, final int newLength) {
        final char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>0f</tt>. Such
     * indices will exist if and only if the specified length is greater than that of the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static float[] copyOf(final float[] original, final int newLength) {
        final float[] copy = new float[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
     * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
     * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>0d</tt>. Such
     * indices will exist if and only if the specified length is greater than that of the original array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static double[] copyOf(final double[] original, final int newLength) {
        final double[] copy = new double[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }
    
    /** Copies the specified array, truncating or padding with <tt>false</tt> (if necessary) so the copy has the
     * specified length. For all indices that are valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but not the original, the copy will contain
     * <tt>false</tt>. Such indices will exist if and only if the specified length is greater than that of the original
     * array.
     * 
     * @param original
     *            the array to be copied
     * @param newLength
     *            the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with false elements to obtain the specified length
     * @throws NegativeArraySizeException
     *             if <tt>newLength</tt> is negative
     * @throws NullPointerException
     *             if <tt>original</tt> is null
     * @since 1.6 */
    public static boolean[] copyOf(final boolean[] original, final int newLength) {
        final boolean[] copy = new boolean[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code false}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static boolean[] copyOfRange(boolean[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        boolean[] result = new boolean[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code (byte) 0}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static byte[] copyOfRange(byte[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        byte[] result = new byte[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code '\\u0000'}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static char[] copyOfRange(char[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        char[] result = new char[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code 0.0d}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static double[] copyOfRange(double[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        double[] result = new double[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code 0.0f}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static float[] copyOfRange(float[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        float[] result = new float[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code 0}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static int[] copyOfRange(int[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        int[] result = new int[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code 0L}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static long[] copyOfRange(long[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        long[] result = new long[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code (short) 0}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    public static short[] copyOfRange(short[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        short[] result = new short[resultLength];
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code null}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @since 1.6
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyOfRange(T[] original, int start, int end) {
        int originalLength = original.length; // For exception priority compatibility.
        if (start > end) {
            throw new IllegalArgumentException();
        }
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        T[] result = (T[]) Array.newInstance(original.getClass().getComponentType(), resultLength);
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /**
     * Copies elements from {@code original} into a new array, from indexes start (inclusive) to
     * end (exclusive). The original order of elements is preserved.
     * If {@code end} is greater than {@code original.length}, the result is padded
     * with the value {@code null}.
     *
     * @param original the original array
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the new array
     * @throws ArrayIndexOutOfBoundsException if {@code start < 0 || start > original.length}
     * @throws IllegalArgumentException if {@code start > end}
     * @throws NullPointerException if {@code original == null}
     * @throws ArrayStoreException if a value in {@code original} is incompatible with T
     * @since 1.6
     */
    @SuppressWarnings("unchecked")
    public static <T, U> T[] copyOfRange(U[] original, int start, int end, Class<? extends T[]> newType) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int originalLength = original.length;
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int resultLength = end - start;
        int copyLength = Math.min(resultLength, originalLength - start);
        T[] result = (T[]) Array.newInstance(newType.getComponentType(), resultLength);
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    /** Checks that {@code fromIndex} and {@code toIndex} are in the range and throws an appropriate exception, if they
     * aren't. */
    private static void rangeCheck(final int length, final int fromIndex, final int toIndex) {
        if (fromIndex > toIndex) { throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex("
            + toIndex + ")"); }
        if (fromIndex < 0) { throw new ArrayIndexOutOfBoundsException(fromIndex); }
        if (toIndex > length) { throw new ArrayIndexOutOfBoundsException(toIndex); }
    }
    
    /** Searches the specified array of longs for the specified value using the binary search algorithm. The array must
     * be sorted (as by the {@link #sort(long[])} method) prior to making this call. If it is not sorted, the results
     * are undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
     * will be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final long[] a, final long key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of longs for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(long[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final long[] a, final int fromIndex, final int toIndex, final long key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final long[] a, final int fromIndex, final int toIndex, final long key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final long midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array of ints for the specified value using the binary search algorithm. The array must be
     * sorted (as by the {@link #sort(int[])} method) prior to making this call. If it is not sorted, the results are
     * undefined. If the array contains multiple elements with the specified value, there is no guarantee which one will
     * be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final int[] a, final int key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of ints for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(int[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final int[] a, final int fromIndex, final int toIndex, final int key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final int[] a, final int fromIndex, final int toIndex, final int key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array of shorts for the specified value using the binary search algorithm. The array must
     * be sorted (as by the {@link #sort(short[])} method) prior to making this call. If it is not sorted, the results
     * are undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
     * will be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final short[] a, final short key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of shorts for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(short[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final short[] a, final int fromIndex, final int toIndex, final short key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final short[] a, final int fromIndex, final int toIndex, final short key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final short midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array of chars for the specified value using the binary search algorithm. The array must
     * be sorted (as by the {@link #sort(char[])} method) prior to making this call. If it is not sorted, the results
     * are undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
     * will be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final char[] a, final char key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of chars for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(char[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final char[] a, final int fromIndex, final int toIndex, final char key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final char[] a, final int fromIndex, final int toIndex, final char key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final char midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array of bytes for the specified value using the binary search algorithm. The array must
     * be sorted (as by the {@link #sort(byte[])} method) prior to making this call. If it is not sorted, the results
     * are undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
     * will be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final byte[] a, final byte key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of bytes for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(byte[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final byte[] a, final int fromIndex, final int toIndex, final byte key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final byte[] a, final int fromIndex, final int toIndex, final byte key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final byte midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array of doubles for the specified value using the binary search algorithm. The array must
     * be sorted (as by the {@link #sort(double[])} method) prior to making this call. If it is not sorted, the results
     * are undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
     * will be found. This method considers all NaN values to be equivalent and equal.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final double[] a, final double key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of doubles for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(double[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found. This method considers all NaN values to be equivalent and equal.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final double[] a, final int fromIndex, final int toIndex, final double key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final double[] a, final int fromIndex, final int toIndex, final double key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final double midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1; // Neither val is NaN, thisVal is smaller
            } else if (midVal > key) {
                high = mid - 1; // Neither val is NaN, thisVal is larger
            } else {
                final long midBits = Double.doubleToLongBits(midVal);
                final long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits) {
                    return mid; // Key found
                } else if (midBits < keyBits) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array of floats for the specified value using the binary search algorithm. The array must
     * be sorted (as by the {@link #sort(float[])} method) prior to making this call. If it is not sorted, the results
     * are undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
     * will be found. This method considers all NaN values to be equivalent and equal.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found. */
    public static int binarySearch(final float[] a, final float key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array of floats for the specified value using the binary search algorithm. The
     * range must be sorted (as by the {@link #sort(float[], int, int)} method) prior to making this call. If it is not
     * sorted, the results are undefined. If the range contains multiple elements with the specified value, there is no
     * guarantee which one will be found. This method considers all NaN values to be equivalent and equal.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final float[] a, final int fromIndex, final int toIndex, final float key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final float[] a, final int fromIndex, final int toIndex, final float key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final float midVal = a[mid];
            
            if (midVal < key) {
                low = mid + 1; // Neither val is NaN, thisVal is smaller
            } else if (midVal > key) {
                high = mid - 1; // Neither val is NaN, thisVal is larger
            } else {
                final int midBits = Float.floatToIntBits(midVal);
                final int keyBits = Float.floatToIntBits(key);
                if (midBits == keyBits) {
                    return mid; // Key found
                } else if (midBits < keyBits) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array for the specified object using the binary search algorithm. The array must be sorted
     * into ascending order according to the {@linkplain Comparable natural ordering} of its elements (as by the
     * {@link #sort(Object[])} method) prior to making this call. If it is not sorted, the results are undefined. (If
     * the array contains elements that are not mutually comparable (for example, strings and integers), it
     * <i>cannot</i> be sorted according to the natural ordering of its elements, hence results are undefined.) If the
     * array contains multiple elements equal to the specified object, there is no guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found.
     * @throws ClassCastException
     *             if the search key is not comparable to the elements of the array. */
    public static int binarySearch(final Object[] a, final Object key) {
        return binarySearch0(a, 0, a.length, key);
    }
    
    /** Searches a range of the specified array for the specified object using the binary search algorithm. The range
     * must be sorted into ascending order according to the {@linkplain Comparable natural ordering} of its elements (as
     * by the {@link #sort(Object[], int, int)} method) prior to making this call. If it is not sorted, the results are
     * undefined. (If the range contains elements that are not mutually comparable (for example, strings and integers),
     * it <i>cannot</i> be sorted according to the natural ordering of its elements, hence results are undefined.) If
     * the range contains multiple elements equal to the specified object, there is no guarantee which one will be
     * found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws ClassCastException
     *             if the search key is not comparable to the elements of the array within the specified range.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static int binarySearch(final Object[] a, final int fromIndex, final int toIndex, final Object key) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key);
    }
    
    // Like public version, but without range checks.
    private static int binarySearch0(final Object[] a, final int fromIndex, final int toIndex, final Object key) {
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final Comparable midVal = (Comparable) a[mid];
            final int cmp = midVal.compareTo(key);
            
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
    
    /** Searches the specified array for the specified object using the binary search algorithm. The array must be sorted
     * into ascending order according to the specified comparator (as by the {@link #sort(Object[], Comparator)
     * sort(T[], Comparator)} method) prior to making this call. If it is not sorted, the results are undefined. If the
     * array contains multiple elements equal to the specified object, there is no guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param key
     *            the value to be searched for
     * @param c
     *            the comparator by which the array is ordered. A <tt>null</tt> value indicates that the elements'
     *            {@linkplain Comparable natural ordering} should be used.
     * @return index of the search key, if it is contained in the array; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element greater than the key, or
     *         <tt>a.length</tt> if all elements in the array are less than the specified key. Note that this guarantees
     *         that the return value will be &gt;= 0 if and only if the key is found.
     * @throws ClassCastException
     *             if the array contains elements that are not <i>mutually comparable</i> using the specified
     *             comparator, or the search key is not comparable to the elements of the array using this comparator. */
    public static <T> int binarySearch(final T[] a, final T key, final Comparator<? super T> c) {
        return binarySearch0(a, 0, a.length, key, c);
    }
    
    /** Searches a range of the specified array for the specified object using the binary search algorithm. The range
     * must be sorted into ascending order according to the specified comparator (as by the
     * {@link #sort(Object[], int, int, Comparator) sort(T[], int, int, Comparator)} method) prior to making this call.
     * If it is not sorted, the results are undefined. If the range contains multiple elements equal to the specified
     * object, there is no guarantee which one will be found.
     * 
     * @param a
     *            the array to be searched
     * @param fromIndex
     *            the index of the first element (inclusive) to be searched
     * @param toIndex
     *            the index of the last element (exclusive) to be searched
     * @param key
     *            the value to be searched for
     * @param c
     *            the comparator by which the array is ordered. A <tt>null</tt> value indicates that the elements'
     *            {@linkplain Comparable natural ordering} should be used.
     * @return index of the search key, if it is contained in the array within the specified range; otherwise,
     *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as the point at which the
     *         key would be inserted into the array: the index of the first element in the range greater than the key,
     *         or <tt>toIndex</tt> if all elements in the range are less than the specified key. Note that this
     *         guarantees that the return value will be &gt;= 0 if and only if the key is found.
     * @throws ClassCastException
     *             if the range contains elements that are not <i>mutually comparable</i> using the specified
     *             comparator, or the search key is not comparable to the elements in the range using this comparator.
     * @throws IllegalArgumentException
     *             if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException
     *             if {@code fromIndex < 0 or toIndex > a.length}
     * @since 1.6 */
    public static <T> int binarySearch(final T[] a, final int fromIndex, final int toIndex, final T key,
        final Comparator<? super T> c) {
        rangeCheck(a.length, fromIndex, toIndex);
        return binarySearch0(a, fromIndex, toIndex, key, c);
    }
    
    // Like public version, but without range checks.
    private static <T> int binarySearch0(final T[] a, final int fromIndex, final int toIndex, final T key,
        final Comparator<? super T> c) {
        if (c == null) { return binarySearch0(a, fromIndex, toIndex, key); }
        int low = fromIndex;
        int high = toIndex - 1;
        
        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final T midVal = a[mid];
            final int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }
}
