package com.fan.gradleplugin.asm.aop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 类适配器
 */
public class ClassAopMethodVisitor extends ClassVisitor {

    public ClassAopMethodVisitor(final ClassVisitor classVisitor) {
        super(Opcodes.ASM4, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        //不代理构造函数
        if (!"<init>".equals(name)) {
            MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
            // 方式一
            // return new MethodVisitorOne(this.api,mv);
            // 方式二
            // return new MethodVisitorTwo(this.api, mv);
            // 方式三
            return new MethodVisitorThree(this.api, mv, access, name, descriptor);
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

}
