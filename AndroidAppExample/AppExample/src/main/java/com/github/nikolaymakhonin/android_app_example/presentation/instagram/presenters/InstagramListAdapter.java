package com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters;

import com.github.nikolaymakhonin.android_app_example.utils.patterns.mvp.BaseRecyclerViewAdapter;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IRecyclerViewAdapterFactory;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.ISinglePresenter;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.views.IInstagramPostView;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChangedList;

public class InstagramListAdapter extends BaseRecyclerViewAdapter<
    IInstagramPostView,
    InstagramPost,
    ICollectionChangedList<InstagramPost>,
    ISinglePresenter<IInstagramPostView, InstagramPost>>
{
    public InstagramListAdapter(
        IRecyclerViewAdapterFactory<IInstagramPostView, InstagramPost, ISinglePresenter<IInstagramPostView, InstagramPost>> factory)
    {
        super(factory);
    }
}
