package com.fan.gradleplugin.asm.aop;


/**
 * 演示使用 ASM 实现 AOP 增强的例子
 *
 * 原始类
 */
public class MethodDemo {


    /**
     * 想要在 hello 方法的开始和结束添加一些东西
     */
    public void hello() {
        System.out.println("hello asm method!");
    }
}