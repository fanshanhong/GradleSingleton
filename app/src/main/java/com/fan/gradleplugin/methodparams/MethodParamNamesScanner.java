package com.fan.gradleplugin.methodparams;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * 据说Java8已经原生支持参数名读取了。具体不是很清楚。本文以java7为例进行说明.
 * 通过ASM字节码操作工具我们可以实现运行时参数名的读写。
 * 简单说说原理：java字节码为每个方法保存了一份方法本地变量列表。可以通过ASM获取这个列表。但是可能会获得列表顺序与期望的不一致。我们获取的本地变量了列表使用不同的编译器编译得到的顺序可能不同。一般而言，通过javac编译出来的列表顺序是按照本地变量使用的顺序。而我们期望的是声明的顺序。如下面这个方法:
 *
 * public String handle(String a,String b){
 *     String c;
 *     c="str";
 *     return a+b;
 * }
 * 得到的localVariable列表顺序:我们期望的是this,a,b,c.而实际上读取到的会是this ,c ,a,b
 * 这个this是非静态方法中的第一个本地变量。所以我们会在下面的代码中对这个顺序进行重排序以解决该问题。
 */
public class MethodParamNamesScanner {

    /**
     * 获取方法参数名列表
     *
     * @param clazz
     * @param m
     * @return
     * @throws IOException
     */
    public static List<String> getMethodParamNames(Class<?> clazz, Method m) throws IOException {
        try (InputStream in = clazz.getResourceAsStream("/" + clazz.getName().replace('.', '/') + ".class")) {
            return getMethodParamNames(in, m);
        }

    }

    public static List<String> getMethodParamNames(InputStream in, Method m) throws IOException {
        try (InputStream ins = in) {
            return getParamNames(ins,
                    new EnclosingMetadata(m.getName(), Type.getMethodDescriptor(m), m.getParameterTypes().length));
        }

    }

    /**
     * 获取构造器参数名列表
     *
     * @param clazz
     * @param constructor
     * @return
     */
    public static List<String> getConstructorParamNames(Class<?> clazz, Constructor<?> constructor) {
        try (InputStream in = clazz.getResourceAsStream("/" + clazz.getName().replace('.', '/') + ".class")) {
            return getConstructorParamNames(in, constructor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public static List<String> getConstructorParamNames(InputStream ins, Constructor<?> constructor) {
        try (InputStream in = ins) {
            return getParamNames(in, new EnclosingMetadata(constructor.getName(), Type.getConstructorDescriptor(constructor),
                    constructor.getParameterTypes().length));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ArrayList<String>();
    }

    /**
     * 获取参数名列表辅助方法
     *
     * @param in
     * @param m
     * @return
     * @throws IOException
     */
    private static List<String> getParamNames(InputStream in, EnclosingMetadata m) throws IOException {
        ClassReader cr = new ClassReader(in);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.EXPAND_FRAMES);// 建议EXPAND_FRAMES
        // ASM树接口形式访问
        List<MethodNode> methods = cn.methods;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < methods.size(); ++i) {
            List<LocalVariable> varNames = new ArrayList<LocalVariable>();
            MethodNode method = methods.get(i);
            // 验证方法签名
            if (method.desc.equals(m.desc) && method.name.equals(m.name)) {
                System.out.println("desc->"+method.desc+"       "+m.desc);
                List<LocalVariableNode> local_variables = method.localVariables;
                for (int l = 0; l < local_variables.size(); l++) {
                    String varName = local_variables.get(l).name;
                    // index-记录了正确的方法本地变量索引。(方法本地变量顺序可能会被打乱。而index记录了原始的顺序)
                    int index = local_variables.get(l).index;
                    //if (!"this".equals(varName)) // 非静态方法,第一个参数是this
                        varNames.add(new LocalVariable(index, varName));
                }
                LocalVariable[] tmpArr = varNames.toArray(new LocalVariable[varNames.size()]);
                // 根据index来重排序，以确保正确的顺序
                Arrays.sort(tmpArr);
                for (int j = 0; j < tmpArr.length; j++) {
                    list.add(tmpArr[j].name);
                }
                break;

            }

        }
        return list;
    }

    /**
     * 方法本地变量索引和参数名封装
     *
     * @author xby Administrator
     */
    static class LocalVariable implements Comparable<LocalVariable> {
        public int index;
        public String name;

        public LocalVariable(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public int compareTo(LocalVariable o) {
            return this.index - o.index;
        }
    }

    /**
     * 封装方法描述和参数个数
     *
     * @author xby Administrator
     */
    static class EnclosingMetadata {
        //method name
        public String name;
        // method description
        public String desc;
        // params size
        public int size;

        public EnclosingMetadata(String name, String desc, int size) {
            this.name = name;
            this.desc = desc;
            this.size = size;
        }
    }

    public static void main(String[] args) throws IOException {
        for (Method m : AdminController.class.getDeclaredMethods()) {
            List<String> list = getMethodParamNames(AdminController.class, m);
            System.out.println(m.getName() + ":");
            for (String str : list) {
                System.out.println(str);
            }
            System.out.println("------------------------");
        }
    }
}