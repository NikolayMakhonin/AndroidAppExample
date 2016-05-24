package com.github.nikolaymakhonin.utils;

import com.github.nikolaymakhonin.utils.time.DateTime;
import com.github.nikolaymakhonin.utils.time.TimeSpan;
import com.github.nikolaymakhonin.utils.contracts.delegates.EqualityComparator;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({ "deprecation", "Convert2Lambda", "Anonymous2MethodRef" })
public class CompareUtils {
    public static int CompareDelegate(final Object d1, final Object d2) {
        final RefParam<Integer> ref_i = new RefParam<>(0);
        if (CompareNullable(d1, d2, ref_i)) { return ref_i.value; }
        return compareDelegate(d1, d2);
    }
    
    public static int compareDelegate(final Object d1, final Object d2) {
        int i;
        if ((i = CompareIdentityHashCode(d1, d2)) != 0) { return i; }
        return 0;// CompareMethod(d1.getMethod(), d2.getMethod());
    }
    
    // public static int CompareMethod(MethodBase m1, MethodBase m2)
    // {
    // int i = 0;
    // if (CompareNullable(m1, m2, ref i)) return i;
    // return compareMethod(m1, m2);
    // }
    // private static int compareMethod(MethodBase m1, MethodBase m2)
    // {
    // int i;
    // boolean bNull;
    // if ((i = CompareTypes(m1.getDeclaringType(), m2.getDeclaringType(), out bNull)) != 0) return i;
    // if (bNull) return String.Compare(m1.getName(), m2.getName(), StringComparator.getOrdinal());
    // if (CompareNullable(m1.getMethodHandle(), m2.getMethodHandle(), ref i)) { if (i != 0) return i; }
    // else if((i = m1.getMethodHandle().hashCode().compareTo(m2.getMethodHandle().hashCode())) != 0) return i;
    // return 0;
    // }
    // public static int CompareTypes(Class t1, Class t2)
    // {
    // boolean bNull;
    // return CompareTypes(t1, t2, out bNull);
    // }
    // public static int CompareTypes(Class t1, Class t2, RefParam<Boolean$3 bNull)
    // {
    // int i = 0;
    // if (CompareNullable(t1, t2, ref i)) { bNull = true; return i; }
    // bNull = false;
    // if ((i = t1.GUID.compareTo(t2.GUID)) != 0) return i;
    // if (t1.equals(t2)) return 0;
    // if ((i = t1.getIsGenericType().compareTo(t2.getIsGenericType())) != 0) return i;
    // var gen1 = t1.GetGenericArguments();
    // var gen2 = t2.GetGenericArguments();
    // if ((i = gen1.length.compareTo(gen2.length)) != 0) return i;
    // for (int j = 0; j < gen1.length; j++)
    // {
    // if ((i = CompareTypes(gen1.get(j), gen2.get(j))) != 0) return i;
    // }
    // return 0;
    // }
    
    public static int CompareHashCode(final Object o1, final Object o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        final Class t1 = o1.getClass(); // t1 = t1.GetPublicBaseType();
        final Class t2 = o2.getClass(); // t2 = t2.GetPublicBaseType();
        int i1 = t1.hashCode();
        int i2 = t2.hashCode();
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        if (o1 instanceof Enum && o2 instanceof Enum) { return ((Enum) o1).compareTo((Enum) o2); }
        i1 = o1.hashCode();
        i2 = o2.hashCode();
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        return 0;
    }
    
    public static int CompareIdentityHashCode(final Object o1, final Object o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        final int i1 = System.identityHashCode(o1);
        final int i2 = System.identityHashCode(o2);
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        return 0;
    }
    
    public static int Compare(final int i1, final int i2) {
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        return 0;
    }
    
    public static int Compare(final long i1, final long i2) {
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        return 0;
    }
    
    public static int Compare(final double i1, final double i2) {
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        return 0;
    }
    
    public static int Compare(final Integer i1, final Integer i2) {
        //noinspection NumberEquality
        if (i1 == i2) { return 0; }
        if (i1 == null) { return 1; }
        if (i2 == null) { return -1; }
        return Compare((int) i1, (int) i2);
    }
    
    public static int Compare(final Long i1, final Long i2) {
        //noinspection NumberEquality
        if (i1 == i2) { return 0; }
        if (i1 == null) { return 1; }
        if (i2 == null) { return -1; }
        return Compare((long) i1, (long) i2);
    }
    
    public static int Compare(final Double i1, final Double i2) {
        //noinspection NumberEquality
        if (i1 == i2) { return 0; }
        if (i1 == null) { return 1; }
        if (i2 == null) { return -1; }
        return Compare((double) i1, (double) i2);
    }
    
    public static int Compare(final String s1, final String s2, final boolean ignoreCase) {
        //noinspection StringEquality
        if (s1 == s2) { return 0; }
        if (s1 == null) { return 1; }
        if (s2 == null) { return -1; }
        return ignoreCase ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
    }
    
    public static int Compare(final String s1, final String s2) {
        return Compare(s1, s2, false);
    }
    
    public static int Compare(final UUID o1, final UUID o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        return o1.compareTo(o2);
    }
    
    public static int Compare(final TimeSpan o1, final TimeSpan o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        return o1.compareTo(o2);
    }

    public static int Compare(final DateTime o1, final DateTime o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        return o1.compareTo(o2);
    }

    public static int Compare(final Date o1, final Date o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        return o1.compareTo(o2);
    }

    public static boolean Equals(final String o1, final String o2, final boolean ignoreCase) {
        //noinspection StringEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return ignoreCase ? o1.equalsIgnoreCase(o2) : o1.equals(o2);
    }
    
    public static boolean Equals(final String o1, final String o2) {
        return Equals(o1, o2, false);
    }

    public static boolean Equals(final Integer o1, final Integer o2) {
        //noinspection NumberEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Double o1, final Double o2) {
        //noinspection NumberEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Float o1, final Float o2) {
        //noinspection NumberEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Byte o1, final Byte o2) {
        //noinspection NumberEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Short o1, final Short o2) {
        //noinspection NumberEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Long o1, final Long o2) {
        //noinspection NumberEquality
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Character o1, final Character o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Boolean o1, final Boolean o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final UUID o1, final UUID o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final TimeSpan o1, final TimeSpan o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final DateTime o1, final DateTime o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static boolean Equals(final Date o1, final Date o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static <T> boolean EqualsObjects(final T o1, final T o2) {
        if (o1 == o2) { return true; }
        if (o1 == null || o2 == null) { return false; }
        return o1.equals(o2);
    }
    
    public static int compareHashCode(final Object o1, final Object o2, final RefParam<Boolean> bNull) {
        if (o1 == o2) {
            bNull.value = o1 == null;
            return 0;
        }
        if (o1 == null) {
            bNull.value = true;
            return 1;
        }
        if (o2 == null) {
            bNull.value = true;
            return -1;
        }
        bNull.value = false;
        final Class t1 = o1.getClass(); // t1 = t1.GetPublicBaseType();
        final Class t2 = o2.getClass(); // t2 = t2.GetPublicBaseType();
        int i1 = t1.hashCode();
        int i2 = t2.hashCode();
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        if (o1 instanceof Enum && o2 instanceof Enum) { return ((Enum) o1).compareTo((Enum) o2); }
        i1 = o1.hashCode();
        i2 = o2.hashCode();
        if (i1 > i2) { return 1; }
        if (i1 < i2) { return -1; }
        return 0;
    }
    
    public static final Comparator<Class> TypeComparator = new Comparator<Class>() {
        @Override
        public int compare(final Class t1, final Class t2) {
            return CompareTypes(t1, t2);
        }
    };
    
    public static final Comparator<Integer> IntegerComparator = new Comparator<Integer>() {
        @Override
        public int compare(final Integer t1, final Integer t2) {
            return Compare(t1, t2);
        }
    };
    
    public static final Comparator<Object> HashCodeComparator = new Comparator<Object>() {
        @Override
        public int compare(final Object o1, final Object o2) {
            return CompareHashCode(o1, o2);
        }
    };
    
    public static final Comparator<String> StringComparator = new Comparator<String>() {
        @Override
        public int compare(final String s1, final String s2) {
            return Compare(s1, s2);
        }
    };
    
    public static final Comparator<String> StringComparatorIgnoreCase = new Comparator<String>() {
        @Override
        public int compare(final String s1, final String s2) {
            return Compare(s1, s2, true);
        }
    };
    
    public static final EqualityComparator<String> StringEqualityComparator = new EqualityComparator<String>() {
        @Override
        public Boolean call(final String s1, final String s2) {
            return Equals(s1, s2);
        }
    };
    
    public static final EqualityComparator<String> StringEqualityComparatorIgnoreCase = new EqualityComparator<String>() {
        @Override
        public Boolean call(final String s1, final String s2) {
            return Equals(s1, s2, true);
        }
    };
    
    public static final EqualityComparator<Class> TypeEqualityComparator = new EqualityComparator<Class>() {
        @Override
        public Boolean call(final Class t1, final Class t2) {
            return t1 == t2;
        }
    };
    
    public static final Comparator<byte[]> ByteArrayComparator = new Comparator<byte[]>() {
        @Override
        public int compare(final byte[] b1, final byte[] b2) {
            return CompareList(b1, b2);
        }
    };
    
    public static final Comparator<Object> IdentityHashCodeComparator = new Comparator<Object>() {
        @Override
        public int compare(final Object o1, final Object o2) {
            return CompareIdentityHashCode(o1, o2);
        }
    };
    
    public static final EqualityComparator<Object> IdentityHashCodeEqualityComparator = new EqualityComparator<Object>() {
        @Override
        public Boolean call(final Object o1, final Object o2) {
            return o1 == o2;
        }
    };
    
    public static int CompareTypes(final Class t1, final Class t2) {
        if (t1 == t2) { return 0; }
        if (t1 == null) { return 1; }
        if (t2 == null) { return -1; }
        int i;
        if ((i = Compare(t1.hashCode(), t2.hashCode())) != 0) { return i; }
        return 0;
    }
    
    public static int CompareTypes(final Class t1, final Class t2, final RefParam<Boolean> bNull) {
        if (t1 == t2) {
            bNull.value = t1 == null;
            return 0;
        }
        if (t1 == null) {
            bNull.value = true;
            return 1;
        }
        if (t2 == null) {
            bNull.value = true;
            return -1;
        }
        int i;
        bNull.value = false;
        if ((i = Compare(t1.hashCode(), t2.hashCode())) != 0) { return i; }
        return 0;
    }
    
    public static boolean CompareNullable(final Object o1, final Object o2, final RefParam<Integer> Result) {
        if (o1 == null) {
            if (o2 == null) {
                Result.value = 0;
                return true;
            }
            Result.value = -1;
            return true;
        }
        if (o2 == null) {
            Result.value = 1;
            return true;
        }
        return false;
    }
    
    public static int CompareList(final List o1, final List o2, final Comparator comparator) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        int i;
        final int cnt = o1.size();
        if ((i = Compare(cnt, o2.size())) != 0) { return i; }
        for (int j = 0; j < cnt; j++) {
            if ((i = comparator.compare(o1.get(j), o2.get(j))) != 0) { return i; }
        }
        return 0;
    }
    
    public static int CompareList(final List<Comparable> o1, final List<Comparable> o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        int i;
        final int cnt = o1.size();
        if ((i = Compare(cnt, o2.size())) != 0) { return i; }
        for (int j = 0; j < cnt; j++) {
            final Comparable i1 = o1.get(j);
            final Comparable i2 = o2.get(j);
            if (i1 == i2) { return 0; }
            if (i1 == null) { return 1; }
            if (i2 == null) { return -1; }
            if ((i = i1.compareTo(i2)) != 0) { return i; }
        }
        return 0;
    }
    
    public static <T> int CompareList(final T[] o1, final T[] o2, final Comparator<T> comparator) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        int i;
        final int cnt = o1.length;
        if ((i = Compare(cnt, o2.length)) != 0) { return i; }
        for (int j = 0; j < cnt; j++) {
            if ((i = comparator.compare(o1[j], o2[j])) != 0) { return i; }
        }
        return 0;
    }
    
    public static <T extends Comparable> int CompareList(final T[] o1, final T[] o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        int i;
        final int cnt = o1.length;
        if ((i = Compare(cnt, o2.length)) != 0) { return i; }
        for (int j = 0; j < cnt; j++) {
            final Comparable i1 = o1[j];
            final Comparable i2 = o2[j];
            if (i1 == i2) { return 0; }
            if (i1 == null) { return 1; }
            if (i2 == null) { return -1; }
            if ((i = i1.compareTo(i2)) != 0) { return i; }
        }
        return 0;
    }
    
    public static int CompareList(final byte[] o1, final byte[] o2) {
        if (o1 == o2) { return 0; }
        if (o1 == null) { return 1; }
        if (o2 == null) { return -1; }
        int i;
        final int cnt = o1.length;
        if ((i = Compare(cnt, o2.length)) != 0) { return i; }
        for (int j = 0; j < cnt; j++) {
            if ((i = Compare(o1[j], o2[j])) != 0) { return i; }
        }
        return 0;
    }
    
    public static int GetIterableHashCode(final Iterable list) {
        if (list == null) { return 0; }
        int i = 0;
        for (final Object item : list) {
            if (item == null) {
                continue;
            }
            i ^= item.hashCode();
        }
        return i;
    }
    
    public static <T> int GetIterableHashCode(final T[] list) {
        if (list == null) { return 0; }
        int i = 0;
        for (final Object item : list) {
            if (item == null) {
                continue;
            }
            i ^= item.hashCode();
        }
        return i;
    }
}
