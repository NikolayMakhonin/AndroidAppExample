package com.github.nikolaymakhonin.utils.contracts.patterns.mvp;

import android.view.ViewGroup;

import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;

public interface IRecyclerViewAdapterFactory<
    TView extends IView,
    TItem extends ITreeModified,
    TPresenter extends ISinglePresenter<TView, TItem>>
{

    TView createItemView(ViewGroup parent, int viewType);

    TPresenter createItemPresenter(TView view, int viewType);

    int getItemViewType(int position, TItem instagramPost);

}
