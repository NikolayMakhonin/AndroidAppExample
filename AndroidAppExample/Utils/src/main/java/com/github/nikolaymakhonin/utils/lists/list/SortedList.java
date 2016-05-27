package com.github.nikolaymakhonin.utils.lists.list;

import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.utils.ArrayUtilsExt;
import com.github.nikolaymakhonin.utils.ClassUtils;
import com.github.nikolaymakhonin.utils.CompareUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class SortedList<T> implements ICollectionChangedList<T>, ISortedCollection {

    private static final String LOG_TAG = "SortedList";
    private final static float RESIZE_COEF = 2.0f;
    private final static float RESIZE_COEF_2 = RESIZE_COEF * RESIZE_COEF;
    private int _count;
    private Object[] _list;

    //region Locker

    private Object _locker = new Object();
    
    @Override
    public Object Locker() {
        return _locker;
    }

    public void setLocker(Object locker) {
        _locker = locker;
    }

    //endregion

    //region Settings

    private boolean _notAddIfExists;
    
    public boolean getNotAddIfExists() {
        return _notAddIfExists;
    }
    
    public void setNotAddIfExists(final boolean value) {
        _notAddIfExists = value;
    }

    public boolean IsReadOnly() {
        return false;
    }

    //endregion

    //region Comparator

    private ListItemComparator _comparator = new ListItemComparator<T>();

    private class ListItemComparator<T> implements Comparator<T> {
        public Comparator<T> comparator;
        
        // public EqualityComparator<T> equalityComparator;
        
        @Override
        public int compare(final T o1, final T o2) {
            if (comparator != null) { return comparator.compare(o1, o2); }
            if (o1 == o2) { return 0; }
            if (o1 == null) { return 1; }
            if (o2 == null) { return -1; }
            
            final boolean v1_comparable = o1 instanceof Comparable;
            Class c1 = null, c2 = null;
            if (v1_comparable) {
                c1 = o1.getClass();
                c2 = o2.getClass();
                if (c2 == c1 || c2.equals(c1) || ClassUtils.IsSubClassOrInterface(c2, c1)) { return ((Comparable) o1)
                    .compareTo(o2); }
            }
            
            final boolean v2_comparable = o2 instanceof Comparable;
            if (v2_comparable) {
                if (!v1_comparable) {
                    c1 = o1.getClass();
                    c2 = o2.getClass();
                }
                if (ClassUtils.IsSubClassOrInterface(c1, c2)) { return -((Comparable) o2).compareTo(o1); }
            }
            
            // if (!v1_comparable && !v2_comparable) {
            // if (equalityComparator != null) {
            // if (equalityComparator.invoke(o1, o2)) { return 0; }
            // } else {
            // if (o1.equals(o2)) { return 0; }
            // }
            // }
            // return CompareUtils.CompareTypes(c1, c2);
            
            return CompareUtils.Compare(System.identityHashCode(o1), System.identityHashCode(o2));
        }
    };
    
    public Comparator<? super Object> getComparator() {
        return _comparator == null ? null : _comparator.comparator;
    }
    
    public void setComparator(final Comparator<? super T> value) {
        synchronized (_locker) {
            if (_comparator == null) {
                if (value == null) { return; }
                _comparator = new ListItemComparator<T>();
                _comparator.comparator = value;
                _countSorted = 0;
                return;
            }
            if (_comparator.comparator == value) { return; }
            _comparator.comparator = value;
            _countSorted = 0;
        }
    }

    //endregion

    //region Constructors

    public SortedList(final Iterable<? extends T> list) {
        this();
        AddAll(list);
    }
    
    public SortedList(final boolean autoSort, final boolean notAddIfExists, final Comparator<? super T> Comparator,
        final Iterable<? extends T> list) {
        this(autoSort, notAddIfExists, Comparator);
        AddAll(list);
    }
    
    public SortedList(final boolean autoSort, final boolean notAddIfExists, final Iterable<? extends T> list) {
        this(autoSort, notAddIfExists);
        AddAll(list);
    }
    
    public SortedList(final Object[] list) {
        this(false, false, null, list);
    }
    
    public SortedList() {
        this(false, false, null, (Object[]) null);
    }
    
    public SortedList(final boolean autoSort) {
        this(autoSort, false, null, (Object[]) null);
    }
    
    public SortedList(final boolean autoSort, final boolean notAddIfExists) {
        this(autoSort, notAddIfExists, null, (Object[]) null);
    }
    
    public SortedList(final boolean autoSort, final boolean notAddIfExists, final Object[] list) {
        this(autoSort, notAddIfExists, null, list);
    }
    
    public SortedList(final boolean autoSort, final boolean notAddIfExists, final Comparator<? super T> Comparator) {
        this(autoSort, notAddIfExists, Comparator, (Object[]) null);
    }
    
    public SortedList(final boolean autoSort, final boolean notAddIfExists, final Comparator<? super T> Comparator,
        final Object[] list) {
        _autoSort = autoSort;
        _notAddIfExists = notAddIfExists;
        setComparator(Comparator);
        if (list != null) {
            if (_notAddIfExists) {
                _list = new Object[4];
                _count = 0;
                AddAllPrivate(list);
            } else {
                _list = list;
                _count = Array.getLength(_list);
            }
        } else {
            _list = new Object[4];
            _count = 0;
        }
    }

    //endregion

    //region Size

    @Override
    public int size() {
        return _count;
    }
    
    @Override
    public void setSize(final int value, final T defaultValue) {
        setSize(value, true, true, defaultValue);
    }
    
    private void setSize(final int value) {
        setSize(value, true, false, null);
    }
    
    private void setSize(final int value, final boolean enableCollectionChanged) {
        setSize(value, enableCollectionChanged, false, null);
    }
    
    private void setSize(final int value, final boolean enableCollectionChanged, final boolean fillDefaultValue,
        final Object defaultValue) {
        synchronized (_locker) {
            final int oldCount = _count;
            _count = value;
            if (value < _countSorted) {
                _countSorted = value;
            }
            if (value < oldCount) {
                if (enableCollectionChanged && _collectionChanged.hasObservers()) {
                    final Object[] removedList = new Object[oldCount - value];
                    System.arraycopy(_list, value, removedList, 0, removedList.length);
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Removed, value, -1,
                        removedList, null));
                }
            }
            if (value > Array.getLength(_list)) {
                _list = ArrayUtilsExt.copyOf(_list, (int) (value * RESIZE_COEF), value);
            } else if (value * RESIZE_COEF_2 < Array.getLength(_list)) {
                _list = ArrayUtilsExt.copyOf(_list, (int) (value * RESIZE_COEF), value);
            } else if (value < oldCount) {
                for (int i = value; i < oldCount; i++) {
                    _list[i] = null;
                }
            }
            if (value > oldCount) {
                if (fillDefaultValue) {
                    Arrays.fill(_list, oldCount, value, defaultValue);
                }
                if (enableCollectionChanged && _collectionChanged.hasObservers()) {
                    final Object[] addedList = new Object[value - oldCount];
                    System.arraycopy(_list, oldCount, addedList, 0, addedList.length);
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Added, -1, oldCount,
                        null, addedList));
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return _count == 0;
    }

    //endregion

    //region Search

    private int binarySearch(final int startIndex, final int length, final T item) {
        return _comparator == null ? Arrays.binarySearch(_list, startIndex, startIndex + length, item) : Arrays
            .binarySearch(_list, startIndex, startIndex + length, item, _comparator);
    }
    
    @Override
    public int indexOf(final Object item) {
        return IndexOf((T) item);
    }
    
    @Override
    public int IndexOf(final T item) {
        synchronized (_locker) {
            if (_autoSort) {
                Sort();
            }
            final int index = binarySearch(0, _countSorted, item);
            if (index < 0 && _countSorted < _count) {
                for (int i = _countSorted; i < _count; i++) {
                    final T item2 = (T) _list[i];
                    int Comparatoresult;
                    if (_comparator != null) {
                        Comparatoresult = _comparator.compare(item, item2);
                    } else if (item instanceof Comparable) {
                        Comparatoresult = ((Comparable) item).compareTo(item2);
                    } else if (item2 instanceof Comparable) {
                        Comparatoresult = -((Comparable) item2).compareTo(item);
                    } else {
                        Comparatoresult = (item == item2 || item != null && item.equals(item2)) ? 0 : -1;
                    }
                    if (Comparatoresult == 0) { return i; }
                }
            }
            return index;
        }
    }

    @Override
    public boolean contains(final Object item) {
        return Contains((T) item);
    }

    @Override
    public boolean Contains(final T item) {
        return IndexOf(item) >= 0;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        synchronized (_locker) {
            for (final Object item : collection) {
                if (!Contains((T) item)) { return false; }
            }
            return true;
        }
    }

    @Override
    public int lastIndexOf(final Object object) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion

    //region Add, Insert

    @Override
    public boolean add(final T item) {
        synchronized (_locker) {
            if (_notAddIfExists || _autoSort) {
                int i = IndexOf(item);
                if (i < 0) {
                    i = ~i;
                } else if (_notAddIfExists) { return false; }
                if (_autoSort) {
                    _countSorted++;
                }
                insert((_autoSort) ? i : _count, item);
                return true;
            }
            insert(_count, item);
            return true;
        }
    }

    @Override
    public void add(final int index, final T object) {
        Insert(index, object);
    }
    
    @Override
    public void Insert(final int index, final T item) {
        synchronized (_locker) {
            if (_autoSort) {
                add(item);
                return;
            }
            if (_notAddIfExists && Contains(item)) {
                return;
            }			
            if (index < _countSorted) {
                _countSorted = index;
            }
            insert(index, item);
        }
    }
    
    private void insert(final int index, final T item) {
        setSize(_count + 1, false);
        for (int i = _count - 1; i > index; i--) {
            _list[i] = _list[i - 1];
        }
        _list[index] = item;
        if (_collectionChanged.hasObservers()) {
            if (index < _count - 1) {
                OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Shift, index, index + 1,
                    null, null));
            }
            OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Added, -1, index, null,
                new Object[] { item }));
        }
    }

    //endregion

    //region Add collection

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        AddAll(collection);
        return true;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> collection) {
        synchronized (_locker) {
            if (!_autoSort) {
                final int oldCount = _count;
                final int collectionCount = collection.size();
                if (collectionCount <= 0) { return true; }
                setSize(_count + collectionCount, false);
                if (index < _countSorted) {
                    _countSorted = index;
                }
                System.arraycopy(_list, index, _list, index + collectionCount, oldCount - index);

                if (_collectionChanged.hasObservers()) {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Shift, index, index
                        + collectionCount, null, null));
                }

                int i = index;
                for (final T item : collection) {
                    _list[i++] = item;
                }

                if (_collectionChanged.hasObservers()) {
                    final Object[] addedList = new Object[collectionCount];
                    System.arraycopy(_list, index, addedList, 0, collectionCount);
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Added, -1, index, null,
                        addedList));
                }
            } else {
                AddAll(collection);
            }
        }
        return true;
    }

    @Override
    public void AddAll(final int index, final T[] collection) {
        synchronized (_locker) {
            if (!_autoSort) {
                final int oldCount = _count;
                final int collectionCount = collection.length;
                if (collectionCount <= 0) {
                    return;
                }
                setSize(_count + collectionCount, false);
                if (index < _countSorted) {
                    _countSorted = index;
                }
                System.arraycopy(_list, index, _list, index + collectionCount, oldCount - index);

                if (_collectionChanged.hasObservers()) {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Shift, index, index
                        + collectionCount, null, null));
                }

                int i = index;
                for (final T item : collection) {
                    _list[i++] = item;
                }

                if (_collectionChanged.hasObservers()) {
                    final Object[] addedList = new Object[collectionCount];
                    System.arraycopy(_list, index, addedList, 0, collectionCount);
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Added, -1, index, null,
                        addedList));
                }
            } else {
                AddAll(collection);
            }
        }
        return;
    }

    @Override
    public void AddAll(final Iterable<? extends T> collection) {
        synchronized (_locker) {
            if (!_autoSort) {
                final int oldCount = _count;
                for (final T item : collection) {
                    setSize(_count + 1, false);
                    _list[_count - 1] = item;
                }
                if (_count > oldCount) {
                    if (_collectionChanged.hasObservers()) {
                        final Object[] addedList = new Object[_count - oldCount];
                        System.arraycopy(_list, oldCount, addedList, 0, addedList.length);
                        OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Added, -1,
                            oldCount, null, addedList));
                    }
                }
            } else {
                for (final T item : collection) {
                    add(item);
                }
            }
        }
    }

    @Override
    public <T2 extends T> void AddAll(final T2[] collection) {
        AddAllPrivate(collection);
    }

    private <T2> void AddAllPrivate(final T2[] collection) {
        synchronized (_locker) {
            if (!_autoSort) {
                final int oldCount = _count;
                for (final Object item : collection) {
                    setSize(_count + 1, false);
                    _list[_count - 1] = item;
                }
                if (_count > oldCount) {
                    if (_collectionChanged.hasObservers()) {
                        final Object[] addedList = new Object[_count - oldCount];
                        System.arraycopy(_list, oldCount, addedList, 0, addedList.length);
                        OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Added, -1,
                            oldCount, null, addedList));
                    }
                }
            } else {
                for (final Object item : collection) {
                    add((T) item);
                }
            }
        }
    }

    //endregion

    //region Remove

    @Override
    public T remove(final int index) {
        synchronized (_locker) {
            final T value = get(index);
            RemoveAt(index);
            return value;
        }
    }

    @Override
    public T RemoveAt(final int index) {
        synchronized (_locker) {
            final T oldItem = (T) _list[index];
            for (int i = index; i < _count - 1; i++) {
                _list[i] = _list[i + 1];
            }
            if (index < _countSorted) {
                _countSorted--;
            }
            setSize(_count - 1, false);
            if (_collectionChanged.hasObservers()) {
                OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Removed, index, -1,
                    new Object[] { oldItem }, null));
                if (index < _count) {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Shift, index + 1,
                        index, null, null));
                }
            }
            return oldItem;
        }
    }

    @Override
    public boolean remove(final Object item) {
        return Remove((T) item);
    }

    @Override
    public boolean Remove(final T item) {
        synchronized (_locker) {
            final int i = IndexOf(item);
            if (i >= 0) {
                RemoveAt(i);
            }
            return i >= 0;
        }
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        for (final Object item : collection) {
            Remove((T) item);
        }
        return true;
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        final IList sortedCollection = new SortedList(true, true, collection);
        synchronized (_locker) {
            final List<Integer> removeIndexes = new ArrayList<Integer>();
            for (int i = _count - 1; i >= 0; i--) {
                if (!sortedCollection.Contains(_list[i])) {
                    removeIndexes.add(i);
                }
            }
            for (final int index : removeIndexes) {
                RemoveAt(index);
            }
            return true;
        }
    }

    @Override
    public void clear() {
        synchronized (_locker) {
            setSize(0);
        }
    }

    //endregion

    //region Get, Set

    @Override
    public T get(final int index) {
        if (index >= _count) { throw new IllegalArgumentException(String.format("SortedList: index(%s) >= count(%s)",
            index, _count)); }
        if (_autoSort) {
            Sort();
        }
        return (T) _list[index];
    }
    
    @Override
    public T set(final int index, final T value) {
        synchronized (_locker) {
            if (index >= _count) { throw new IllegalArgumentException(String.format(
                "SortedList: index(%s) >= count(%s)", index, _count)); }
            if (_autoSort) {
                Sort();
            }
            final T oldItem = (T) _list[index];
            if (_autoSort || _notAddIfExists) {
                int i = IndexOf(value);
                if (i < 0) {
                    i = ~i;
                    if (i > index) {
                        i--;
                    }
                } else if (_notAddIfExists && i != index) {
                    final T existsValue = (T) _list[i];
                    RemoveAt(index);
                    return existsValue;
                }
                _list[index] = value;
                if (_collectionChanged.hasObservers()) {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Setted, index, index,
                        new Object[] { oldItem }, new Object[] { value }));
                }
                if (_autoSort) {
                    move(index, i);
                    if (index < _countSorted && i >= _countSorted) {
                        _countSorted--;
                    }
                    if (index != i && _collectionChanged.hasObservers()) {
                        OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Moved, index, i,
                            null, new Object[] { value }));
                    }
                } else {
                    if (index < _countSorted) {
                        _countSorted = index;
                    }
                }
            } else {
                _list[index] = value;
                if (index < _countSorted) {
                    _countSorted = index;
                }
                if (_collectionChanged.hasObservers()) {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Setted, index, index,
                        new Object[] { oldItem }, new Object[] { value }));
                }
            }
            
        }
        return value;
    }

    //endregion

    //region Move

    public boolean Move(final int oldIndex, final int newIndex) {
        synchronized (_locker) {
            if (_autoSort) {
                Log.e(LOG_TAG, "Попытка переместить объект в сортированной коллекции");
                return false;
            }
            if (move(oldIndex, newIndex)) {
                if (oldIndex != newIndex && _collectionChanged.hasObservers()) {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Moved, oldIndex,
                        newIndex, null, new Object[] { _list[newIndex] }));
                }
                return true;
            }
            return false;
        }
    }
    
    private boolean move(final int oldIndex, int newIndex) {
        if (oldIndex == newIndex) { return true; }
        if (oldIndex < 0) {
            Log.e(LOG_TAG, "oldIndex < 0");
            return false;
        }
        if (oldIndex >= _count) {
            Log.e(LOG_TAG, "oldIndex >= count");
            return false;
        }
        if (newIndex < 0) {
            newIndex = 0;
        }
        if (newIndex >= _count) {
            newIndex = _count - 1;
        }
        final T moveObject = (T) _list[oldIndex];
        final int step = (newIndex > oldIndex) ? 1 : -1;
        for (int i = oldIndex; i != newIndex; i += step) {
            _list[i] = _list[i + step];
        }
        _list[newIndex] = moveObject;
        return true;
    }

    //endregion

    //region Sort

    private boolean _autoSort;

    @Override
    public boolean getAutoSort() {
        return _autoSort;
    }

    @Override
    public void setAutoSort(final boolean value) {
        _autoSort = value;
    }

    @Override
    public void ReSort() {
        synchronized (_locker) {
            _countSorted = 0;
            Sort();
        }
    }
    
    @Override
    public void Sort() {
        if (_countSorted == _count) { return; }
        synchronized (_locker) {
            if (_comparator == null) {
                Arrays.sort(_list, 0, _count);
            } else {
                Arrays.sort(_list, 0, _count, _comparator);
            }
            _countSorted = _count;
            if (_collectionChanged.hasObservers()) {
                OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Resorted, -1, -1, null,
                    null));
            }
        }
    }

    private int _countSorted;

    public int CountSorted() {
        synchronized (_locker) {
            return _countSorted;
        }
    }

    //endregion

    //region Copy

    @Override
    public void CopyTo(final T[] array, final int arrayIndex) {
        synchronized (_locker) {
            if (_autoSort) {
                this.Sort();
            }
            System.arraycopy(_list, 0, array, arrayIndex, _count);
        }
    }
    
    @Override
    public Object[] toArray() {
        synchronized (_locker) {
            if (_autoSort) {
                this.Sort();
            }
            return Arrays.copyOf(_list, _count);
        }
    }
    
    @Override
    public <T> T[] toArray(final Class<? extends T[]> classOfArrayT) {
        synchronized (_locker) {
            if (_autoSort) {
                this.Sort();
            }
            return Arrays.copyOf(_list, _count, classOfArrayT);
        }
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        if (a.length < _count) {
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(_list, _count, a.getClass());
        }
        System.arraycopy(_list, 0, a, 0, _count);
        if (a.length > _count) {
            a[_count] = null;
        }
        return a;
    }
    
    @Override
    public List<T> subList(final int start, final int end) {
        return new SortedList<T>(false, false, Arrays.copyOfRange(_list, start, end));
    }

    //endregion

    //region Iterator

    @Override
    public Iterator<T> iterator() {
        synchronized (_locker) {
            if (_autoSort) {
                Sort();
            }
            return new SortedListIterator<T>(_list, _count);
        }
    }

    @Override
    public ListIterator<T> listIterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<T> listIterator(final int location) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion

    //region CollectionChanged event

    private final Subject<CollectionChangedEventArgs, CollectionChangedEventArgs> _collectionChanged = PublishSubject.create();

    @Override
    public Observable<CollectionChangedEventArgs> CollectionChanged() {
        return _collectionChanged;
    }

    public boolean CollectionChangedHasObservers() {
        return _collectionChanged.hasObservers();
    }

    private final LinkedList<CollectionChangedEventArgs> _queueCollectionChanged = new LinkedList<CollectionChangedEventArgs>();
    private boolean _queueCollectionChangedHandling;

    private void OnCollectionChanged(final CollectionChangedEventArgs eventArgs) {
        _queueCollectionChanged.offer(eventArgs);
        if (_queueCollectionChangedHandling) { return; }
        _queueCollectionChangedHandling = true;
        while (_queueCollectionChanged.size() > 0) {
            final CollectionChangedEventArgs queueEventArgs = _queueCollectionChanged.poll();
            _collectionChanged.onNext(queueEventArgs);
        }
        _queueCollectionChangedHandling = false;
    }

    @Override
    public void OnItemModified(final int index) {
        synchronized (_locker) {
            OnItemModified(index, get(index));
        }
    }
    
    @Override
    public void OnItemModified(final int index, final T oldItem) {
        synchronized (_locker) {
            if (_collectionChanged.hasObservers()) {
//                if (_autoSort) {
//                    set(index, get(index));
//                } else {
                    OnCollectionChanged(new CollectionChangedEventArgs(this, CollectionChangedType.Setted, index, index,
                            new Object[] { oldItem }, new Object[] { get(index) }));
//                }
            }
        }
    }

    //endregion
}
