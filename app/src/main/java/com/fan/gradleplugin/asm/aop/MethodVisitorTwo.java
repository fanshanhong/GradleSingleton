package com.fan.gradleplugin.asm.aop;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * 结合已有的方法进行代码增强
 * 就是提前把增强的代码写好, 我们写在了 MethodAspect 类中的两个方法
 * 然后在指定的地方, 使用字节码的方式调用 MethodAspect中的方法
 */
public class MethodVisitorTwo extends MethodVisitor {

    public MethodVisitorTwo(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    /**
     * 方法的开始,即刚进入方法里面
     */
    @Override
    public void visitCode() {
        mv.visitMethodInsn(INVOKESTATIC, MethodAspect.class.getName().replace(".", "/"), "beforeMethod", "()V", false);
        super.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == ARETURN || opcode == RETURN) {
            mv.visitMethodInsn(INVOKESTATIC, MethodAspect.class.getName().replace(".", "/"), "afterMethod", "()V", false);
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        mv.visitMaxs(6, 6);
        super.visitEnd();
    }
}
