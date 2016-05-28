package com.github.nikolaymakhonin.android_app_example.di.modules;

import com.github.nikolaymakhonin.android_app_example.presentation.common.EventBus;
import com.github.nikolaymakhonin.common_di.modules.app.AppModuleBase;
import com.github.nikolaymakhonin.common_di.scopes.PerApplication;

import dagger.Module;
import dagger.Provides;

@Module(includes = { AppModuleBase.class })
public class AppModule {

    @Provides
    @PerApplication
    public EventBus getEventBus() {
        return new EventBus();
    }

}
