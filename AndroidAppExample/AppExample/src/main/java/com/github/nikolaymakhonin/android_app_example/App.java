package com.github.nikolaymakhonin.android_app_example;

import android.support.multidex.MultiDexApplication;

import com.github.nikolaymakhonin.android_app_example.di.components.AppComponent;
import com.github.nikolaymakhonin.android_app_example.di.factories.ComponentsFactory;
import com.github.nikolaymakhonin.common_di.contracts.IHasAppComponentBase;
import com.github.nikolaymakhonin.logger.Log;

public class App extends MultiDexApplication implements IHasAppComponentBase<AppComponent> {

    //region DI

    private AppComponent _appComponent;

    @Override
    public AppComponent getAppComponent() {
        return _appComponent;
    }

    //endregion

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("App", "onCreate");

        Thread.setDefaultUncaughtExceptionHandler(Log.uncaughtExceptionHandler);

        _appComponent = ComponentsFactory.buildAppComponent(this);

        _appComponent.initFabric();
    }
}
