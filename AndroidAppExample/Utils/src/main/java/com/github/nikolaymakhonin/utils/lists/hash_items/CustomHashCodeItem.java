package com.github.nikolaymakhonin.utils.lists.hash_items;

import rx.functions.Func1;

public class CustomHashCodeItem<T> extends HashItem<T> {
    public CustomHashCodeItem(final T value, final Func1<T, Integer> getHashCode) {
        super(value, getHashCode);
    }
}
