package com.github.nikolaymakhonin.common_di.components;

import com.github.nikolaymakhonin.common_di.modules.app.AppModuleBase;
import com.github.nikolaymakhonin.common_di.scopes.PerApplication;

import dagger.Component;

@Component(dependencies = {ServiceComponentBase.class}, modules = { AppModuleBase.class })
@PerApplication
public interface AppComponentBase extends ServiceComponentBase {

}

