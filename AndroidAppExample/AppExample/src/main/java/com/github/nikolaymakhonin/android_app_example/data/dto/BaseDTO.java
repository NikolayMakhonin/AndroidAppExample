package com.github.nikolaymakhonin.android_app_example.data.dto;

import com.github.nikolaymakhonin.utils.serialization.ISerializableModified;

import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public abstract class BaseDTO implements ISerializableModified {

    //region Modified event

    private final Subject _modifiedSubject = PublishSubject.create();

    @Override
    public Subject Modified() {
        return _modifiedSubject;
    }

    //endregion

    //region Serialization

    //    private static final int _currentVersion = 0;
    //
    //    @Override
    //    public void Serialize(BinaryWriter writer) throws Exception {
    //        writer.write(_currentVersion);
    //    }
    //
    //    @Override
    //    public Object DeSerialize(BinaryReader reader) throws Exception {
    //        int version = reader.readInt();
    //
    //        return this;
    //    }

    //endregion


}
