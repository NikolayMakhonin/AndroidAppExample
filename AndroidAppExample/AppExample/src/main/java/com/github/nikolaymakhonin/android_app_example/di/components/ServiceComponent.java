package com.github.nikolaymakhonin.android_app_example.di.components;


import com.github.nikolaymakhonin.android_app_example.core.EventBus;
import com.github.nikolaymakhonin.android_app_example.di.modules.ServiceModule;
import com.github.nikolaymakhonin.common_di.components.ServiceComponentBase;
import com.github.nikolaymakhonin.common_di.scopes.PerService;

import dagger.Component;

@Component(modules = { ServiceModule.class })
@PerService
public interface ServiceComponent extends ServiceComponentBase
{
    EventBus getEventBus();
}
