package com.fan.gradleplugin.asm.method;

import com.fan.gradleplugin.asm.ChangeVersionAdapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_5;
import static org.objectweb.asm.TypeReference.NEW;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/28 14:54
 * @Modify:
 */
public class MethodVisitorTest {

    // ASM提供了三个基于MethodVisitor API的核心组件来生成和转换方法：
    //
    // 1. ClassReader类解析已编译方法的内容，并在ClassVisitor返回的MethodVisitor对象上调用相应方法，该对象作为参数传递给其accept方法。
    //
    // 2. ClassWriter的visitMethod方法返回MethodVisitor接口的实现，该接口直接以二进制形式构建编译的方法。
    //
    // 3. MethodVisitor类将其接收的所有方法委托给另一个MethodVisitor实例。 可以将其视为事件过滤器。

    // ClassWriter 选项
    // 为一个方法计算堆栈映射帧不是一件容易的事：
    //
    // 您必须计算所有帧，找到与跳转目标相对应或无条件跳转的帧，最后压缩其余的帧框架。
    //
    // 同样，为方法计算局部变量和操作数堆栈部分的大小比较容易，但仍然不是很容易。
    //
    // 指定自动计算
    // 希望ASM可以为您计算。
    //
    // 创建ClassWriter时，可以指定必须自动计算的内容：
    //
    // 不计算任何东西
    // 使用 new ClassWriter(0) 不会自动计算任何内容。
    //
    // 您必须自己计算帧以及局部变量和操作数堆栈的大小。
    //
    // 计算局部变量和操作数堆栈部分
    // 使用 new ClassWriter(ClassWriter.COMPUTE_MAXS) 将为您计算局部变量和操作数堆栈部分。
    //
    // 您仍然必须调用visitMaxs，但是可以使用任何参数：它们将被忽略并重新计算。
    //
    // 使用此选项，您仍然必须自己计算帧。
    //
    // 计算所有
    // 使用 new ClassWriter(ClassWriter.COMPUTE_FRAMES) 会自动计算所有内容。
    //
    // 您不必调用visitFrame，但仍必须调用visitMaxs（参数将被忽略并重新计算）。


    public static void main(String[] args) throws Exception {
        generateGetMethod();

    }

    private static void generateGetMethod() throws Exception {

        ClassWriter classWriter = new ClassWriter(0);
        ClassVisitor cv = new ChangeVersionAdapter(classWriter);
        cv.visit(V1_5, ACC_PUBLIC, "MyT", null, "java/lang/Object", null);

        cv.visitField(ACC_PUBLIC, "f", "I", null, 1);

        MethodVisitor constructorMethod = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
                null);

        MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "compareTo",
                "(Ljava/lang/Object;)I", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "MyT", "f", "I");
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 1);// 在访问所有说明之后，必须完成对visitMaxs的调用。它用于定义此方法执行帧的局部变量和操作数堆栈部分的大小。
        mv.visitEnd();


        MethodVisitor mv2 = classWriter.visitMethod(ACC_PUBLIC, "checkAndSetF", "(I)V", null,
                null);
        mv2.visitCode();
        mv2.visitVarInsn(ILOAD, 1);
        Label label = new Label();
        mv2.visitJumpInsn(IFLT, label);
        mv2.visitVarInsn(ALOAD, 0);
        mv2.visitVarInsn(ILOAD, 1);
        mv2.visitFieldInsn(PUTFIELD, "MyT", "f", "I");
//        Label end = new Label();
//        mv2.visitJumpInsn(GOTO, end);
//        mv2.visitLabel(label);
//        mv2.visitFrame(F_SAME, 0, null, 0, null);
//        mv2.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
//        mv2.visitInsn(DUP);
//        mv2.visitMethodInsn(INVOKESPECIAL,"java/lang/IllegalArgumentException", "<init>", "()V");
//        mv2.visitInsn(ATHROW);
//        mv2.visitLabel(end);
//        mv2.visitFrame(F_SAME, 0, null, 0, null);
        mv2.visitInsn(RETURN);
        mv2.visitMaxs(2, 2);
        mv2.visitEnd();

        cv.visitEnd();


        byte[] b = classWriter.toByteArray();

        File file
                = new File("MyT.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(b);
        fileOutputStream.flush();
        fileOutputStream.close();
    }




}
