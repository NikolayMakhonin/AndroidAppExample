package com.github.nikolaymakhonin.utils.serialization;

public interface ReadItemAction {

    void call(BinaryReader r) throws Exception;

}
