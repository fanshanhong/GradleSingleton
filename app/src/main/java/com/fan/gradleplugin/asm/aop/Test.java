package com.fan.gradleplugin.asm.aop;


import com.fan.gradleplugin.asm.MyClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/6/10 10:12
 * @Modify:
 */
public class Test {
    public static void aopWithAsmTest() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        String fullName = MethodDemo.class.getName();
        String fullNameType = fullName.replace(".", "/");
        // ClassReader 的构造,可以传类全路径名, 也可以传入 bytes
        ClassReader cr = new ClassReader(fullNameType);
        ClassWriter cw = new ClassWriter(0);// 这个传入 0, 表示自己计算操作数栈和局部变量表的大小

        // 类适配器
        ClassAopMethodVisitor cv = new ClassAopMethodVisitor(cw);
        cr.accept(cv, ClassReader.SKIP_DEBUG);
        byte[] bytes = cw.toByteArray();

        MyClassLoader classLoader = new MyClassLoader();
        // 注意, ClassLoader的 defineClass 第一个参数要传入的fullName 是  com/xx/xx/XClass  这样的
        Class cls = classLoader.defineClass(fullName, bytes);

        // 反射调用
        Object demo = cls.newInstance();
        Method method = demo.getClass().getMethod("hello");
        method.invoke(demo);

        // 也可以写到一个新的文件中
        File file
                = new File("MethodDemo.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static void main(String[] args) throws Exception {
        aopWithAsmTest();
    }
}
