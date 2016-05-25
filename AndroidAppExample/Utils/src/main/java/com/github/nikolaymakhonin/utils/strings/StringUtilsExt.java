package com.github.nikolaymakhonin.utils.strings;

import android.util.Base64;

public class StringUtilsExt {

    public static String toBase64(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT), CharsetUtils.US_ASCII);
    }

    public static byte[] fromBase64(String base64String) {
        return Base64.decode(base64String.getBytes(CharsetUtils.US_ASCII), Base64.DEFAULT);
    }

}
