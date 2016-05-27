package com.github.nikolaymakhonin.utils.lists.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SortedListIterator<T> implements Iterator<T> {
    private final int _count;
    private int _index;
    private final Object[] _list;
    
    public SortedListIterator(final Object[] list, final int count) {
        _count = count;
        _index = 0;
        _list = list;
    }
    
    @Override
    public boolean hasNext() {
        return _index < _count;
    }
    
    @Override
    public T next() {
        if (!hasNext()) { throw new NoSuchElementException(); }
        return (T)_list[_index++];
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
