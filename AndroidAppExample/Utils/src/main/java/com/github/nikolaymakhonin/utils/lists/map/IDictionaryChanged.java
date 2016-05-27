package com.github.nikolaymakhonin.utils.lists.map;

import rx.Observable;

public interface IDictionaryChanged<TKey, TValue>
{
    Observable<DictionaryChangedEventArgs<TKey, TValue>> DictionaryChanged();
    
    void OnItemModified(TKey index, TValue oldValue);
}
