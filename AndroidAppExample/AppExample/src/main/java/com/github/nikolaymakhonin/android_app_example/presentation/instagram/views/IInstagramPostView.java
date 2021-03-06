package com.github.nikolaymakhonin.android_app_example.presentation.instagram.views;

import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IView;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramPostPresenter;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;

public interface IInstagramPostView extends IView<InstagramPostPresenter, InstagramPost> {

}
