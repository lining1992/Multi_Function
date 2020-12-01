package com.baidu.iov.dueros.test.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author v_lining05
 * @date 2020/8/31
 */

@Aspect
class SectionAspect {

    @Pointcut("")
    public void checkNetBehavior() {

    }

    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint joinPoint) {
        Log.d("TAG", "checkNet");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        signature.getMethod().getAnnotation(CheckNet.class);
        return null;
    }


}
