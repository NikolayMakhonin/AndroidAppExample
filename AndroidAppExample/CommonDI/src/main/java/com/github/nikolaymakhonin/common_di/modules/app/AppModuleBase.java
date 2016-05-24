package com.github.nikolaymakhonin.common_di.modules.app;

import android.content.Context;
import android.view.LayoutInflater;

import com.github.nikolaymakhonin.common_di.scopes.PerApplication;

import dagger.Module;
import dagger.Provides;

@Module()
public class AppModuleBase {

    @Provides
    @PerApplication
    public LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
