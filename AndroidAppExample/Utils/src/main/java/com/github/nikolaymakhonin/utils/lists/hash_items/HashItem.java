package com.github.nikolaymakhonin.utils.lists.hash_items;

import com.github.nikolaymakhonin.utils.ClassUtils;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.delegates.EqualityComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rx.functions.Func1;

public class HashItem<T> implements Comparable {
    protected final int _hashCode;
    protected final T _value;
    protected final Class _type;
    protected final int _typeHashCode;
    
    public T Value() {
        return _value;
    }
    
    public HashItem(final T value) {
        this(value, null);
    }
    
    protected HashItem(final T value, final Func1<T, Integer> getHashCode) {
        _value = value;
        if ((_value) == null) {
            _hashCode = 0;
            _type = Object.class;
            _typeHashCode = 0;
        } else {
            _hashCode = getHashCode == null ? _value.hashCode() : getHashCode.call(_value);
            _type = _value.getClass();
            _typeHashCode = _type.hashCode();
        }
    }
    
    @Override
    public int compareTo(final Object obj) {
        return compareTo(obj, null, null);
    }
    
    public int compareTo(final Object obj, final Comparator<T> Comparator,
        final EqualityComparator<T> equalityComparator) {
        if (!(obj instanceof HashItem)) { return 1; }
        final HashItem<T> hashItem = (HashItem<T>) obj;
        if (_value == hashItem._value) { return 0; }
        if (_value == null) { return 1; }
        if (hashItem._value == null) { return -1; }
        int i;
        
        if ((i = CompareUtils.Compare(_hashCode, hashItem._hashCode)) != 0) { return i; }
        
        if (Comparator != null) { return Comparator.compare(_value, hashItem._value); }
        
        final boolean v1_comparable = _value instanceof Comparable;
        if (v1_comparable
            && (_typeHashCode == hashItem._typeHashCode && hashItem._type.equals(_type) || ClassUtils
                .IsSubClassOrInterface(hashItem._type, _type))) { return ((Comparable) _value)
            .compareTo(hashItem._value); }
        
        final boolean v2_comparable = hashItem._value instanceof Comparable;
        if (v2_comparable && (ClassUtils.IsSubClassOrInterface(_type, hashItem._type))) { return -((Comparable) hashItem._value)
            .compareTo(_value); }
        
        if (!v1_comparable && !v2_comparable) {
            if (equalityComparator != null) {
                if (equalityComparator.call(_value, hashItem._value)) { return 0; }
            } else {
                if (_value.equals(hashItem._value)) { return 0; }
            }
            
            if ((i = CompareUtils.Compare(_typeHashCode, hashItem._typeHashCode)) != 0) { return i; }
            
            return CompareUtils.CompareTypes(_type, hashItem._type);
        }
        
        return 0;
    }
    
    public static List<HashItem> ConvertToHashItems(final List items) {
        if (items == null) { return null; }
        final List<HashItem> newItems = new ArrayList<>(items.size());
        final int len = items.size();
        for (int i = 0; i < len; i++) {
            newItems.set(i, new HashItem(items.get(i)));
        }
        return newItems;
    }
    
    public static List ConvertToItems(final List<HashItem> items) {
        if (items == null) { return null; }
        final List newItems = new ArrayList(items.size());
        final int len = items.size();
        for (int i = 0; i < len; i++) {
            newItems.set(i, items.get(i).Value());
        }
        return newItems;
    }
    
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return compareTo(obj, null, null) == 0;
    }
    
    @Override
    public String toString() {
        return (_value) == null ? "<null>" : _value.toString();
    }
    
    @Override
    public int hashCode() {
        return _hashCode;
    }
}
