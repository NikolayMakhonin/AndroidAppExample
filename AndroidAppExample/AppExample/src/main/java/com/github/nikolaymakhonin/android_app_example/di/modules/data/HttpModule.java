package com.github.nikolaymakhonin.android_app_example.di.modules.data;

import android.content.Context;

import com.github.nikolaymakhonin.common_di.scopes.PerService;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import dagger.Provides;

public class HttpModule {

    @Provides
    @PerService
    public static Picasso getPicasso(Context applicationContext, OkHttpDownloader okHttpDownloader) {
        Picasso.Builder builder = new Picasso.Builder(applicationContext);
        builder.downloader(okHttpDownloader);
        Picasso instance = builder.build();
        instance.setIndicatorsEnabled(true);
        instance.setLoggingEnabled(true);
        return instance;
    }

    @Provides
    @PerService
    public static OkHttpDownloader getOkHttpDownloader(Context applicationContext) {
        return new OkHttpDownloader(applicationContext, Integer.MAX_VALUE);
    }

}
