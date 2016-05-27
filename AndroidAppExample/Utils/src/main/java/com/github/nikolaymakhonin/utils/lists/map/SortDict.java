package com.github.nikolaymakhonin.utils.lists.map;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.RefParam;
import com.github.nikolaymakhonin.utils.contracts.delegates.EqualityComparator;
import com.github.nikolaymakhonin.utils.lists.ConvertIterator;
import com.github.nikolaymakhonin.utils.lists.hash_items.CustomHashCodeItem;
import com.github.nikolaymakhonin.utils.lists.hash_items.HashItem;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedEventArgs;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChanged;
import com.github.nikolaymakhonin.utils.lists.list.IList;
import com.github.nikolaymakhonin.utils.lists.list.SortedList;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.functions.Func1;

public class SortDict<TKey, TValue> implements IDictionaryChangedDict<TKey, TValue>, ICollectionChanged<Map.Entry<TKey, TValue>> {
    private static final String LOG_TAG = "SortDict";

    @Override
    public Object Locker() {
        return _items.Locker();
    }
    
    private final SortedList<Entry<HashItem<TKey>, TValue>> _items;
    private       Set<TKey>                                 _keys;
    private       Set<TValue>                               _values;
    private final Func1<TKey, HashItem<TKey>>               _createHashItem;
    
    Comparator<TKey> _Comparator;
    
    public Comparator<TKey> getComparator() {
        return _Comparator;
    }
    
    public void setComparator(final Comparator<TKey> value) {
        _Comparator = value;
    }
    
    EqualityComparator<TKey> _EqualityComparator;
    
    public EqualityComparator<TKey> getEqualityComparator() {
        return _EqualityComparator;
    }
    
    public void setEqualityComparator(final EqualityComparator<TKey> value) {
        _EqualityComparator = value;
    }
    
    public Func1<TKey, Integer> _GetKeyHashCode;
    
    public Func1<TKey, Integer> getGetKeyHashCode() {
        return _GetKeyHashCode;
    }
    
    public void setGetKeyHashCode(final Func1<TKey, Integer> value) {
        _GetKeyHashCode = value;
    }
    
    private final Comparator<Map.Entry<HashItem<TKey>, TValue>> mapEntryComparator = new Comparator<Map.Entry<HashItem<TKey>, TValue>>() {
        @Override
        public int compare(final Map.Entry<HashItem<TKey>, TValue> item1, final Map.Entry<HashItem<TKey>, TValue> item2) {
            return (item1.getKey()).compareTo(item2.getKey(), _Comparator, _EqualityComparator);
        }
    };
    
    public SortDict() {
        this(null, null);
    }
    
    public SortDict(final Comparator<TKey> Comparator) {
        this(Comparator, null);
    }
    
    public SortDict(final Comparator<TKey> Comparator, final EqualityComparator<TKey> equalityComparator) {
        this(Comparator, equalityComparator, null);
    }
    
    public SortDict(final Func1<TKey, HashItem<TKey>> createHashItem) {
        this(null, null, null, createHashItem);
    }
    
    public SortDict(final Comparator<TKey> Comparator, final EqualityComparator<TKey> equalityComparator,
        final Func1<TKey, Integer> getKeyHashCode) {
        this(Comparator, equalityComparator, getKeyHashCode, null);
    }
    
    private SortDict(final Comparator<TKey> Comparator, final EqualityComparator<TKey> equalityComparator,
        final Func1<TKey, Integer> getKeyHashCode, final Func1<TKey, HashItem<TKey>> createHashItem) {
        _Comparator = Comparator;
        _EqualityComparator = equalityComparator;
        _GetKeyHashCode = getKeyHashCode;
        _createHashItem = createHashItem;
        _items = new SortedList<>(true, true, mapEntryComparator);
    }
    
    private Map.Entry<TKey, TValue>[] convertKeyValuePairArray(final Object[] items) {
        if (items == null) { return null; }
        final int length = items.length;
        final Map.Entry<TKey, TValue>[] convertItems = new SimpleEntry[length];
        for (int i = 0; i < length; i++) {
            final Map.Entry<HashItem<TKey>, TValue> item = (Map.Entry) items[i];
            convertItems[i] = new SimpleEntry(item.getKey(), item.getValue());
        }
        return convertItems;
    }

    private HashItem<TKey> createHashItem(final TKey key) {
        if (_createHashItem != null) { return _createHashItem.call(key); }
        return new CustomHashCodeItem<>(key, _GetKeyHashCode);
    }
    
    private TValue putPrivate(final TKey key, final TValue value) {
        _items.add(new SimpleEntry<>(createHashItem(key), value));
        _keys = null;
        _values = null;
        return value;
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return ContainsKey((TKey) key);
    }
    
    @Override
    public boolean ContainsKey(final TKey key) {
        return _items.Contains(new SimpleEntry<>(createHashItem(key), null));
    }
    
    private Set<TKey> getKeys() {
        final Object[] keys = new Object[_items.size()];
        int i = 0;
        for (final Map.Entry<HashItem<TKey>, TValue> item : _items) {
            keys[i++] = item.getKey().Value();
        }
        return new SortedList(false, false, keys);
    }
    
    private Set<TValue> getValues() {
        final Object[] values = new Object[_items.size()];
        int i = 0;
        for (final Map.Entry<HashItem<TKey>, TValue> item : _items) {
            values[i++] = item.getValue();
        }
        return new SortedList(false, false, values);
    }
    
    @Override
    public Set<TKey> keySet() {
        return _keys != null ? _keys : (_keys = getKeys());
    }
    
    @Override
    public TValue remove(final Object key) {
        if (!containsKey(key)) { return null; }
        final TValue value = get(key);
        Remove((TKey) key);
        return value;
    }
    
    @Override
    public boolean Remove(final TKey key) {
        synchronized (_items.Locker()) {
            final int index = _items.IndexOf(new SimpleEntry<>(createHashItem(key), null));
            if (index < 0) { return false; }
            _keys = null;
            _values = null;
            _items.RemoveAt(index);
            return true;
        }
    }
    
    public boolean TryGetValue(final TKey key, final RefParam<TValue> value) {
        synchronized (_items.Locker()) {
            final int index = _items.IndexOf(new SimpleEntry<>(createHashItem(key), null));
            if (index < 0) {
                value.value = null;
                return false;
            }
            value.value = _items.get(index).getValue();
            return true;
        }
    }
    
    @Override
    public Collection<TValue> values() {
        return _values != null ? _values : (_values = getValues());
    }
    
    @Override
    public TValue get(final Object key) {
        return Get((TKey) key);
    }
    
    @Override
    public TValue Get(final TKey key) {
        final RefParam<TValue> value = new RefParam<>();
        if (TryGetValue(key, value)) { return value.value; }
        return null;
    }
    
    @Override
    public TValue put(final TKey key, final TValue value) {
        synchronized (_items.Locker()) {
            final int index = _items.IndexOf(new SimpleEntry<>(createHashItem(key), null));
            if (index < 0) {
                putPrivate(key, value);
                return value;
            }
            setItem(index, new SimpleEntry<>(createHashItem(key), value));
            return value;
        }
    }
    
    protected void setItem(final int index, final Map.Entry<HashItem<TKey>, TValue> item) {
        _items.set(index, item);
        _keys = null;
        _values = null;
    }
    
    public void put(final Map.Entry<TKey, TValue> item) {
        put(item.getKey(), item.getValue());
    }
    
    @Override
    public void clear() {
        synchronized (_items.Locker()) {
            _items.clear();
            _keys = null;
            _values = null;
        }
    }
    
    /** == ContainsKey(item.Key) */
    public boolean Contains(final Map.Entry<TKey, TValue> item) {
        return ContainsKey(item.getKey());
    }
    
    public void CopyTo(final Map.Entry<TKey, TValue>[] array, int arrayIndex) {
        synchronized (_items.Locker()) {
            for (final Map.Entry<HashItem<TKey>, TValue> item : _items) {
                array[arrayIndex++] = new SimpleEntry<>(item.getKey().Value(), item.getValue());
            }
        }
    }
    
    @Override
    public int size() {
        return _items.size();
    }
    
    public boolean IsReadOnly() {
        return false;
    }
    
    /** == Remove(item.Key); */
    public boolean Remove(final Map.Entry<TKey, TValue> item) {
        return Remove(item.getKey());
    }
    
    private final Func1<Map.Entry<HashItem<TKey>, TValue>, Map.Entry<TKey, TValue>> _convertEntryFunc
        = entry -> new SimpleEntry<>(entry.getKey().Value(), entry.getValue());
    
    @Override
    public Set<Map.Entry<TKey, TValue>> entrySet() {
        final IList<Entry<TKey, TValue>> items = new SortedList<>(false, false);
        int                              i     = 0;
        for (final Map.Entry<HashItem<TKey>, TValue> item : _items) {
            items.set(i++, new SimpleEntry<>(item.getKey().Value(), item.getValue()));
        }
        return items;
    }
    
    @Override
    public Iterator<Map.Entry<TKey, TValue>> iterator() {
        return new ConvertIterator<>(_items.iterator(), _convertEntryFunc);
    }
    
    @Override
    public boolean isEmpty() {
        return _items.isEmpty();
    }
    
    @Override
    public void putAll(final Map<? extends TKey, ? extends TValue> map) {
        for (final TKey key : map.keySet()) {
            put(key, map.get(key));
        }
    }
    
    @Override
    public boolean containsValue(final Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void OnItemModified(final TKey key) {
        synchronized (_items.Locker()) {
            if (_items.CollectionChangedHasObservers()) {
                SimpleEntry<HashItem<TKey>, TValue> entry = new SimpleEntry<>(createHashItem(key), null);
                final int index = _items.IndexOf(entry);
                if (index < 0) {
                    Log.e(LOG_TAG, "OnItemModified: Item not found by key=" + key);
                } else {
                    entry.setValue(_items.get(index).getValue());
                    _items.OnItemModified(index, entry);
                }
            }
        }
    }
    
    @Override
    public void OnItemModified(final TKey key, final TValue oldValue) {
        synchronized (_items.Locker()) {
            if (_items.CollectionChangedHasObservers()) {
                SimpleEntry<HashItem<TKey>, TValue> entry = new SimpleEntry<>(createHashItem(key), oldValue);
                final int index = _items.IndexOf(entry);
                if (index < 0) {
                    Log.e(LOG_TAG, "OnItemModified: Item not found by key=" + key);
                } else {
                    _items.OnItemModified(index, entry);
                }
            }
        }
    }

    @Override
    public void OnItemModified(final int index, final Map.Entry<TKey, TValue> oldItem) {
        synchronized (_items.Locker()) {
            final HashItem<TKey> key = _items.get(index).getKey();
            _items.OnItemModified(index, new SimpleEntry<>(key, oldItem.getValue()));
        }
    }

    @Override
    public void OnItemModified(final int index) {
        _items.OnItemModified(index);
    }

    //region Changed events

    private Observable<DictionaryChangedEventArgs<TKey, TValue>> _dictionaryChangedObservable;

    @Override
    public Observable<DictionaryChangedEventArgs<TKey, TValue>> DictionaryChanged() {
        if (_dictionaryChangedObservable == null) {
            synchronized (Locker()) {
                if (_dictionaryChangedObservable == null) {
                    _dictionaryChangedObservable = _items.CollectionChanged().flatMap(eventArgs -> {
                        int count;
                        Object[] oldItems, newItems;
                        DictionaryChangedEventArgs<TKey, TValue>[] result;
                        switch (eventArgs.getChangedType()) {
                            case Added:
                                newItems = eventArgs.getNewItems();
                                count = newItems.length;
                                result = new DictionaryChangedEventArgs[count];
                                for (int i = 0; i < count; i++) {
                                    Entry<TKey, TValue> item = (Entry<TKey, TValue>) newItems[i];
                                    result[i] = new DictionaryChangedEventArgs<>(
                                        this,
                                        DictionaryChangedType.Added,
                                        item.getKey(),
                                        null,
                                        item.getValue()
                                    );
                                }
                                break;
                            case Removed:
                                oldItems = eventArgs.getOldItems();
                                count = oldItems.length;
                                result = new DictionaryChangedEventArgs[count];
                                for (int i = 0; i < count; i++) {
                                    Entry<TKey, TValue> item = (Entry) oldItems[i];
                                    result[i] = new DictionaryChangedEventArgs<>(
                                        this,
                                        DictionaryChangedType.Removed,
                                        item.getKey(),
                                        item.getValue(),
                                        null
                                    );
                                }
                                break;
                            case Setted:
                                Entry<TKey, TValue> oldItem = (Entry) eventArgs.getOldItems()[0];
                                Entry<TKey, TValue> newItem = (Entry) eventArgs.getNewItems()[0];
                                TKey oldKey = oldItem.getKey();
                                TKey newKey = newItem.getKey();
                                if (CompareUtils.EqualsObjects(oldKey, newKey)) {
                                    result = new DictionaryChangedEventArgs[] {
                                        new DictionaryChangedEventArgs<>(
                                            this,
                                            DictionaryChangedType.Setted,
                                            newKey,
                                            oldItem.getValue(),
                                            newItem.getValue()
                                        )
                                    };
                                } else {
                                    result = new DictionaryChangedEventArgs[] {
                                        new DictionaryChangedEventArgs<>(
                                            this,
                                            DictionaryChangedType.Removed,
                                            oldKey,
                                            oldItem.getValue(),
                                            null
                                        ),
                                        new DictionaryChangedEventArgs<>(
                                            this,
                                            DictionaryChangedType.Added,
                                            newKey,
                                            null,
                                            newItem.getValue()
                                        )
                                    };
                                }
                                break;
                            default:
                                return null;
                        }

                        return Observable.from(result);
                    });
                }
            }
        }
        return _dictionaryChangedObservable;
    }

    private Observable<CollectionChangedEventArgs> _collectionChangedObservable;

    @Override
    public Observable<CollectionChangedEventArgs> CollectionChanged() {
        if (_collectionChangedObservable == null) {
            synchronized (Locker()) {
                if (_collectionChangedObservable == null) {
                    _collectionChangedObservable = _items.CollectionChanged().map(eventArgs ->
                        new CollectionChangedEventArgs(
                            this,
                            eventArgs.getChangedType(),
                            eventArgs.getOldIndex(),
                            eventArgs.getNewIndex(),
                            castHashItems(eventArgs.getOldItems()),
                            castHashItems(eventArgs.getNewItems()))
                    );
                }
            }
        }
        return _collectionChangedObservable;
    }

    private static Object[] castHashItems(Object[] items) {
        if (items == null) {
            return null;
        }
        int count = items.length;
        Object[] result = new Object[count];
        for (int i = 0; i < count; i++) {
            result[i] = ((HashItem)items[i]).Value();
        }
        return result;
    }

    //endregion

}
