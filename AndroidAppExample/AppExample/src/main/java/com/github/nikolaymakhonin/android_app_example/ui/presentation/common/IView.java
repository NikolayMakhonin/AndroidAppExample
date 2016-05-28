package com.github.nikolaymakhonin.android_app_example.ui.presentation.common;

import rx.Observable;

public interface IView<TPresenter> {

    boolean isAttached();

    Observable<Boolean> attachedObservable();

    TPresenter getPresenter();

    void setPresenter(TPresenter value);
}
