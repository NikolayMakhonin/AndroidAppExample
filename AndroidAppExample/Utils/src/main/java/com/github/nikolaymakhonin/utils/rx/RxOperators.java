package com.github.nikolaymakhonin.utils.rx;

import com.github.nikolaymakhonin.utils.RefParam;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public final class RxOperators {

    public static <T> OperatorDeferredWithTime<T> deferred(long timeout, TimeUnit unit) {
        return deferred(timeout, unit, Schedulers.computation());
    }

    public static <T> OperatorDeferredWithTime<T> deferred(long timeout, TimeUnit unit, Scheduler scheduler) {
        return new OperatorDeferredWithTime<>(timeout, unit, scheduler);
    }

    public static <T> Observable<T> toCompletable(Observable<T> observable, RefParam<Action0> outCompleteAction) {
        boolean[] completed     = new boolean[1];
        Subject   forceCompleteSubject = PublishSubject.create();

        //Convert to completable
        Observable completable = Observable.merge(observable, forceCompleteSubject).takeUntil(o -> completed[0]);

        outCompleteAction.value = () -> {
            //bindObservable will completed before next emit
            completed[0] = true;
            //Force complete bindObservable
            forceCompleteSubject.onNext(null);
        };

        return completable;
    }
}
