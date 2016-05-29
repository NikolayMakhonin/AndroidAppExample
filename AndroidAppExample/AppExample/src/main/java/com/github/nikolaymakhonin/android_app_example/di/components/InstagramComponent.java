package com.github.nikolaymakhonin.android_app_example.di.components;

import com.github.nikolaymakhonin.android_app_example.di.modules.presentation.InstagramPresentationModule;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramListAdapter;

import dagger.Subcomponent;

@Subcomponent(modules = InstagramPresentationModule.class)
public interface InstagramComponent {

    InstagramListAdapter createInstagramListAdapter();

}
