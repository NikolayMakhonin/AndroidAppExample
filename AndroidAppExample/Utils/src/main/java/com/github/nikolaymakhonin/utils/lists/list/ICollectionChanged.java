package com.github.nikolaymakhonin.utils.lists.list;

import rx.Observable;

public interface ICollectionChanged<T> {
    Observable<CollectionChangedEventArgs> CollectionChanged();
    
    void OnItemModified(int index);
    
    void OnItemModified(int index, T oldItem);
}
