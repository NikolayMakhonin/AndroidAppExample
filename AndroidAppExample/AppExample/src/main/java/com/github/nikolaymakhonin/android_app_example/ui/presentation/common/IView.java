package com.github.nikolaymakhonin.android_app_example.ui.presentation.common;

import rx.Observable;

public interface IView {

    boolean isAttached();

    Observable<Boolean> attachedObservable();

}
