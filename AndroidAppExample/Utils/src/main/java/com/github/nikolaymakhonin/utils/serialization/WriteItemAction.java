package com.github.nikolaymakhonin.utils.serialization;

public interface WriteItemAction<TItem> {

    void call(BinaryWriter w, TItem o) throws Exception;

}
