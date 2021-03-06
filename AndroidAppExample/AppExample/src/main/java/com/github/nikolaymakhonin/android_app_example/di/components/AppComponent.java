package com.github.nikolaymakhonin.android_app_example.di.components;


import com.github.nikolaymakhonin.android_app_example.di.modules.AppModule;
import com.github.nikolaymakhonin.android_app_example.presentation.common.EventBus;
import com.github.nikolaymakhonin.common_di.components.AppComponentBase;
import com.github.nikolaymakhonin.common_di.scopes.PerApplication;

import dagger.Component;

@PerApplication
@Component(dependencies = { ServiceComponent.class }, modules = { AppModule.class })
public interface AppComponent extends AppComponentBase, ServiceComponent
{
    EventBus getEventBus();
    InstagramComponent getInstagramComponent();
}

