package com.github.nikolaymakhonin.utils.lists.list;

import com.github.nikolaymakhonin.utils.contracts.patterns.EventArgs;

public class CollectionChangedEventArgs extends EventArgs {

    private final int _oldIndex;
    
    /** индекс первого элемента OldItems */
    public int getOldIndex() {
        return _oldIndex;
    }

    private final int _newIndex;
    
    /** индекс первого элемента NewItems */
    public int getNewIndex() {
        return _newIndex;
    }

    private final Object[] _oldItems;
    
    public Object[] getOldItems() {
        return _oldItems;
    }

    private final Object[] _newItems;
    
    public Object[] getNewItems() {
        return _newItems;
    }

    private final CollectionChangedType _changedType;
    
    public CollectionChangedType getChangedType() {
        return _changedType;
    }
    
    public CollectionChangedEventArgs(Object object, final CollectionChangedType changedType, final int oldIndex, final int newIndex, final Object[] oldItems,
        final Object[] newItems)
    {
        super(object);
        _newIndex = newIndex;
        _oldIndex = oldIndex;
        _oldItems = oldItems;
        _newItems = newItems;
        _changedType = changedType;
    }
}
