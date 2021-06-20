package com.fan.gradleplugin.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_5;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/28 15:07
 * @Modify:
 */
public class ChangeVersionAdapter extends ClassVisitor {
    public ChangeVersionAdapter(ClassVisitor cv) {
        super(ASM4, cv);
    }

    //此类仅覆盖ClassVisitor类的一个visit()方法。
    //
    //这样，除了对visit方法的调用之外，所有调用均以不变的方式转发给传递给构造函数的类visitor cv。

    // visit 方法中, 仅仅是修改了版本号
    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(V1_5, access, name, signature, superName, interfaces);
    }


    // 通过更改visitField和visitMethod方法中的access或name参数，可以更改修饰符或字段或方法的名称。
//    @Override
//    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//        return super.visitField(access, name, descriptor, signature, value);
//    }

    // 此外，您可以选择完全不转发此调用，结果是删除了相应的类元素。
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // 这里返回 null, 那字段就都删除了
        // return null;

        // 我们尝试改一下字段的名字
        // return super.visitField(access, "hahahahaha", descriptor, signature, value);


        // 我们尝试添加一个字段
        if (name.equals("hello")) {
            cv.visitField(access, "newName", descriptor, signature, value);
        }
        return super.visitField(access, name, descriptor, signature, value);

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // 这里返回 null, 方法就被删除了
        return null;
//        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {

        // access 说的是修饰符
        cv.visitField(25, "vvv", "I", null, 1);
        super.visitEnd();
    }
}
