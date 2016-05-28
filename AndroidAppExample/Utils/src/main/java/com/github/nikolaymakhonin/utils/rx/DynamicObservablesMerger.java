package com.github.nikolaymakhonin.utils.rx;

import com.github.nikolaymakhonin.utils.RefParam;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class DynamicObservablesMerger<T>  {

    @SafeVarargs
    public DynamicObservablesMerger(Observable<T>... baseAttachedObservables) {
        for (Observable<T> baseAttachedObservable : baseAttachedObservables) {
            attach(baseAttachedObservable);
        }
    }

    private final Subject<Observable<T>, ? extends Observable<T>> _modifiedObservables = PublishSubject.create();

    private final Observable<T> _treeModifiedSubject = Observable
        .merge((Observable<? extends Observable<T>>) _modifiedObservables);

    /**
     * Return detach func
     */
    public Action0 attach(Observable<T> modifiedObservable) {
        RefParam<Action0> doComplete = new RefParam<>();

        Observable<T> completable = RxOperators.toCompletable(modifiedObservable, doComplete);

        //bind modifiedObservable to _treeModifiedSubject
        _modifiedObservables.onNext(completable);

        return doComplete.value;
    }

    public Observable<T> observable() {
        return _treeModifiedSubject;
    }
}
