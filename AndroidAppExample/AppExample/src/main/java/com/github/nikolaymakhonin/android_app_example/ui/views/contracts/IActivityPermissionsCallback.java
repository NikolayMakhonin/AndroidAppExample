package com.github.nikolaymakhonin.android_app_example.ui.views.contracts;

import rx.Observable;

public interface IActivityPermissionsCallback {
    Observable<RequestPermissionsResult> getRequestPermissionsObservable();
}
