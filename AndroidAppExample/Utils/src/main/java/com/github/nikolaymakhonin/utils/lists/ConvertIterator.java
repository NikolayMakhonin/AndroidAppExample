package com.github.nikolaymakhonin.utils.lists;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rx.functions.Func1;

public class ConvertIterator<TSource, TDest> implements Iterator<TDest> {
    private final Iterator              _Iterator;
    private final Func1<TSource, TDest> _convertFunc;
    
    public ConvertIterator(final Iterator Iterator) {
        this(Iterator, null);
    }
    
    public ConvertIterator(final Iterator Iterator, final Func1<TSource, TDest> convertFunc) {
        _Iterator = Iterator;
        _convertFunc = convertFunc;
    }
    
    @Override
    public boolean hasNext() {
        return _Iterator.hasNext();
    }
    
    @Override
    public TDest next() {
        if (!_Iterator.hasNext()) {
            throw new NoSuchElementException();
        }
        final Object o = _Iterator.next();
        return (_convertFunc != null)
            ? _convertFunc.call((TSource) o)
            : (TDest) o;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
