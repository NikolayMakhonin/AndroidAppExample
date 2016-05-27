package com.github.nikolaymakhonin.android_app_example.di.components;


import com.github.nikolaymakhonin.android_app_example.di.modules.AppModule;
import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.EventBus;
import com.github.nikolaymakhonin.common_di.components.AppComponentBase;
import com.github.nikolaymakhonin.common_di.scopes.PerApplication;

import dagger.Component;

@Component(dependencies = { ServiceComponent.class }, modules = { AppModule.class })
@PerApplication
public interface AppComponent extends AppComponentBase, ServiceComponent
{
    EventBus getEventBus();
}

