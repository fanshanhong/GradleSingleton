package com.fan.gradleplugin.asm.method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.NOP;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/31 14:35
 * @Modify:
 */
public class MethodVisitorTest2 {

    //
    // MethodVisitor 像 ClassVisitor 一样, 可以对类进行转换
    // 更改参数可以用于更改单个指令，不转接收到的调用会删除一条指令，然后插入接收到的调用之间的调用(call)会添加新的指令。
    //
    // MethodVisitor类提供了这种方法适配器的基本实现，该适配器除了转发所有接收到的方法调用外，什么也不做。
    public void transformMethod() {

    }

    public static void main(String[] args) {

    }
}

// 为了了解如何使用方法适配器，让我们考虑一个非常简单的适配器，该适配器删除方法内部的NOP指令（可以删除它们，因为它们什么都不做，因此不会出现问题）
class RemoveNopAdapter extends MethodVisitor {

    public RemoveNopAdapter(MethodVisitor mv) {
        super(ASM4, mv);
    }

    // 转发指令, 如果不是 NOP 就转发, NOP 就不转发, 这样, 就删除了 NOP 指令
    @Override
    public void visitInsn(int opcode) {
        if (opcode != NOP) {
            mv.visitInsn(opcode);
        }
    }
}

// 类适配器只是构建一个方法适配器，封装了链中下一个类访问者返回的方法访问者，然后返回该适配器。
class RemoveNopClassAdapter extends ClassVisitor {
    public RemoveNopClassAdapter(ClassVisitor cv) {
        super(ASM4, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        MethodVisitor mv;
        mv = cv.visitMethod(access, name, desc, signature, exceptions);

        // 类适配器可以选择仅在方法中而不在构造函数中删除NOP。
        // mv = cv.visitMethod(access, name, desc, signature, exceptions);
        // if (mv != null && !name.equals("<init>")) {
        //     mv = new RemoveNopAdapter(mv);
        // }

        if (mv != null) {
            mv = new RemoveNopAdapter(mv);
        }
        return mv;
    }
}

