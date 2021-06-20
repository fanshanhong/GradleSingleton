package com.fan.gradleplugin.asm.aop;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

/**
 * 方法适配器
 * 通过字节编写字节码的方式织入代码
 * 这种方法貌似比较麻烦, 个人倾向于 MethodVisitorTwo
 */
public class MethodVisitorOne extends MethodVisitor {

    public MethodVisitorOne(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitCode() {

        // 在进入方法的时候, 加入几条指令
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("方式一:方法开始运行");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        super.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        // 在 RETURN 指令和 ARETURN 指令之前, 加入几条指令
        if (opcode == ARETURN || opcode == RETURN) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("方式一:方法调用结束");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        }
        super.visitInsn(opcode);
    }

    /**
     * 修改 局部变量表 和 操作数栈的数量
     */
    @Override
    public void visitEnd() {
        mv.visitMaxs(6, 6);
        super.visitEnd();
    }
}
