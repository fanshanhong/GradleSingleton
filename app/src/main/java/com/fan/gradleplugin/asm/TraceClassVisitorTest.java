package com.fan.gradleplugin.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_5;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/28 09:35
 * @Modify:
 */
public class TraceClassVisitorTest {

    public static void main(String[] args) throws Exception {

        // 文本表示将更易于使用。这是TraceClassVisitor类提供的。


        ClassWriter cw = new ClassWriter(0);

        PrintWriter printWriter = new PrintWriter(System.out);

        // 这段代码创建了一个TraceClassVisitor，它将接收到的所有调用委托给cw，并将这些调用的文本表示输出到printWriter。
        // PrintWriter 包装了标准输出, 因此会打印在控制台上
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(cw, printWriter);

        traceClassVisitor.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/MainActivity3", null, "java/lang/Object",
                new String[]{/*"pkg/Mesurable"*/});

        traceClassVisitor.visitEnd();


        printWriter.flush();
        printWriter.close();
    }
}
