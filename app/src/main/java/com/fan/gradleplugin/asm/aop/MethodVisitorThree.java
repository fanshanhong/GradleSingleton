package com.fan.gradleplugin.asm.aop;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Description: 尝试第三种写法:使用 AdviceAdapter
 * @Author: shanhongfan
 * @Date: 2021/6/10 10:25
 * @Modify:
 */
public class MethodVisitorThree extends AdviceAdapter {
    /**
     * Constructs a new {@link AdviceAdapter}.
     */
    protected MethodVisitorThree(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }

    @Override
    protected void onMethodEnter() {
        // 父类的默认实现是空的
        super.onMethodEnter();

        mv.visitMethodInsn(INVOKESTATIC, MethodAspect.class.getName().replace(".", "/"), "beforeMethod", "()V", false);
    }

    @Override
    protected void onMethodExit(int opcode) {
        // 父类的默认实现是空的
        super.onMethodExit(opcode);

        mv.visitMethodInsn(
                INVOKESTATIC,
                MethodAspect.class.getName().replace(".", "/"),
                "afterMethod",
                "()V",
                false);

        //
        // 为啥需要 ALOAD,0 这个指令???
        // 这里是想在 Activity 的 onCreate 方法中调用 com.fan.gradleplugin.TraceUtil 类中的 onActivityCreate(Context context)方法. 该方法需要把 Activity 传入
        // 这个 ALOAD,0 就是拿到 this, 把 this 作为参数传入
        //        mv.visitVarInsn(ALOAD, 0);
        //        mv.visitMethodInsn(INVOKESTATIC,
        //                "com/fan/gradleplugin/TraceUtil",
        //                "onActivityCreate", "(Landroid/app/Activity;)V",
        //                false);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(6, 6);
    }
}
