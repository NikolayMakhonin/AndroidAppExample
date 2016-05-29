package com.github.nikolaymakhonin.android_app_example.ui.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.github.nikolaymakhonin.logger.Log;

import com.github.nikolaymakhonin.android_app_example.ui.contracts.IActivityPermissionsCallback;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class PermissionsHelper {

    private static final String LOG_TAG = "PermissionHelper";
    private static int _nextRequestCode;

    /** Activity must implement {@link IActivityPermissionsCallback} */
    public static Observable<Boolean> requestPermissions(Activity activity, String[] permissions) {

        //Get not granted permissions
        int count = permissions.length;
        List<String> requestPermissions = new ArrayList<>(count);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
                break;
            }
        }

        if (requestPermissions.size() == 0) {
            //All permissions already granted
            return Observable.just(true);
        } else {
            if (!(activity instanceof IActivityPermissionsCallback)) {
                Log.e(LOG_TAG, "Activity is not implement IActivityPermissionsCallback");
                return Observable.just(false);
            }

            final int requestCode = _nextRequestCode++;

            Observable<Boolean> resultObservable = ((IActivityPermissionsCallback)activity).getRequestPermissionsObservable()
                .filter(requestResult -> requestResult.requestCode == requestCode)
                .first()
                .map(requestResult -> requestResult.isGranted())
                .replay(1);

            ActivityCompat.requestPermissions(
                activity,
                requestPermissions.toArray(new String[requestPermissions.size()]),
                requestCode);

            return resultObservable;
        }
    }
}
