package com.fan.gradleplugin.methodparams;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: ParameterNameASM
 * @Description: 用字节码工具ASM来直接获取参数名称
 *
 * 在java8中要获取类函数参数名称必须在编译时增加 -parameters 参数
 */
public abstract class ParameterNameASM {

    public void fun(String name1, int age1) {
        System.out.println("name:" + name1 + ",age:" + String.valueOf(age1));
    }

    public abstract void test1(String msg, int age);

    public abstract void test2(String msg, int age, Object other);

    public static void main(String[] args) throws Exception {
        Method method = ParameterNameASM.class.getDeclaredMethod("fun", String.class, int.class);
        System.out.println("fun参数名称：" + Arrays.toString(getClassMethodParamNames(method)));

        method = ParameterNameASM.class.getDeclaredMethod("test1", String.class, int.class);
        System.out.println("test1参数名称：" + Arrays.toString(getInterfaceMethodParamNames(method).toArray()));

        method = ParameterNameASM.class.getDeclaredMethod("test2", String.class, int.class, Object.class);
        System.out.println("test2参数名称：" + Arrays.toString(getInterfaceMethodParamNames(method).toArray()));
    }

    /**
     * 获取类方法的参数名,指的是成员方法
     *
     * @param method
     * @return
     * @throws IOException
     */
    public static String[] getClassMethodParamNames(final Method method) throws IOException {

        final Class<?>[] methodParameterTypes = method.getParameterTypes();
        final String[] methodParametersNames = new String[methodParameterTypes.length];
        final String className = method.getDeclaringClass().getName();


        ClassReader cr = new ClassReader(className);

        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM6) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                final Type[] args = Type.getArgumentTypes(descriptor);
                // 方法名相同并且参数个数相同
                if (!name.equals(method.getName()) || !matchTypes(args, method.getParameterTypes())) {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, descriptor, signature, exceptions);

                return new MethodVisitor(Opcodes.ASM6, v) {
                    /**
                     * 获取本地变量表
                     * LocalVariableTable
                     */
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(method.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < methodParametersNames.length) {
                            methodParametersNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }

                };
            }
        };

        cr.accept(classVisitor, ClassReader.SKIP_FRAMES);

        return methodParametersNames;
    }

    /**
     * 获取接口方法的参数名（抽象方法也可以）
     * 编译时增加参数  -parameters
     *
     * @param method
     * @return
     * @throws IOException
     */
    public static List<String> getInterfaceMethodParamNames(final Method method) throws IOException {
        final Class<?>[] methodParameterTypes = method.getParameterTypes();
        final List<String> methodParametersNames = new ArrayList<>();
        final String className = method.getDeclaringClass().getName();
        ClassReader cr = new ClassReader(className);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM6) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

                final Type[] args = Type.getArgumentTypes(descriptor);

                // 方法名相同并且参数个数相同
                if (!name.equals(method.getName()) || !matchTypes(args, method.getParameterTypes())) {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }

                MethodVisitor v = super.visitMethod(access, name, descriptor, signature, exceptions);

                return new MethodVisitor(Opcodes.ASM6, v) {
                    /**
                     * 获取 MethodParameters 参数
                     */
                    @Override
                    public void visitParameter(String name, int access) {
                        methodParametersNames.add(name);
                        super.visitParameter(name, access);
                    }
                };
            }
        };

        cr.accept(classVisitor, ClassReader.SKIP_FRAMES);

        return methodParametersNames;
    }

    /**
     * 比较参数是否一致
     */
    private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
        if (types.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }
}