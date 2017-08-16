package com.yin.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yin.permission_request.Permission;
import com.yin.permission_request.PermissionCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permission.with(this).check(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                Log.e("aaaaaaaaaaaaaa", "permissionGranted");
            }

            @Override
            public void permissionDenied(int[] grantResults) {
                Log.e("aaaaaaaaaaaaaa", "permissionDenied");
            }
        });
    }
}
