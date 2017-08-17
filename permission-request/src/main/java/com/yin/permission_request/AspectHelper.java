package com.yin.permission_request;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

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

    @Pointcut("execution(@com.yin.permission_request.NeedPermission * *(..)) && @annotation(needPermission)")
    public void needPermission(NeedPermission needPermission) {
    }

    @Around("needPermission(needPermission)")
    public void onRequestPermissionMethod(final ProceedingJoinPoint proceedingJoinPoint, NeedPermission needPermission) throws Throwable {
        if (needPermission == null) return;
        Activity activity = null;
        if (proceedingJoinPoint.getThis() instanceof Activity) {
            activity = (Activity) proceedingJoinPoint.getThis();
        } else if (proceedingJoinPoint.getThis() instanceof Fragment) {
            activity = ((Fragment) proceedingJoinPoint.getThis()).getActivity();
        } else if (proceedingJoinPoint.getArgs()[0] instanceof Activity) {
            activity = (Activity) proceedingJoinPoint.getArgs()[0];
        } else if (proceedingJoinPoint.getArgs()[0] instanceof Fragment) {
            activity = ((Fragment) proceedingJoinPoint.getArgs()[0]).getActivity();
        }
        if (activity != null) {
            Permission.with(activity).check(needPermission.value(), new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    try {
                        proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
                    } catch (Throwable throwable) {
                    }
                }

                @Override
                public void permissionDenied(int[] grantResults) {

                }
            });
        }
    }
}
