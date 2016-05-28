package com.github.nikolaymakhonin.utils.contracts.patterns.mvp;

import rx.Observable;

public interface IView<TPresenter, TViewModel> {

    boolean isAttached();

    Observable<Boolean> attachedObservable();

    TPresenter getPresenter();

    void setPresenter(TPresenter value);

    void updateView(TViewModel viewModel);
}
