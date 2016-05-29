package com.github.nikolaymakhonin.common_di.components;

import android.content.Context;

import com.github.nikolaymakhonin.common_di.modules.service.ServiceModuleBase;
import com.github.nikolaymakhonin.common_di.scopes.PerService;

import dagger.Component;
import io.fabric.sdk.android.Fabric;

@PerService
@Component(modules = { ServiceModuleBase.class })
public interface ServiceComponentBase {
    Context getApplicationContext();
    /** Run it in application onCreate if you want initialize Fabric */
    Fabric initFabric();
}

