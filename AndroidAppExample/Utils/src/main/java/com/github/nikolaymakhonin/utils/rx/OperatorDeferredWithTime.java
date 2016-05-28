package com.github.nikolaymakhonin.utils.rx;

import java.util.concurrent.TimeUnit;

import rx.Observable.Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * This operation filters out events which are published too quickly in succession. This is done by dropping events which are
 * followed up by other events before a specified timer has expired. If the timer expires and no follow up event was published (yet)
 * the last received event is published.
 *
 * @param <T> the value type
 */
public final class OperatorDeferredWithTime<T> implements Operator<T, T> {
    final long      timeout;
    final TimeUnit  unit;
    final Scheduler scheduler;

    /**
     * @param timeout
     *            How long each event has to be the 'last event' before it gets published.
     * @param unit
     *            The unit of time for the specified timeout.
     * @param scheduler
     *            The {@link Scheduler} to use internally to manage the timers which handle timeout for each event.
     *
     */
    public OperatorDeferredWithTime(long timeout, TimeUnit unit, Scheduler scheduler) {
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final Worker        worker = scheduler.createWorker();
        final SerializedSubscriber<T> s      = new SerializedSubscriber<>(child);
        final SerialSubscription      ssub   = new SerialSubscription();

        s.add(worker);
        s.add(ssub);

        return new Subscriber<T>(child) {
            final DebounceState<T> state = new DebounceState<>();
            final Subscriber<?> self = this;
            private long _lastEmitTime;
            private final Object _locker = new Object();

            @Override
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(final T t) {
                synchronized (_locker) {
                    final int index = state.next(t);
                    final long lastEmitTime = _lastEmitTime;

                    long emitTimeout = timeout - (System.currentTimeMillis() - _lastEmitTime);
                    if (_lastEmitTime == 0 || emitTimeout < 1) {
                        //emit immediately
                        ssub.set(Subscriptions.empty());
                        state.emit(index, s, self);
                        _lastEmitTime = System.currentTimeMillis();
                    } else {
                        //set timer
                        ssub.set(worker.schedule(() -> {
                            synchronized (_locker) {
                                if (lastEmitTime == _lastEmitTime) {
                                    state.emit(index, s, self);
                                    _lastEmitTime = System.currentTimeMillis();
                                }
                            }
                        }, emitTimeout, unit));
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
                state.clear();
            }

            @Override
            public void onCompleted() {
                state.emitAndComplete(s, this);
            }
        };
    }
    /**
     * Tracks the last value to be emitted and manages completion.
     * @param <T> the value type
     */
    static final class DebounceState<T> {
        /** Guarded by this. */
        int index;
        /** Guarded by this. */
        T value;
        /** Guarded by this. */
        boolean hasValue;
        /** Guarded by this. */
        boolean terminate;
        /** Guarded by this. */
        boolean emitting;

        public synchronized int next(T value) {
            this.value = value;
            hasValue = true;
            return ++index;
        }
        public void emit(int index, Subscriber<T> onNextAndComplete, Subscriber<?> onError) {
            T localValue;
            synchronized (this) {
                if (emitting || !hasValue || index != this.index) {
                    return;
                }
                localValue = value;

                value = null;
                hasValue = false;
                emitting = true;
            }

            try {
                onNextAndComplete.onNext(localValue);
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, onError, localValue);
                return;
            }

            // Check if a termination was requested in the meantime.
            synchronized (this) {
                if (!terminate) {
                    emitting = false;
                    return;
                }
            }

            onNextAndComplete.onCompleted();
        }
        public void emitAndComplete(Subscriber<T> onNextAndComplete, Subscriber<?> onError) {
            T localValue;
            boolean localHasValue;

            synchronized (this) {
                if (emitting) {
                    terminate = true;
                    return;
                }
                localValue = value;
                localHasValue = hasValue;

                value = null;
                hasValue = false;

                emitting = true;
            }

            if (localHasValue) {
                try {
                    onNextAndComplete.onNext(localValue);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, onError, localValue);
                    return;
                }
            }
            onNextAndComplete.onCompleted();
        }
        public synchronized void clear() {
            ++index;
            value = null;
            hasValue = false;
        }
    }
}