package com.github.nikolaymakhonin.utils.rx;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.RefParam;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class DynamicObservablesMerger<T>  {

    private final ConnectableObservableRemovable<? extends Observable<T>> _modifiedObservablesBuffer;

    @SafeVarargs
    public DynamicObservablesMerger(Observable<T>... baseAttachedObservables) {
        _modifiedObservables = PublishSubject.create();
        _modifiedObservablesBuffer = RxOperators.replay(_modifiedObservables);
        //There is no need to unsubscribe:
        _modifiedObservablesBuffer.connect();

        _treeModifiedSubject = Observable
            .merge(_modifiedObservablesBuffer);

        for (Observable<T> baseAttachedObservable : baseAttachedObservables) {
            attach(baseAttachedObservable);
        }
    }

    private final Subject<Observable<T>, ? extends Observable<T>> _modifiedObservables;

    private final Observable<T> _treeModifiedSubject;

    /**
     * Return detach func
     */
    public Action0 attach(Observable<T> modifiedObservable) {
        RefParam<Action0> doComplete = new RefParam<>();

        Observable<T> completable = RxOperators.toCompletable(modifiedObservable, doComplete);

        //bind modifiedObservable to _treeModifiedSubject
        _modifiedObservables.onNext(completable);

        return () -> {
            doComplete.value.call();
            _modifiedObservablesBuffer.remove(o -> CompareUtils.EqualsObjects(o, modifiedObservable));
        };
    }

    public Observable<T> observable() {
        return _treeModifiedSubject;
    }
}
