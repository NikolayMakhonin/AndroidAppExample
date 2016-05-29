package com.github.nikolaymakhonin.android_app_example.di.components;


import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.WhatsThereApi;
import com.github.nikolaymakhonin.android_app_example.di.modules.ServiceModule;
import com.github.nikolaymakhonin.common_di.components.ServiceComponentBase;
import com.github.nikolaymakhonin.common_di.scopes.PerService;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import dagger.Component;

@PerService
@Component(modules = { ServiceModule.class })
public interface ServiceComponent extends ServiceComponentBase
{
    WhatsThereApi getWhatsThereApi();
    OkHttpDownloader getOkHttpDownloader();
    Picasso getPicasso();
}
