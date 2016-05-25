package com.github.nikolaymakhonin.utils.serialization;

public interface IStreamSerializable {

    void Serialize(BinaryWriter writer) throws Exception;
    
    Object DeSerialize(BinaryReader reader) throws Exception;

}
