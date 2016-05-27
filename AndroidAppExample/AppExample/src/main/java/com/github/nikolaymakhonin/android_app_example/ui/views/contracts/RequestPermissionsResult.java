package com.github.nikolaymakhonin.android_app_example.ui.views.contracts;

import android.content.pm.PackageManager;

public class RequestPermissionsResult {

    public final int requestCode;
    public final String[] permissions;
    public final int[] grantResults;

    public RequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    public final boolean isGranted() {
        int count = permissions.length;
        for (int i = 0; i < count; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
