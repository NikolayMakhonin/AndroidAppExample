package com.github.nikolaymakhonin.utils.lists.map;

import java.util.Map;

public interface IDictionary<TKey, TValue> extends Map<TKey, TValue>, Iterable<Map.Entry<TKey, TValue>> {
    TValue Get(TKey key);
    
    boolean ContainsKey(TKey key);
    
    boolean Remove(TKey key);
}
