package com.github.nikolaymakhonin.utils.contracts.patterns.mvp;

import com.github.nikolaymakhonin.utils.contracts.patterns.BaseTreeModified;
import com.github.nikolaymakhonin.utils.serialization.ISerializableModified;

public abstract class BaseViewModel extends BaseTreeModified implements ISerializableModified {

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

