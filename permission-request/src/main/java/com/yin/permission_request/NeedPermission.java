package com.yin.permission_request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yin on 2017/8/16.
 * 注解要在activity或Fragment中使用，如果不是需要被注解的函数的第一个参数类型为activity或Fragment
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {
    String[] value() default "";
}
