package com.fan.gradleplugin.reflectasm;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/6/19 21:41
 * @Modify:
 */
public class Test2 {

    public static void main(String[] args) {

        // 通过get方法得到某个类的加强类，直接调用类上的setName完成方法的调用.
        // 主要的过程便是在get方法中，它通过asm生成SomeClass的代理类，实现了MethodAccess的invoke方法，方法的内容是生成SomeClass的所有方法的调用index，这样可以通过指定方法名称的方式调用类上的方法。直接调用类上方法的速度肯定要快于反射调用了

        ConstructorAccess<SomeClass> access = ConstructorAccess.get(SomeClass.class);
        SomeClass someObject = access.newInstance();

        // 只能获取 non-private 的方法
        MethodAccess methodAccess = MethodAccess.get(SomeClass.class);

        Object name = methodAccess.invoke(someObject, "getName");
        System.out.println(name);
        methodAccess.invoke(someObject, "setName", "newName");
        name = methodAccess.invoke(someObject, "getName");
        System.out.println("newName:" + name);

    }
}
