package com.github.nikolaymakhonin.android_app_example.di.factories.instagram;

import android.view.ViewGroup;

import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramPostPresenter;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.views.IInstagramPostView;
import com.github.nikolaymakhonin.android_app_example.ui.views.InstagramPostView;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IRecyclerViewAdapterFactory;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.ISinglePresenter;

public class InstagramListFactory implements IRecyclerViewAdapterFactory<
    IInstagramPostView,
    InstagramPost,
    ISinglePresenter<IInstagramPostView, InstagramPost>>
{
    @Override
    public IInstagramPostView createItemView(ViewGroup parent, int viewType) {
        return new InstagramPostView(parent.getContext());
    }

    @Override
    public ISinglePresenter<IInstagramPostView, InstagramPost> createItemPresenter(IInstagramPostView view,
        int viewType)
    {
        InstagramPostPresenter presenter = new InstagramPostPresenter();
        presenter.setView(view);
        return presenter;
    }

    @Override
    public int getItemViewType(int position, InstagramPost instagramPost) {
        return 0;
    }
}
