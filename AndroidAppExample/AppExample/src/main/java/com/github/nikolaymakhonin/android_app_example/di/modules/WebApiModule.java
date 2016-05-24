package com.github.nikolaymakhonin.android_app_example.di.modules;

import com.github.nikolaymakhonin.android_app_example.data.apis.converters.CombineConverterFactory;
import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.WhatsThereApi;
import com.github.nikolaymakhonin.common_di.scopes.PerService;
import com.github.nikolaymakhonin.logger.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class WebApiModule {

    private static final String LOG_TAG = "WebApiModule";

    @Provides
    @PerService
    @Named("WhatsThereApiGson")
    public static Gson getWhatsThereApiGson() {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        //see: http://stackoverflow.com/questions/5671373/unparseable-date-1302828677828-trying-to-deserialize-with-gson-a-millisecond
        builder
            .registerTypeAdapter(Date.class,
                (JsonDeserializer<Date>) (json, typeOfT, context)
                    -> new Date(json.getAsJsonPrimitive().getAsLong()));
//            .registerTypeAdapter(URI.class,
//                (JsonDeserializer<URI>) (json, typeOfT, context) -> {
//                    try {
//                        return new URI(json.getAsString());
//                    } catch (URISyntaxException e) {
//                        throw new RuntimeException(e);
//                    }
//                });

        Gson gson = builder.create();

        return gson;
    }

    @Provides
    @PerService
    @Named("WhatsThereApiRetrofit")
    public static Retrofit getWhatsThereApiRetrofit(@Named("WhatsThereApiGson") Gson gson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.whatsthere.co/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(new CombineConverterFactory(GsonConverterFactory.create(gson),
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

        return retrofit;
    }

    @Provides
    @PerService
    public static WhatsThereApi getWhatsThereApi(@Named("WhatsThereApiRetrofit") Retrofit retrofit) {
        WhatsThereApi api = retrofit.create(WhatsThereApi.class);
        return api;
    }

}
