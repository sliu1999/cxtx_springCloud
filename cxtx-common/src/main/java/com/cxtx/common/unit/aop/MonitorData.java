package com.cxtx.common.unit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检测第三方接口的注解
 * in 进数据的接口
 * out 出数据的接口
 * apiPath 接口apiPath
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitorData {
    String apiPath() default "";

    String type() default "in";


}
