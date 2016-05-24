package com.github.nikolaymakhonin.common_di.contracts;

import com.github.nikolaymakhonin.common_di.components.AppComponentBase;

public interface IHasAppComponentBase<T extends AppComponentBase> {

    T getAppComponent();

}
