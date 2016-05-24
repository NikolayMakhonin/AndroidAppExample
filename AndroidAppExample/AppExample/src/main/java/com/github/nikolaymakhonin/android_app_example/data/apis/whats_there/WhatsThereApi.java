package com.github.nikolaymakhonin.android_app_example.data.apis.whats_there;

import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.entities.ResponseInstagram;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

//docs: http://square.github.io/retrofit/
public interface WhatsThereApi {
    @GET("media_search_by_location/{lat}&{lng}&{radius}&undefined")
    Observable<ResponseInstagram> getInstagramPostsByGeo(@Path("lat") double lat, @Path("lng") double lng, @Path("radius") int radiusInMeters);
}
