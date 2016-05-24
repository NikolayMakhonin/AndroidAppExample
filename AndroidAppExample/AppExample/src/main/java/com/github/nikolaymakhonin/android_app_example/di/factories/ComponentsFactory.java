package com.github.nikolaymakhonin.android_app_example.di.factories;


import android.content.Context;
import android.support.annotation.NonNull;

import com.github.nikolaymakhonin.android_app_example.di.components.AppComponent;
import com.github.nikolaymakhonin.android_app_example.di.components.DaggerAppComponent;
import com.github.nikolaymakhonin.android_app_example.di.components.DaggerServiceComponent;
import com.github.nikolaymakhonin.android_app_example.di.components.ServiceComponent;
import com.github.nikolaymakhonin.common_di.modules.service.ServiceModuleBase;

public final class ComponentsFactory {

    public static AppComponent buildAppComponent(@NonNull Context appContext) {

        ServiceComponent serviceComponent = buildServiceComponent(appContext);

        AppComponent appComponent = DaggerAppComponent.builder()
            .serviceComponent(serviceComponent)
            .build();

        return appComponent;
    }

    public static ServiceComponent buildServiceComponent(@NonNull Context appContext) {
        ServiceComponent serviceComponent = DaggerServiceComponent.builder()
            .serviceModuleBase(new ServiceModuleBase(appContext))
            .build();

        return serviceComponent;
    }
}
