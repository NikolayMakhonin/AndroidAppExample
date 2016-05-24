package com.github.nikolaymakhonin.common_di.modules.service;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.nikolaymakhonin.common_di.scopes.PerService;

import dagger.Module;
import dagger.Provides;

@Module(includes = { FabricModule.class })
public class ServiceModuleBase {

    // You can not store a global link to other contexts, in order to avoid memory leaks
    private final Context _applicationContext;

    public ServiceModuleBase(@NonNull Context anyContext) {
        _applicationContext = anyContext.getApplicationContext();
    }

    @Provides
    @PerService
    public Context getApplicationContext() {
        return _applicationContext;
    }

    @Provides
    @PerService
    public NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
