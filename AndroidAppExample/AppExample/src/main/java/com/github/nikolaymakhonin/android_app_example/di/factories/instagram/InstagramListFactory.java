package com.github.nikolaymakhonin.android_app_example.di.factories.instagram;

import android.view.ViewGroup;

import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramPostPresenter;
import com.github.nikolaymakhonin.android_app_example.ui.views.InstagramPostView;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IRecyclerViewAdapterFactory;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.ISinglePresenter;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IView;

public class InstagramListFactory implements IRecyclerViewAdapterFactory {

    @Override
    public IView createItemView(ViewGroup parent, int viewType) {
        return new InstagramPostView(parent.getContext());
    }

    @Override
    public ISinglePresenter createItemPresenter(IView view, int viewType) {
        return new InstagramPostPresenter();
    }

    @Override
    public int getItemViewType(int position, ITreeModified instagramPost) {
        return 0;
    }
}
