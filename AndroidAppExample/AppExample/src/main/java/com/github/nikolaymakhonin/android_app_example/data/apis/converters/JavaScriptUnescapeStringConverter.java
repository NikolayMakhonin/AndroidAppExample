package com.github.nikolaymakhonin.android_app_example.data.apis.converters;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class JavaScriptUnescapeStringConverter implements Converter<ResponseBody, ResponseBody> {

    @Override
    public ResponseBody convert(ResponseBody responseBody) throws IOException {
        String content = responseBody.string();
        content = StringEscapeUtils.unescapeEcmaScript(content);
        return ResponseBody.create(responseBody.contentType(), content);
    }
}
