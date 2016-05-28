package com.github.nikolaymakhonin.android_app_example.presentation.common;

import com.github.nikolaymakhonin.utils.contracts.patterns.IDisposable;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;

public interface ISinglePresenter<TView extends IView, TViewModel extends ITreeModified>
    extends ITreeModified, IDisposable
{
    TView getView();

    void setView(TView value);

    TViewModel getViewModel();

    void setViewModel(TViewModel value);

    boolean isAllowBindView();

    void setAllowBindView(boolean value);

    boolean isViewBind();
}
