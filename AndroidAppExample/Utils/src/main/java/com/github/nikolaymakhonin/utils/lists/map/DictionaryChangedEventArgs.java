package com.github.nikolaymakhonin.utils.lists.map;

import com.github.nikolaymakhonin.utils.contracts.patterns.EventArgs;

public class DictionaryChangedEventArgs<TKey, TValue> extends EventArgs {
    private DictionaryChangedEventArgs _eventArgsObject;
    
    public DictionaryChangedEventArgs getEventArgsObject() {
        if (_eventArgsObject != null) { return _eventArgsObject; }
        return _eventArgsObject = new DictionaryChangedEventArgs(_object, _changedType, _key, _oldValue, _newValue);
    }

    private final TKey _key;
    
    public TKey getKey() {
        return _key;
    }

    private final TValue _oldValue;
    
    public TValue getOldValue() {
        return _oldValue;
    }

    private final TValue _newValue;
    
    public TValue getNewValue() {
        return _newValue;
    }

    private final DictionaryChangedType _changedType;
    
    public DictionaryChangedType getChangedType() {
        return _changedType;
    }
    
    public DictionaryChangedEventArgs(Object object, final DictionaryChangedType changedType, final TKey key, final TValue oldValue,
        final TValue newValue)
    {
        super(object);
        _key = key;
        _oldValue = oldValue;
        _newValue = newValue;
        _changedType = changedType;
    }
}
