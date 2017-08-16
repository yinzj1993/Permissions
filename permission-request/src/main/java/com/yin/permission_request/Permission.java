package com.yin.permission_request;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

/**
 * Created by yin on 2017/8/15.
 */
public class Permission {

    public static final int CODE_REQUEST_SETTING = -900;
    public static final int CODE_REQUEST_WRITE_SETTING = -901;
    public static final int CODE_REQUEST_SYSTEM_ALERT_WINDOW = -902;

    private static Permission INSTANCE;
    private static AppCompatActivity sActivity;
    private SparseArray<PermissionCallback> mCallbacks = new SparseArray<>();

    private Permission() {
    }

    public synchronized static Permission with(AppCompatActivity activity) {
        if (INSTANCE == null) {
            INSTANCE = new Permission();
        }
        sActivity = activity;
        return INSTANCE;
    }

    static Permission INSTANCE() {
        return INSTANCE;
    }

    /**
     * 特殊权限需要单独申请
     *
     * @param permissions
     * @param callback
     */
    public void check(String[] permissions, PermissionCallback callback) {
        if (permissions == null || callback == null || (permissions != null && permissions.length == 0)) {
            return;
        }
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.SYSTEM_ALERT_WINDOW.equals(permissions[i]) || Manifest.permission.WRITE_SETTINGS.equals(permissions[i])) {
                throw new IllegalArgumentException("特殊权限需要单独申请");
            }
        }
        if (!PermissionUtils.isOverMarshmallow() || PermissionUtils.hasSelfPermissions(sActivity, permissions)) {
            callback.permissionGranted();
        } else {
            mCallbacks.put(mCallbacks.size(), callback);
            ActivityCompat.requestPermissions(sActivity, permissions, mCallbacks.indexOfValue(callback));
        }
    }

    public void check(String permission, PermissionCallback callback) {
        if (permission == null || callback == null) {
            return;
        }
        switch (permission) {
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                if (!PermissionUtils.isOverMarshmallow() || PermissionUtils.hasSelfPermissions(sActivity, permission) || PermissionUtils.canDrawOverlays(sActivity)) {
                    callback.permissionGranted();
                } else {
                    mCallbacks.put(CODE_REQUEST_SYSTEM_ALERT_WINDOW, callback);
                    alertWindowPermission();
                }
                break;
            case Manifest.permission.WRITE_SETTINGS:
                if (!PermissionUtils.isOverMarshmallow() || PermissionUtils.hasSelfPermissions(sActivity, permission) || PermissionUtils.canWriteSetting(sActivity)) {
                    callback.permissionGranted();
                } else {
                    mCallbacks.put(CODE_REQUEST_WRITE_SETTING, callback);
                    writeSettingsPermission();
                }
                break;
            default:
                if (!PermissionUtils.isOverMarshmallow() || PermissionUtils.hasSelfPermissions(sActivity, permission)) {
                    callback.permissionGranted();
                } else {
                    mCallbacks.put(mCallbacks.size(), callback);
                    ActivityCompat.requestPermissions(sActivity, new String[]{permission}, mCallbacks.indexOfValue(callback));
                }
                break;
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.verifyPermissions(grantResults)) {
            mCallbacks.get(requestCode).permissionGranted();
        } else {
            mCallbacks.get(requestCode).permissionDenied(grantResults);
        }
        mCallbacks.remove(requestCode);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_REQUEST_SETTING:

                break;
            case CODE_REQUEST_SYSTEM_ALERT_WINDOW:
                if (PermissionUtils.canDrawOverlays(sActivity)) {
                    mCallbacks.get(requestCode).permissionGranted();
                } else {
                    mCallbacks.get(requestCode).permissionDenied(new int[]{0});
                }
                mCallbacks.remove(requestCode);
                break;
            case CODE_REQUEST_WRITE_SETTING:
                if (PermissionUtils.canWriteSetting(sActivity)) {
                    mCallbacks.get(requestCode).permissionGranted();
                } else {
                    mCallbacks.get(requestCode).permissionDenied(new int[]{0});
                }
                mCallbacks.remove(requestCode);
                break;

            default:
                break;

        }
    }

    private void alertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + sActivity.getPackageName()));
        sActivity.startActivityForResult(intent, CODE_REQUEST_SYSTEM_ALERT_WINDOW);
    }

    private void writeSettingsPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + sActivity.getPackageName()));
        sActivity.startActivityForResult(intent, CODE_REQUEST_WRITE_SETTING);
    }
}
