package com.github.nikolaymakhonin.android_app_example.di.modules;

import com.github.nikolaymakhonin.android_app_example.core.EventBus;
import com.github.nikolaymakhonin.common_di.modules.service.ServiceModuleBase;
import com.github.nikolaymakhonin.common_di.scopes.PerService;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
    ServiceModuleBase.class,
    WebApiModule.class
})
public class ServiceModule {

    @Provides
    @PerService
    public EventBus getEventBus() {
        return new EventBus();
    }

}
