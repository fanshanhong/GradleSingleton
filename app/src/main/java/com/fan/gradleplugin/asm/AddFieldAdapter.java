package com.fan.gradleplugin.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 这里是一个类适配器，它将一个字段添加到类中，除非该字段已经存在：
 */
public class AddFieldAdapter extends ClassVisitor {

    private int fAcc;
    private String fName;
    private String fDesc;
    private boolean isFieldPresent;

    public AddFieldAdapter(ClassVisitor cv, int fAcc, String fName,
                           String fDesc) {
        super(Opcodes.ASM4, cv);
        this.fAcc = fAcc; //access
        this.fName = fName;
        this.fDesc = fDesc;

        // class
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        if (name.equals(fName)) {
            isFieldPresent = true; // 已经存在该字段
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {// 如果不存在该字段, 手动调用 vc.visitField 来添加新的字段
            FieldVisitor fv = cv.visitField(fAcc, fName, fDesc, null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        cv.visitEnd();

        // super方法和上面的 cv.visitEnd() 基本等价的
        // super.visitEnd();
    }



}
