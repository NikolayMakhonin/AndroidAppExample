package com.github.nikolaymakhonin.utils.contracts.delegates;

import rx.functions.Func2;

public interface EqualityComparator<T> extends Func2<T, T, Boolean> {}
