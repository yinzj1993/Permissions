package com.yin.permission_request;

/**
 * Created by yin on 2017/8/15.
 */

public interface PermissionCallback {
    void permissionGranted();

    void permissionDenied(int[] grantResults);
}
