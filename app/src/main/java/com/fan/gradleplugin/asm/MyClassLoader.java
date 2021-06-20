package com.fan.gradleplugin.asm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 使用生成的类
 * <p>
 * （1）生成 class 文件
 * <p>
 * 先前的字节数组可以存储在Comparable.class文件中，以备将来使用。
 * <p>
 * 或者，可以使用ClassLoader动态加载它。
 * <p>
 * （2）定义自己的 ClassLoader
 * <p>
 * 一种方法是定义一个classLoader子类，该类的defineClass方法是公共的：
 */
public class MyClassLoader extends ClassLoader {
    public Class defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

    public static void main(String[] args) {

        try {
            Class<?> aClass = Class.forName("pkg.Comparable");
            System.out.println(aClass);
        } catch (ClassNotFoundException e) {
            System.out.println("使用 Class.forName 未找到 pkg.Comparable");
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(new File("Comparable.class"));

            int len = -1;
            byte[] bytes = new byte[512];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((len = fileInputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }

            byteArrayOutputStream.flush();

            // 使用 defineClass 将.class 文件的流转成 Class 对象
            //  defineClass方法接受一组字节，然后将其具体化为一个Class类型实例，它一般从磁盘上加载一个文件，然后将文件的字节传递给JVM，通过JVM（native 方法）对于Class的定义，将其具体化，实例化为一个Class类型实例。
            //
            //    getParent方法返回其parent ClassLoader。
            //
            //    getResource和getResources方法，从给定的repository中查找URLs，同时它们也具备类似loadClass一样的代理机制，我们可以将loadClass视为：defineClass(getResource(name).getBytes())。
            Class<?> aClass = new MyClassLoader().defineClass(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            System.out.println(aClass);

            //new MyClassLoader().defineClass(getResource(name).getBytes())

//            Class<?> aClass1 = new MyClassLoader().loadClass("Comparable");
//            System.out.println(aClass1);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
