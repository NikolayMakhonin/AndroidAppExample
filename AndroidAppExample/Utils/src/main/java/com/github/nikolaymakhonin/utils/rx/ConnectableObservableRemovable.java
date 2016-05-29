package com.github.nikolaymakhonin.utils.rx;

import com.android.internal.util.Predicate;

import rx.observables.ConnectableObservable;

public abstract class ConnectableObservableRemovable<T> extends ConnectableObservable<T> {
    protected ConnectableObservableRemovable(OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
    }

    public abstract void remove(Predicate<T> predicate);
}
