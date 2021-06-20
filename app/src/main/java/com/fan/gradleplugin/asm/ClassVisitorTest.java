package com.fan.gradleplugin.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_5;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/27 14:26
 * @Modify:
 */
public class ClassVisitorTest {

    public static void main(String[] args) throws Exception {

        //ClassReader
        //ClassReader 类解析 字节数组给出的已编译类(.class文件)，并在作为参数传递给其accept方法的ClassVisitor实例上调用相应的visitXxx方法。 可以将其视为事件产生器。
        //
        //ClassWriter
        //ClassWriter 类是ClassVisitor抽象类的子类，该类直接以二进制形式构建编译的类。它产生一个包含已编译类的字节数组作为输出，可以使用toByteArray方法进行检索。可以将其视为事件消费者。
        //
        //ClassVisitor
        //ClassVisitor 类将其接收的所有方法委托给另一个ClassVisitor实例。
        //
        //可以将其视为事件过滤器。


        // 类解析器, 传入.class 文件的字节数组
        byte[] b1 = getClassBytes("Comparable.class");
        ClassReader cr = new ClassReader(b1);
        System.out.println(new String(b1));
        ClassWriter cw = new ClassWriter(0);
        // 调用 accept 方法, 会在 cw 上不断调用 visitXX 相关方法, 调用完了之后, cw 就获得了新的 class 的字节数组.  这里,我们直接传入了 cw, 其实没有对原先的类有任何修改,因此 cw.toByteArray() 和 原先的 byteArrayOutputStream.toByteArray() 应该是一样的
        cr.accept(cw, 0);

        byte[] b2 = cw.toByteArray(); // b2 represents the same class as b1
        System.out.println(new String(b2));

        boolean isEquals = true;
        if (b1.length == b2.length) {
            for (int i = 0; i < b2.length; i++) {
                if (b2[i] != b1[i]) {
                    isEquals = false;
                    break;
                }
            }
        } else {
            isEquals = false;
        }
        System.out.println("isEquals:" + isEquals);


        //---------------------
        // 下一步是在类读取器和类写入器之间引入ClassVisitor.
        byte[] bytes1 = getClassBytes("MainActivity3.class");

        ClassReader classReader = new ClassReader(bytes1);

        ClassWriter classWriter = new ClassWriter(0);

        ChangeVersionAdapter adapter = new ChangeVersionAdapter(classWriter);

        // 现在可以通过重写某些方法来过滤某些事件，以便能够转换类。
        classReader.accept(adapter, 0);
        byte[] bytes2 = classWriter.toByteArray();
        File file = new File("MainActivity2.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes2);
        fileOutputStream.flush();
        fileOutputStream.close();


        //--------优化, 建议仅将此优化用于“附加”转换。
//        ClassReader cr2 = new ClassReader(b1);
//        ClassWriter cw2 = new ClassWriter(cr2, 0); // 注意这里第一个参数是 ClassReader
//        ChangeVersionAdapter ca2 = new ChangeVersionAdapter(cw2);
//        cr.accept(ca2, 0);
//        cw2.toByteArray();

    }

    public static byte[] getClassBytes(String name) throws Exception {
        // 拿到.class 文件的字节数组
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(new File(name));
        int len = -1;
        byte[] bytes = new byte[512];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = fileInputStream.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, len);
        }
        byteArrayOutputStream.flush();
        byte[] b = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return b;
    }

}


