package com.github.nikolaymakhonin.utils.rx;

import com.github.nikolaymakhonin.utils.RefParam;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.util.InternalObservableUtils;
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
    
    //region replay with removable buffer

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the underlying Observable
     * that will replay all of its items and notifications to any future {@link Observer}. A Connectable
     * Observable resembles an ordinary Observable, except that it does not begin emitting items when it is
     * subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @return a {@link ConnectableObservableRemovable} that upon connection causes the source Observable to emit its
     *         items to its {@link Observer}s
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable) {
        return OperatorReplayRemovable.create(observable);
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on the items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable.
     * <p>
     * <img width="640" height="450" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.f.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            the selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @return an Observable that emits items that are the results of invoking the selector on a
     *         {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorReplayRemovable.multicastSelector(InternalObservableUtils.createReplaySupplier(observable), selector);
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     * replaying {@code bufferSize} notifications.
     * <p>
     * <img width="640" height="440" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.fn.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            the selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable
     *         replaying no more than {@code bufferSize} items
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize) {
        return OperatorReplayRemovable.multicastSelector(InternalObservableUtils.createReplaySupplier(observable, bufferSize), selector);
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     * replaying no more than {@code bufferSize} items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="445" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.fnt.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable, and
     *         replays no more than {@code bufferSize} items that were emitted within the window defined by
     *         {@code time}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(observable, selector, bufferSize, time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     * replaying no more than {@code bufferSize} items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="445" height="440" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.fnts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that is the time source for the window
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable, and
     *         replays no more than {@code bufferSize} items that were emitted within the window defined by
     *         {@code time}
     * @throws IllegalArgumentException
     *             if {@code bufferSize} is less than zero
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final long time, final TimeUnit unit, final Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        return OperatorReplayRemovable.multicastSelector(
            InternalObservableUtils.createReplaySupplier(observable, bufferSize, time, unit, scheduler), selector);
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     * replaying a maximum of {@code bufferSize} items.
     * <p>
     * <img width="640" height="440" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.fns.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param bufferSize
     *            the buffer size that limits the number of items the connectable observable can replay
     * @param scheduler
     *            the Scheduler on which the replay is observed
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     *         replaying no more than {@code bufferSize} notifications
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, final Func1<? super Observable<T>, ? extends Observable<R>> selector, final int bufferSize, final Scheduler scheduler) {
        return OperatorReplayRemovable.multicastSelector(InternalObservableUtils.createReplaySupplier(observable, bufferSize),
            InternalObservableUtils.createReplaySelectorAndObserveOn(selector, scheduler));
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     * replaying all items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="435" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.ft.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     *         replaying all items that were emitted within the window defined by {@code time}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit) {
        return replay(observable, selector, time, unit, Schedulers.computation());
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     * replaying all items that were emitted within a specified time window.
     * <p>
     * <img width="640" height="440" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.fts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the scheduler that is the time source for the window
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     *         replaying all items that were emitted within the window defined by {@code time}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, Func1<? super Observable<T>, ? extends Observable<R>> selector, final long time, final TimeUnit unit, final Scheduler scheduler) {
        return OperatorReplayRemovable.multicastSelector(
            InternalObservableUtils.createReplaySupplier(observable, time, unit, scheduler), selector);
    }

    /**
     * Returns an Observable that emits items that are the results of invoking a specified selector on items
     * emitted by a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable.
     * <p>
     * <img width="640" height="445" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.fs.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param <R>
     *            the type of items emitted by the resulting Observable
     * @param selector
     *            a selector function, which can use the multicasted sequence as many times as needed, without
     *            causing multiple subscriptions to the Observable
     * @param scheduler
     *            the Scheduler where the replay is observed
     * @return an Observable that emits items that are the results of invoking the selector on items emitted by
     *         a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable,
     *         replaying all items
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <R, T> Observable<R> replay(Observable<T> observable, final Func1<? super Observable<T>, ? extends Observable<R>> selector, final Scheduler scheduler) {
        return OperatorReplayRemovable.multicastSelector(
            InternalObservableUtils.createReplaySupplier(observable),
            InternalObservableUtils.createReplaySelectorAndObserveOn(selector, scheduler));
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable that
     * replays at most {@code bufferSize} items emitted by that Observable. A Connectable Observable resembles
     * an ordinary Observable, except that it does not begin emitting items when it is subscribed to, but only
     * when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.n.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items emitted by that Observable
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, final int bufferSize) {
        return OperatorReplayRemovable.create(observable, bufferSize);
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     * replays at most {@code bufferSize} items that were emitted during a specified time window. A Connectable
     * Observable resembles an ordinary Observable, except that it does not begin emitting items when it is
     * subscribed to, but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.nt.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items that were emitted during the window defined by
     *         {@code time}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, int bufferSize, long time, TimeUnit unit) {
        return replay(observable, bufferSize, time, unit, Schedulers.computation());
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     * that replays a maximum of {@code bufferSize} items that are emitted within a specified time window. A
     * Connectable Observable resembles an ordinary Observable, except that it does not begin emitting items
     * when it is subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.nts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the scheduler that is used as a time source for the window
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items that were emitted during the window defined by
     *         {@code time}
     * @throws IllegalArgumentException
     *             if {@code bufferSize} is less than zero
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, final int bufferSize, final long time, final TimeUnit unit, final Scheduler scheduler) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize < 0");
        }
        return OperatorReplayRemovable.create(observable, time, unit, scheduler, bufferSize);
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     * replays at most {@code bufferSize} items emitted by that Observable. A Connectable Observable resembles
     * an ordinary Observable, except that it does not begin emitting items when it is subscribed to, but only
     * when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.ns.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param bufferSize
     *            the buffer size that limits the number of items that can be replayed
     * @param scheduler
     *            the scheduler on which the Observers will observe the emitted items
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     *         replays at most {@code bufferSize} items that were emitted by the Observable
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, final int bufferSize, final Scheduler scheduler) {
        return OperatorReplayRemovable.observeOn(replay(observable, bufferSize), scheduler);
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     * replays all items emitted by that Observable within a specified time window. A Connectable Observable
     * resembles an ordinary Observable, except that it does not begin emitting items when it is subscribed to,
     * but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.t.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>This version of {@code replay} operates by default on the {@code computation} {@link Scheduler}.</dd>
     * </dl>
     *
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     *         replays the items that were emitted during the window defined by {@code time}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, long time, TimeUnit unit) {
        return replay(observable, time, unit, Schedulers.computation());
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     * replays all items emitted by that Observable within a specified time window. A Connectable Observable
     * resembles an ordinary Observable, except that it does not begin emitting items when it is subscribed to,
     * but only when its {@code connect} method is called. 
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.ts.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param time
     *            the duration of the window in which the replayed items must have been emitted
     * @param unit
     *            the time unit of {@code time}
     * @param scheduler
     *            the Scheduler that is the time source for the window
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable and
     *         replays the items that were emitted during the window defined by {@code time}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, final long time, final TimeUnit unit, final Scheduler scheduler) {
        return OperatorReplayRemovable.create(observable, time, unit, scheduler);
    }

    /**
     * Returns a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable that
     * will replay all of its items and notifications to any future {@link Observer} on the given
     * {@link Scheduler}. A Connectable Observable resembles an ordinary Observable, except that it does not
     * begin emitting items when it is subscribed to, but only when its {@code connect} method is called.
     * <p>
     * <img width="640" height="515" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/replay.s.png" alt="">
     * <dl>
     *  <dt><b>Backpressure Support:</b></dt>
     *  <dd>This operator supports backpressure. Note that the upstream requests are determined by the child
     *  Subscriber which requests the largest amount: i.e., two child Subscribers with requests of 10 and 100 will
     *  request 100 elements from the underlying Observable sequence.</dd>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>you specify which {@link Scheduler} this operator will use</dd>
     * </dl>
     *
     * @param scheduler
     *            the Scheduler on which the Observers will observe the emitted items
     * @return a {@link ConnectableObservableRemovable} that shares a single subscription to the source Observable that
     *         will replay all of its items and notifications to any future {@link Observer} on the given
     *         {@link Scheduler}
     * @see <a href="http://reactivex.io/documentation/operators/replay.html">ReactiveX operators documentation: Replay</a>
     */
    public static <T> ConnectableObservableRemovable<T> replay(Observable<T> observable, final Scheduler scheduler) {
        return OperatorReplayRemovable.observeOn(replay(observable), scheduler);
    }
    
    //endregion
}
