package com.github.nikolaymakhonin.android_app_example.di.modules;

import com.github.nikolaymakhonin.android_app_example.data.apis.converters.CombineConverterFactory;
import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.WhatsThereApi;
import com.github.nikolaymakhonin.common_di.scopes.PerApplication;
import com.github.nikolaymakhonin.common_di.scopes.PerService;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class WebApiModule {

    @Provides
    @PerService
    public static WhatsThereApi getWhatsThereApi() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://www.whatsthere.co/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(new CombineConverterFactory(
                GsonConverterFactory.create(),
                null,
                responseBody -> {
                    //Unescape response string
                    String content = responseBody.string();
                    content = StringEscapeUtils.unescapeEcmaScript(content);
                    //trim spaces and quotes
                    content = StringUtils.strip(content, " \r\n\t\"'");
                    return ResponseBody.create(responseBody.contentType(), content);
                }))
            .build();
        WhatsThereApi api = retrofit.create(WhatsThereApi.class);
        return api;
    }
}
