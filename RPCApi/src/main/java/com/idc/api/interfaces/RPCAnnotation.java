package com.idc.api.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RPCAnnotation {
    // 对外发布服务的接口
    Class<?> value();

    // 版本,用于区分
    String version() default "";
}
