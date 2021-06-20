package com.fan.gradleplugin.asm;

import org.objectweb.asm.util.ASMifier;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/28 10:01
 * @Modify:
 */
public class ASMifierTest {

    // ASMifier
    // 此后端使TraceClassVisitor类的每个方法都能打印用于调用它的Java代码。
    // 例如，调用visitEnd()方法将打印cv.visitEnd();。
    //
    // 结果是，当具有ASMifier后端的TraceClassVisitor访问者访问类时，它将打印源代码以使用ASM生成此类。
    //
    // 如果您使用此访问者访问已经存在的 class ，这将很有用。
    //
    // 引用场景
    // 例如，如果您不知道如何使用ASM生成一些已编译的类，请编写相应的源代码，使用javac进行编译，然后使用ASMifier访问已编译的类。
    //
    // 您将获得ASM代码来生成此编译的类！

    // 命令行使用形式
    // 可以从命令行使用ASMifier类。
    //
    // 例如使用:
    // 注意:我把asm-util-7.0.jar:asm-7.0.jar 两个 jar 包都拷贝到 libs 下面了, 然后在 libs 下面执行命令:
    // java -classpath asm-util-7.0.jar:asm-7.0.jar org.objectweb.asm.util.ASMifier java.lang.Runnable

    // IDEA 的插件  ASM bytecode outline 应该就是用这个来实现的


    // 另外, java -classpath asm.jar:asm-util.jar  org.objectweb.asm.util.TraceClassVisitor java.lang.Void
    // 这个工具, 可以打印类的字节码形式.

    // 如果要跟踪链中某个位置的单个方法的内容，而不是跟踪其类的所有内容，可以使用TraceMethodVisitor而不是TraceClassVisitor
}
