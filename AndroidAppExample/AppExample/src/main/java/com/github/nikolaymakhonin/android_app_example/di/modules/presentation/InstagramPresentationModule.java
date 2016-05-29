package com.github.nikolaymakhonin.android_app_example.di.modules.presentation;

import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.WhatsThereApi;
import com.github.nikolaymakhonin.android_app_example.di.factories.instagram.InstagramListFactory;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramListAdapter;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.views.IInstagramPostView;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IRecyclerViewAdapterFactory;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.ISinglePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class InstagramPresentationModule {

    @Provides
    public static InstagramListAdapter createInstagramListAdapter(
        IRecyclerViewAdapterFactory<IInstagramPostView, InstagramPost, ISinglePresenter<IInstagramPostView, InstagramPost>> factory,
        WhatsThereApi whatsThereApi)
    {
        return new InstagramListAdapter(factory, whatsThereApi);
    }

    @Provides
    public static IRecyclerViewAdapterFactory<IInstagramPostView, InstagramPost, ISinglePresenter<IInstagramPostView, InstagramPost>> createInstagramListFactory() {
        return new InstagramListFactory();
    }

}
