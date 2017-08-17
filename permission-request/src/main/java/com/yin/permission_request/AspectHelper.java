package com.yin.permission_request;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by yin on 2017/8/16.
 */

@Aspect
public class AspectHelper {

    @After("execution(void android.support.v4.app.FragmentActivity.onRequestPermissionsResult(..))")
    public void onRequestPermissionsResult(JoinPoint joinPoint) throws Throwable {
        if (Permission.INSTANCE() != null) {
            Permission.INSTANCE().onRequestPermissionsResult((int) joinPoint.getArgs()[0], (String[]) joinPoint.getArgs()[1], (int[]) joinPoint.getArgs()[2]);
        }
    }

    @After("execution(void android.support.v4.app.FragmentActivity.onActivityResult(..))")
    public void onActivityResult(JoinPoint joinPoint) throws Throwable {
        if (Permission.INSTANCE() != null && (int) joinPoint.getArgs()[0] > 9000 && (int) joinPoint.getArgs()[0] < 9004) {
            Permission.INSTANCE().onActivityResult((int) joinPoint.getArgs()[0], (int) joinPoint.getArgs()[1], (Intent) joinPoint.getArgs()[2]);
        }
    }

    @Pointcut("execution(@com.yin.permission_request.AsyncPermission * *(..)) && @annotation(asyncPermission)")
    public void needPermission(AsyncPermission asyncPermission) {
    }

    @Around("needPermission(asyncPermission)")
    public void onRequestPermissionMethod(final ProceedingJoinPoint proceedingJoinPoint, final AsyncPermission asyncPermission) throws Throwable {
        if (asyncPermission == null || asyncPermission.value() == null || asyncPermission.value().length == 0) {
            proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }
        Activity activity = null;
        if (proceedingJoinPoint.getThis() instanceof Activity) {
            activity = (Activity) proceedingJoinPoint.getThis();
        } else if (proceedingJoinPoint.getThis() instanceof Fragment) {
            activity = ((Fragment) proceedingJoinPoint.getThis()).getActivity();
        } else {
            for (int i = 0; i < proceedingJoinPoint.getArgs().length; i++) {
                if (proceedingJoinPoint.getArgs()[i] instanceof Activity) {
                    activity = (Activity) proceedingJoinPoint.getArgs()[i];
                } else if (proceedingJoinPoint.getArgs()[i] instanceof Fragment) {
                    activity = ((Fragment) proceedingJoinPoint.getArgs()[i]).getActivity();
                }
            }
        }
        if (activity != null) {
            final Activity finalActivity = activity;
            Permission.with(activity).request(asyncPermission.value(), new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    try {
                        proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
                    } catch (Throwable throwable) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (asyncPermission.deniedToast() != null && asyncPermission.deniedToast().length() != 0) {
                                    Toast.makeText(finalActivity, asyncPermission.deniedToast(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void permissionDenied(int[] grantResults) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (asyncPermission.deniedToast() != null && asyncPermission.deniedToast().length() != 0) {
                                Toast.makeText(finalActivity, asyncPermission.deniedToast(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}
