package com.github.nikolaymakhonin.utils.lists.hash_items;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.delegates.EqualityComparator;

import java.util.Comparator;

import rx.functions.Func1;

public class StringIgnoreCaseHashItem extends HashItem<String> {
    public StringIgnoreCaseHashItem(final String value) {
        super(value, hashCoder);
    }
    
    @Override
    public int compareTo(final Object obj, final Comparator<String> comparator,
        final EqualityComparator<String> equalityComparator) {
        return super.compareTo(obj, comparator != null ? comparator : CompareUtils.StringComparatorIgnoreCase,
            equalityComparator != null ? equalityComparator : CompareUtils.StringEqualityComparatorIgnoreCase);
    }
    
    private static final Func1<String, Integer> hashCoder = key -> key == null ? 0 : key.toLowerCase().hashCode();
    
    public static StringIgnoreCaseHashItem cast(final String value) {
        return new StringIgnoreCaseHashItem(value);
    }
    
    public static String cast(final StringIgnoreCaseHashItem item) {
        return item.Value();
    }
}
