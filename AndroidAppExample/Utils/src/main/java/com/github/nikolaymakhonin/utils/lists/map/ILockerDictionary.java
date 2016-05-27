package com.github.nikolaymakhonin.utils.lists.map;

import com.github.nikolaymakhonin.utils.contracts.patterns.ILocker;

public interface ILockerDictionary<TKey, TValue> extends IDictionary<TKey, TValue>, ILocker {}
