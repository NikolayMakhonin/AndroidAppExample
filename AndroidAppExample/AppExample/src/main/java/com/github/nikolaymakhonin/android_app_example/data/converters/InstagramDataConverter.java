package com.github.nikolaymakhonin.android_app_example.data.converters;

import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.entities.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.data.dto.instagram.InstagramPostDTO;

public class InstagramDataConverter {

    public static InstagramPostDTO whatsThereGsonToDTO(InstagramPost data) {
        InstagramPostDTO dto = new InstagramPostDTO();

        return dto;
    }

}
