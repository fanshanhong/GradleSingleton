package com.fan.gradleplugin.reflectasm;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Description: ReflectASM is a very small Java library that provides high performance reflection by using code generation.
 *
 * An access class is generated to set/get fields, call methods, or create a new instance.
 *
 * The access class uses bytecode rather than Java’s reflection, so it is much faster.
 *
 * It can also access primitive fields via bytecode to avoid boxing.
 *
 * ReflectASM 的思想非常好，通过 asm 创建一个集成类。然后所有的实现都是直接通过调用，性能是优于反射的。
 * @Author: shanhongfan
 * @Date: 2021/6/19 21:30
 * @Modify:
 */
public class Test {

    public static void main(String[] args) {

        methodTest();

        constructorTest();
        ;
        fieldTest();
    }

    private static void methodTest() {
        UserService someObject = new UserService("fan");
        MethodAccess access = MethodAccess.get(UserService.class);
        access.invoke(someObject, "showName");

    }

    private static void constructorTest() {
        // 发现不支持有参构造器
        ConstructorAccess<UserService> access = ConstructorAccess.get(UserService.class);
        UserService someObject = access.newInstance();
        someObject.showName();

    }

    private static void fieldTest() {
        // 字段默认也无法反射私有变量
        UserService someObject = new UserService();
        FieldAccess access = FieldAccess.get(UserService.class);

        for (String fieldName : access.getFieldNames()) {
            System.out.println(fieldName + ":" + access.get(someObject, fieldName));
        }

        for (int i = 0; i < access.getFieldCount(); i++) {
            System.out.println(access.get(someObject, i));
        }

    }
}
