package com.fan.gradleplugin.asm;

import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V1_5;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/5/26 16:19
 * @Modify:
 */
public class ClassWriterTest {

    public static void main(String[] args) throws Exception {
        ClassWriter cw = new ClassWriter(0);

        // 对visit方法的调用定义了类头。
        //
        // 与所有其他ASM常量一样，V1_5参数是在ASM Opcodes接口中定义的常量。它指定类版本Java 1.5。
        //
        // ACC_XXX常量是与Java修饰符相对应的标志。在这里，我们指定该类为接口，并且它是公共的和抽象的（因为无法实例化）。
        //
        // 第三个参数 name 指定类名/接口名
        //
        // 第四个参数 指定泛型类型, 这里为 null
        //
        // 第五个参数 指定父类, 所有接口隐式继承自 Object
        //
        // 第六个参数 指定扩展接口的数组
        cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable", null, "java/lang/Object",
                new String[]{/*"pkg/Mesurable"*/});

        // 接下来的对visitField的三个调用是相似的，用于定义三个字段。
        //
        // 第一个参数是一组标志，它们对应于Java修饰符。 在这里，我们指定字段是public，final和static。
        //
        // 第二个参数是字段的名称，它出现在源代码中。
        //
        // 第三个参数是字段的类型，采用类型描述符形式。 在这里字段是整数字段，其描述符为I。
        //
        // 第四个参数对应于泛型。 在我们的示例中，该字段为null，因为字段类型未使用泛型。
        //
        // 最后一个参数是字段的常量值：必须使用此参数仅适用于真正恒定的字段，即最终的静态字段。
        //
        // 由于此处没有 annotations，因此我们将立即调用FieldVisitor的visitEnd()方法，即无需调用其visitAnnotation或visitAttribute方法。
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I",
                null, new Integer(-1)).visitEnd();

        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I",
                null, new Integer(0)).visitEnd();

        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I",
                null, new Integer(1)).visitEnd();


        // visitMethod调用用于定义compareTo方法。
        //
        //同样，这里的第一个参数是一组与Java修饰符相对应的标志。
        //
        //第二个参数是方法名称，它出现在源代码中。
        //
        //第三个参数是方法的描述符。
        //
        //第四个参数对应于泛型。在我们的例子中，它为null，因为该方法未使用泛型。
        //
        //最后一个参数是由方法抛出的异常数组，由其内部名称指定。此处为null，因为该方法未声明任何异常。
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();

        // 最后，对visitEnd的最后一次调用, 用于通知ClassWriter类已完成对toByteArray的调用用于将其作为字节数组检索。
        cw.visitEnd();
        byte[] b = cw.toByteArray();

        File file
                = new File("Comparable.class");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(b);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
