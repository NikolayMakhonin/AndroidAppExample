package com.github.nikolaymakhonin.utils.lists.list;

import java.util.List;
import java.util.Set;

public interface IList<T> extends List<T>, Set<T> {
    void Insert(int index, T item);
    
    void setSize(final int value, final T defaultValue);
    
    int IndexOf(T item);
    
    T RemoveAt(int index);
    
    void AddAll(Iterable<? extends T> collection);
    
    <T2 extends T> void AddAll(final T2[] collection);

    void AddAll(final int index, final T[] collection);

    void Sort();
    
    void ReSort();
    
    boolean Contains(T item);
    
    boolean Remove(T item);
    
    <T2> T2[] toArray(Class<? extends T2[]> classOfArrayT);
    
    void CopyTo(final T[] array, final int arrayIndex);
}
