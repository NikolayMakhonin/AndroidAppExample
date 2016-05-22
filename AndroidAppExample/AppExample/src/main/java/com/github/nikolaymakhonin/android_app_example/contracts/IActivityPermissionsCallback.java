package com.github.nikolaymakhonin.android_app_example.contracts;

import rx.Observable;

public interface IActivityPermissionsCallback {
    Observable<RequestPermissionsResult> getRequestPermissionsObservable();
}
