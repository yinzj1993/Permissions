package com.yin.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yin.permission_request.AsyncPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Permission.with(this).request(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionCallback() {
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
//        Permission.with(this).request(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION});
//        Util.afterPermission(this);
//        Log.e("aaaaaaaaaa", String.valueOf(afterPermission()));
        afterPermission();
    }

    @AsyncPermission(value = {Manifest.permission.CAMERA}, deniedToast = "Denied")
    public void afterPermission() {
        Log.e("aaaaaaaaaaaaaa", "afterPermission");
    }
}
