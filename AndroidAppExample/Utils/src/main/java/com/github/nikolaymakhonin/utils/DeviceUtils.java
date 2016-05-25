package com.github.nikolaymakhonin.utils;

import android.os.Build;

import org.apache.commons.lang3.ArrayUtils;

public final class DeviceUtils {

    private static final String[] _emulatorModels = { "genymotion", "emulator" };

    public static boolean isEmulator() {
        String model = Build.MODEL.toLowerCase();
        return ArrayUtils.indexOf(_emulatorModels, model) >= 0;
    }
}
