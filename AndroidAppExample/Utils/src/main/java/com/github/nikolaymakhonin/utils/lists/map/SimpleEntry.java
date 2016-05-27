package com.github.nikolaymakhonin.utils.lists.map;

import java.util.Map;

public class SimpleEntry<TKey, TValue> implements Map.Entry<TKey, TValue> {
    final TKey _key;
    TValue _value;
    
    public SimpleEntry(final TKey key, final TValue value) {
        _key = key;
        _value = value;
    }
    
    @Override
    public TKey getKey() {
        return _key;
    }
    
    @Override
    public TValue getValue() {
        return _value;
    }
    
    @Override
    public TValue setValue(final TValue value) {
        _value = value;
        return _value;
    }
    
    @Override
    public String toString() {
        return "[" + ((_key) == null ? "<null>" : _key.toString()) + ", " 
                + ((_value) == null ? "<null>" : _value.toString()) + "]";
    }
}
