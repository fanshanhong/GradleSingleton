package com.fan.gradleplugin.asm.aop;

/**
 * 切面类
 */
public class MethodAspect {

    public static void beforeMethod() {
        System.out.println("方式二:方法开始运行...");
    }

    public static void afterMethod() {
        System.out.println("方式二333:方法运行结束...");
    }

}
