package com.yin.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yin.permission_request.NeedPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Permission.with(this).check(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionCallback() {
//            @Override
//            public void permissionGranted() {
//                Log.e("aaaaaaaaaaaaaa", "permissionGranted");
//            }
//
//            @Override
//            public void permissionDenied(int[] grantResults) {
//                Log.e("aaaaaaaaaaaaaa", "permissionDenied" + Arrays.toString(grantResults));
//            }
//        });
//        Util.afterPermission(this);
        afterPermission();
    }

    @NeedPermission({Manifest.permission.CAMERA})
    public void afterPermission() {
        Log.e("aaaaaaaaaaaaaa", "afterPermission");
    }
}
