package com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.dto;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.Date;

/*
Generated from:
http://www.whatsthere.co/media_search_by_location/1.303473052305062&103.83716053955072&1000&undefined
by jsonschema2pojo plugin

{
    "meta": {"code": 200},
    "data": [
        {
            "video_views": 0,
            "videos": {
                "low_resolution": {
                    "url": "https://scontent.cdninstagram.com/t50.2886-16/13303525_794591604004451_495212733_s.mp4",
                    "width": 480,
                    "height": 480
                },
                "standard_resolution": {
                    "url": "https://scontent.cdninstagram.com/t50.2886-16/13327020_794574640678699_875692578_n.mp4",
                    "width": 640,
                    "height": 640
                },
                "low_bandwidth": {
                    "url": "https://scontent.cdninstagram.com/t50.2886-16/13303525_794591604004451_495212733_s.mp4",
                    "width": 480,
                    "height": 480
                }
            },
            "tags": ["hongkrew"],
            "type": "video",
            "location": {
                "latitude": 22.2809,
                "name": "Hong Kong",
                "longitude": 114.178,
                "id": 214424288
            },
            "comments": {
                "count": 0,
            },
            "created_time": "1464481657",
            "link": "https://www.instagram.com/p/BF-JU3RiVUX/",
            "likes": {
                "count": 0,
            },
            "images": {
                "low_resolution": {
                    "url": "https://scontent.cdninstagram.com/t51.2885-15/s320x320/e15/13285229_1776789085899719_2029322790_n.jpg?ig_cache_key=MTI2MDQ4NTk2MTg2ODQ2NTQzMQ%3D%3D.2",
                    "width": 320,
                    "height": 320
                },
                "thumbnail": {
                    "url": "https://scontent.cdninstagram.com/t51.2885-15/s150x150/e15/13285229_1776789085899719_2029322790_n.jpg?ig_cache_key=MTI2MDQ4NTk2MTg2ODQ2NTQzMQ%3D%3D.2",
                    "width": 150,
                    "height": 150
                },
                "standard_resolution": {
                    "url": "https://scontent.cdninstagram.com/t51.2885-15/e15/13285229_1776789085899719_2029322790_n.jpg?ig_cache_key=MTI2MDQ4NTk2MTg2ODQ2NTQzMQ%3D%3D.2",
                    "width": 640,
                    "height": 640
                }
            },
            "caption": {
                "text": "Ordering a Diet Coke in Hong Kong is kind of a big deal.",
            },
            "id": "1260485961868465431_11515596",
            "user": {
                "username": "noahreich",
                "profile_picture": "https://scontent.cdninstagram.com/t51.2885-19/11809964_1477182719244845_753802528_a.jpg",
                "id": "11515596",
                "full_name": "Noah Reich"
            }
        }
    ]
}
 */

public class InstagramSearchResponse {

    @SerializedName("meta")
    public Meta   meta;
    @SerializedName("data")
    public Post[] data;

    public static class Meta {
        @SerializedName("code")
        public int code;
    }

    public static class Post {
        @SerializedName("video_views")
        public int          videoViews;
        @SerializedName("videos")
        public MediaSamples videos;
        @SerializedName("type")
        public PostType     type;
        @SerializedName("location")
        public Location     location;
        @SerializedName("comments")
        public Comments     comments;
        @SerializedName("created_time")
        public Date         createdTime;
        @SerializedName("link")
        public URI          link;
        @SerializedName("likes")
        public Comments     likes;
        @SerializedName("images")
        public MediaSamples images;
        @SerializedName("caption")
        public Caption      caption;
        @SerializedName("id")
        public String       id;
        @SerializedName("user")
        public User         user;
        @SerializedName("tags")
        public String[]     tags;

        public enum PostType {
            @SerializedName("image")
            Image,

            @SerializedName("video")
            Video
        }

        public static class MediaSamples {
            @SerializedName("low_resolution")
            public Media lowResolution;
            @SerializedName("standard_resolution")
            public Media standardResolution;
            @SerializedName("low_bandwidth")
            public Media lowBandwidth;

            public static class Media {
                @SerializedName("url")
                public URI url;
                @SerializedName("width")
                public int width;
                @SerializedName("height")
                public int height;
            }
        }

        public static class Location {
            @SerializedName("latitude")
            public double latitude;
            @SerializedName("name")
            public String name;
            @SerializedName("longitude")
            public double longitude;
            @SerializedName("id")
            public int    id;
        }

        public static class Comments {
            @SerializedName("count")
            public int count;
        }

        public static class Caption {
            @SerializedName("text")
            public String text;
        }

        public static class User {
            @SerializedName("username")
            public String username;
            @SerializedName("profile_picture")
            public String profilePicture;
            @SerializedName("id")
            public String id;
            @SerializedName("full_name")
            public String fullName;
        }
    }
}