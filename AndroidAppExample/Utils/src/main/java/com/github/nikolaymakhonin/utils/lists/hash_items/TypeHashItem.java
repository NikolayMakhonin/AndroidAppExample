package com.github.nikolaymakhonin.utils.lists.hash_items;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.delegates.EqualityComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TypeHashItem extends HashItem<Class> {
    public TypeHashItem(final Class value) {
        super(value);
    }
    
    @Override
    public int compareTo(final Object obj, final Comparator<Class> Comparator,
        final EqualityComparator<Class> equalityComparator) {
        return super.compareTo(obj, Comparator != null ? Comparator : CompareUtils.TypeComparator,
            equalityComparator != null ? equalityComparator : CompareUtils.TypeEqualityComparator);
    }
    
    private boolean equalityTypes(final Class t1, final Class t2) {
        return t1 == t2;
    }
    
    public static List<TypeHashItem> ConvertToTypeHashItems(final List<Class> items) {
        if (items == null) { return null; }
        final List<TypeHashItem> newItems = new ArrayList<>(items.size());
        final int len = items.size();
        for (int i = 0; i < len; i++) {
            newItems.set(i, new TypeHashItem(items.get(i)));
        }
        return newItems;
    }
    
//    public static List<Class> ConvertToItems(final List<TypeHashItem> items) {
//        if (items == null) { return null; }
//        final List<Class> newItems = new ArrayList(items.size());
//        final int len = items.size();
//        for (int i = 0; i < len; i++) {
//            newItems.set(i, items.get(i).Value());
//        }
//        return newItems;
//    }
}
