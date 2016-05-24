package com.github.nikolaymakhonin.android_app_example.frontend.contracts;

import rx.Observable;

public interface IActivityPermissionsCallback {
    Observable<RequestPermissionsResult> getRequestPermissionsObservable();
}
