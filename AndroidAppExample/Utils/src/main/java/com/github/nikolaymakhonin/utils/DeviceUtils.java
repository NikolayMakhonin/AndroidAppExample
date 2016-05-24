package com.github.nikolaymakhonin.utils;

import android.os.Build;

public final class DeviceUtils {

    private static final String[] _emulatorModels = { "genymotion", "emulator" };

    public static boolean isEmulator() {
        String model = Build.MODEL.toLowerCase();
        return ArrayUtils.indexOf(_emulatorModels, model) >= 0;
    }
}
