package com.yin.permission_request;

import android.content.Intent;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

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
        if (Permission.INSTANCE() != null && (int) joinPoint.getArgs()[0] > 899 && (int) joinPoint.getArgs()[0] < 903) {
            Permission.INSTANCE().onActivityResult((int) joinPoint.getArgs()[0], (int) joinPoint.getArgs()[1], (Intent) joinPoint.getArgs()[2]);
        }
    }
}
