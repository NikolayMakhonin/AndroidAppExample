package com.github.nikolaymakhonin.android_app_example.di.modules;

import com.github.nikolaymakhonin.android_app_example.di.modules.data.HttpModule;
import com.github.nikolaymakhonin.android_app_example.di.modules.data.WebApiModule;
import com.github.nikolaymakhonin.common_di.modules.service.ServiceModuleBase;

import dagger.Module;

@Module(includes = {
    ServiceModuleBase.class,
    HttpModule.class,
    WebApiModule.class
})
public class ServiceModule {

}
