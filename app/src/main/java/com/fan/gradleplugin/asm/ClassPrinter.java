package com.fan.gradleplugin.asm;

import org.objectweb.asm.*;

import java.io.IOException;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class ClassPrinter extends ClassVisitor {

    public static void main(String[] args) throws IOException {
        ClassPrinter cp = new ClassPrinter();

        // 创建一个ClassReader来解析Runnable类
        ClassReader cr = new ClassReader("java.lang.String");

        // ClassReader 类解析作为字节数组给出的已编译类，并在作为参数传递给其accept方法的ClassVisitor实例上调用相应的visitXxx方法。
        cr.accept(cp, 0);
    }


    public ClassPrinter() {
        super(Opcodes.ASM4);
    }

    public ClassPrinter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println("interfaces:");
        System.out.println(interfaces);
        System.out.println(name + " extends " + superName + " {");
    }

    @Override
    public void visitSource(String source, String debug) {
        System.out.println("visitSource");
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc,
                                             boolean visible) {
        System.out.println("visitAnnotation");
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {
        System.out.println("visitAttribute");
    }

    @Override
    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        System.out.println(" " + desc + " " + name);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        System.out.println(" " + name + desc);
        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println("}");
    }

}
