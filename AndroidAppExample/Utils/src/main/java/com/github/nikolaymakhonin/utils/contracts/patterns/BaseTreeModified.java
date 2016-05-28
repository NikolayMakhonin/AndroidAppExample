package com.github.nikolaymakhonin.utils.contracts.patterns;

import com.github.nikolaymakhonin.utils.RefParam;
import com.github.nikolaymakhonin.utils.rx.RxOperators;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public abstract class BaseTreeModified implements ITreeModified {

    protected final Object _propertySetLocker = new Object();

    //region Modified events

    private final Subject _modifiedSubject = PublishSubject.create();

    @Override
    public Subject Modified() {
        return _modifiedSubject;
    }

    private final Subject<Observable, Observable> _modifiedObservables = PublishSubject.create();
    private final Observable                      _treeModifiedSubject = Observable.merge((Observable<? extends Observable<?>>) _modifiedObservables);

    /** Return unBind func */
    protected Action0 bindToTreeModified(Observable modifiedObservable) {
        RefParam<Action0> doComplete = new RefParam<>();

        Observable completable = RxOperators.toCompletable(modifiedObservable, doComplete);

        //bind modifiedObservable to _treeModifiedSubject
        _modifiedObservables.onNext(completable);

        return doComplete.value;
    }

    @Override
    public Observable TreeModified() {
        return _treeModifiedSubject;
    }

    //endregion

}
