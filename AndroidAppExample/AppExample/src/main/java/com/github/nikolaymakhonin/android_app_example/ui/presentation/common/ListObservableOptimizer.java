package com.github.nikolaymakhonin.android_app_example.ui.presentation.common;

import android.support.annotation.NonNull;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedEventArgs;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedType;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChangedList;
import com.github.nikolaymakhonin.utils.rx.DynamicObservablesMerger;
import com.github.nikolaymakhonin.utils.rx.RxOperators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class ListObservableOptimizer<TItem> {

    private final Scheduler _observeOnScheduler;
    private final Scheduler _subscribeOnScheduler;
    private final long _deferredTimeOut;
    private final TimeUnit _deferredTimeOutUnit;

    public ListObservableOptimizer() {
        this(Schedulers.computation(), AndroidSchedulers.mainThread(), 250, TimeUnit.MILLISECONDS);
    }

    public ListObservableOptimizer(Scheduler observeOnScheduler, Scheduler subscribeOnScheduler) {
        this(observeOnScheduler, subscribeOnScheduler, 250, TimeUnit.MILLISECONDS);
    }

    public ListObservableOptimizer(long deferredTimeOut, TimeUnit deferredTimeOutUnit) {
        this(Schedulers.computation(), AndroidSchedulers.mainThread(), deferredTimeOut, deferredTimeOutUnit);
    }

    public ListObservableOptimizer(
        Scheduler observeOnScheduler,
        Scheduler subscribeOnScheduler,
        long deferredTimeOut,
        TimeUnit deferredTimeOutUnit)
    {
        _observeOnScheduler = observeOnScheduler;
        _subscribeOnScheduler = subscribeOnScheduler;
        _deferredTimeOut = deferredTimeOut;
        _deferredTimeOutUnit = deferredTimeOutUnit;
    }

    //region Properties

    private final Subject<CollectionChangedEventArgs, CollectionChangedEventArgs> _propertyChangeSubject = PublishSubject.create();

    protected final DynamicObservablesMerger _collectionChangedMerger = new DynamicObservablesMerger(_propertyChangeSubject);

    private final Object _locker = new Object();

    //region Source

    private ICollectionChangedList<TItem> _source;

    private Action0 _sourceUnBindFunc;

    public ICollectionChangedList<TItem> getSource() {
        return _source;
    }

    public void setSource(ICollectionChangedList<TItem> value) {
        if (CompareUtils.EqualsObjects(_source, value)) {
            return;
        }
        synchronized (_locker) {
            if (_sourceUnBindFunc != null) {
                _sourceUnBindFunc.call();
                _sourceUnBindFunc = null;
            }
            _source = value;
            if (_source != null) {
                _sourceUnBindFunc = _collectionChangedMerger.attach(_source.CollectionChanged());
            }
        }
        _propertyChangeSubject.onNext(createResortEventArgs(value));
    }

    //endregion

    //endregion

    private final List<CollectionChangedEventArgs> _collectionChangedQueue = new ArrayList<>();

    public Observable observable() {
        return _collectionChangedMerger.observable()
            .doOnNext((Action1<CollectionChangedEventArgs>) e -> {
                synchronized (_locker) {
                    if (_collectionChangedQueue.size() != 1
                        || _collectionChangedQueue.get(0).getChangedType() != CollectionChangedType.Resorted)
                    {
                        if (e.getChangedType() == CollectionChangedType.Resorted) {
                            _collectionChangedQueue.clear();
                        }
                        if (_collectionChangedQueue.size() * 2 > ((Collection) e.getObject()).size()) {
                            _collectionChangedQueue.clear();
                            e = createResortEventArgs(e.getObject());
                        }
                        _collectionChangedQueue.add(e);
                    }
                }
            })
            .map(o -> null)
            .lift(RxOperators.deferred(250, TimeUnit.MILLISECONDS))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap((Func1<CollectionChangedEventArgs, Observable<CollectionChangedEventArgs>>) e -> {
                synchronized (_locker) {
                    Observable<CollectionChangedEventArgs> result = Observable.from(
                        _collectionChangedQueue.toArray(new CollectionChangedEventArgs[_collectionChangedQueue.size()]));
                    _collectionChangedQueue.clear();
                    return result;
                }
            });
    }

    @NonNull
    private CollectionChangedEventArgs createResortEventArgs(Object list) {
        return new CollectionChangedEventArgs(
            list,
            CollectionChangedType.Resorted,
            -1, -1, null,  null);
    }
}
