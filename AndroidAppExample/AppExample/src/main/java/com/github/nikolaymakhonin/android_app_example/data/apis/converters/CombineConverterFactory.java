package com.github.nikolaymakhonin.android_app_example.data.apis.converters;

import com.github.nikolaymakhonin.logger.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CombineConverterFactory extends Converter.Factory
{
    private static final String LOG_TAG = "CombineConverterFactory";

    private final Converter.Factory _converterFactory;
    private final Converter<RequestBody, RequestBody> _requestConverter;
    private final Converter<ResponseBody, ResponseBody> _responseConverter;

    public CombineConverterFactory(Converter.Factory converterFactory,
        Converter<RequestBody, RequestBody> requestConverterClass,
        Converter<ResponseBody, ResponseBody> responseConverterClass)
    {
        _converterFactory = converterFactory;
        _requestConverter = requestConverterClass;
        _responseConverter = responseConverterClass;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
        Annotation[] methodAnnotations, Retrofit retrofit)
    {
        Converter<?, RequestBody> first = _converterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);

        if (_requestConverter == null) {
            return first;
        }

        Converter<RequestBody, RequestBody> second;
        try {
            second = _requestConverter;
        } catch (Exception e) {
            Log.e(LOG_TAG, null, e);
            return null;
        }

        return new CombineConverter<>(first, second);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Converter<ResponseBody, ?> second = _converterFactory.responseBodyConverter(type, annotations, retrofit);

        if (_responseConverter == null) {
            return second;
        }

        Converter<ResponseBody, ResponseBody> first;
        try {
            first = _responseConverter;
        } catch (Exception e) {
            Log.e(LOG_TAG, null, e);
            return null;
        }

        return new CombineConverter<>(first, second);
    }
}
