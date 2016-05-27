package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.factories;

import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.dto.InstagramPostDTO;
import com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.entities.InstagramPost;

public class InstagramFactory {

    public static InstagramPost whatsThereGsonToDTO(InstagramPostDTO data) {
        InstagramPost viewModel = new InstagramPost();

        return viewModel;
    }

}
