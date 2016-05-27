package com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.dto;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.Date;

public class InstagramPostDTO {

    public String[] tags;

    private PostType type;
    public enum PostType {
        @SerializedName("image")
        Image,

        @SerializedName("video")
        Video
    }

    public Location location;
    public class Location {
        public double latitude;
        public double longitude;
        public long id;
        public String name;
    }

    public Date created_time;
    public URI  link;

    public MediaSamples images;
    public MediaSamples videos;
    public class MediaSamples {
        public Media low_resolution;
        public Media thumbnail;
        public Media standard_resolution;
    }
    public class Media {
        public URI url;
        public int width;
        public int height;
    }

    public Caption caption;
    public class Caption {
        public String text;
    }

    public User user;
    public class User {
        public String username;
        public String full_name;
        public URI profile_picture;
        public long id;
    }
}
