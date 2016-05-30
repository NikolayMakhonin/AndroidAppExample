package com.github.nikolaymakhonin.utils.strings;

import android.util.Base64;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtilsExt {

    public static String toBase64(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT), CharsetUtils.US_ASCII);
    }

    public static byte[] fromBase64(String base64String) {
        return Base64.decode(base64String.getBytes(CharsetUtils.US_ASCII), Base64.DEFAULT);
    }

    public static String throwableToString(final Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static boolean isNullOrEmpty(final String str) {
        return str == null || str.length() == 0;
    }

}
