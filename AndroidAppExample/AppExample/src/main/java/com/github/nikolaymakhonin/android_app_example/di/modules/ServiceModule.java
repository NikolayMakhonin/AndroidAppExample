package com.github.nikolaymakhonin.android_app_example.di.modules;

import com.github.nikolaymakhonin.common_di.modules.service.ServiceModuleBase;

import dagger.Module;

@Module(includes = {
    ServiceModuleBase.class,
    WebApiModule.class
})
public class ServiceModule {

}
