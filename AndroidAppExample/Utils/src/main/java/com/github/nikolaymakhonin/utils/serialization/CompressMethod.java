package com.github.nikolaymakhonin.utils.serialization;

/** Метод сжатия */
public enum CompressMethod {
    /** Не сжимать */
    None,
    /** ! Используйте только для текстовых данных */
    @Deprecated
    TextDeflate,
    /** ! Используйте только для текстовых данных */
    @Deprecated
    TextGZip,
    /** Рекоммендую паковать этим. Пакует отлично бинарные данные. */
    @Deprecated
    BinaryZLib, 
    Zip
}
