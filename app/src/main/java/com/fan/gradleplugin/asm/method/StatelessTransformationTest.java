package com.fan.gradleplugin.asm.method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.io.FileOutputStream;

import static com.fan.gradleplugin.asm.ClassVisitorTest.getClassBytes;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * @Description: 无状态转换, 又称为代码增强
 *
 * public void m() throws Exception {
 * Thread.sleep(100);
 * }
 *
 * 增强为:
 *
 * public void m() throws Exception {
 * timer -= System.currentTimeMillis();    // timer=-1000
 * Thread.sleep(100);
 * timer += System.currentTimeMillis();     // timer=100
 * }
 * @Author: shanhongfan
 * @Date: 2021/6/9 16:37
 * @Modify:
 */
public class StatelessTransformationTest {

    public static void main(String[] args) throws Exception {

        ClassReader classReader = new ClassReader(getClassBytes("MyT.class"));

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        AddTimerAdapter addTimerAdapter = new AddTimerAdapter(classWriter);

        classReader.accept(addTimerAdapter, 0);


        classWriter.visitEnd();
        byte[] bytes = classWriter.toByteArray();
        File file
                = new File("MyTEnhancer.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}


/**
 * 自定义类转换器
 */
class AddTimerAdapter extends ClassVisitor {

    private String owner;
    // 是否是接口
    private boolean isInterface;


    public AddTimerAdapter(ClassVisitor cv) {
        super(ASM4, cv);
    }


    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name + "Enhancer", signature, superName, interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }


    /**
     * 对方法进行增强
     *
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {

        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        // 不是接口, 并且不是构造, 就对方法进行增强
        if (!isInterface && mv != null && !name.equals("<init>")) {
            mv = new AddTimerMethodAdapter(mv);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if (!isInterface) {
            // 添加字段 timer, public static long 类型的
            FieldVisitor fv = cv.visitField(ACC_PUBLIC + ACC_STATIC, "timer", "J", null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        } else {
            cv.visitEnd();
        }
    }


    class AddTimerMethodAdapter extends MethodVisitor {

        public AddTimerMethodAdapter(MethodVisitor mv) {
            super(ASM4, mv);
        }

        /**
         * 方法开始处添加指令
         */
        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J");
            mv.visitInsn(LSUB);
            mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        }

        /**
         * 方法结束添加指令
         *
         * @param opcode
         */
        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                        "currentTimeMillis", "()J");
                mv.visitInsn(LADD);
                mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            }
            mv.visitInsn(opcode);
        }

        /**
         * 更新最大操作数堆栈大小
         *
         * 这个方法也可以不重写, 可以依靠 COMPUTE_MAXS 选项来计算最佳值
         * @param maxStack
         * @param maxLocals
         */
//        @Override
//        public void visitMaxs(int maxStack, int maxLocals) {
//            mv.visitMaxs(maxStack + 4, maxLocals);
//        }
    }
}

