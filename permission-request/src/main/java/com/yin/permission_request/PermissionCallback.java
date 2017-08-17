package com.yin.permission_request;

/**
 * Created by yin on 2017/8/15.
 */

public interface PermissionCallback {
    void permissionGranted();

    /**
     *
     * @param grantResults -1:拒绝 0:同意
     */
    void permissionDenied(int[] grantResults);
}
