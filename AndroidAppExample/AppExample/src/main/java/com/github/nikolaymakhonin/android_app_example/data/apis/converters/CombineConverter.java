package com.github.nikolaymakhonin.android_app_example.data.apis.converters;

import java.io.IOException;

import retrofit2.Converter;

public class CombineConverter<T1, T2, T3> implements Converter<T1, T3> {

    private final Converter<T1, T2> _first;
    private final Converter<T2, T3> _second;

    public CombineConverter(Converter<T1, T2> first, Converter<T2, T3> second) {

        _first = first;
        _second = second;
    }

    @Override
    public T3 convert(T1 value) throws IOException {
        return _second.convert(_first.convert(value));
    }
}
