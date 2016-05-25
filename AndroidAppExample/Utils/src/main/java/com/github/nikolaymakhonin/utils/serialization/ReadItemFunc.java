package com.github.nikolaymakhonin.utils.serialization;

public interface ReadItemFunc<TItem> {
    TItem invoke(BinaryReader r) throws Exception;
}
