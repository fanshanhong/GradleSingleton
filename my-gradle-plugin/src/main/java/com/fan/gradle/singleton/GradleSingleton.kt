package com.fan.gradle.singleton;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * 自定义插件的第三种方式
 *
 * 1.在单独的工程中写  src/main/groovy/包名
 * 2.src/main/resources 中写配置
 * 3.上传到本地 maven 库
 *
 * 使用
 * 1.在 RootProject 的 build.gradle 中添加 本地 maven 库地址和  classpath 'com.fan.gradle:singleton:1.0.0'
 * 2.在需要使用的地方 apply 全路径名: apply plugin: 'com.fan.gradle.singleton.GradleSingleton'
 *
 */
//public class GradleSingleton implements Plugin<Project> {
//    @Override
//    public void apply(Project target) {
////        project.android.registerTransform(new MyTransform(project))
//        System.out.println("22222222");
//
//        AppExtension android = target.getExtensions().getByType(AppExtension.class);
//        android.registerTransform(new LogTransform());
//        System.out.println("111");
//        target.task("aaaa", new Action<Task>() {
//            @Override
//            public void execute(Task task) {
//                System.out.println("eeeee");
//            }
//        });
//    }
//}

public class GradleSingleton :  Plugin<Project> {


    override fun apply(project: Project) {
        println("aasasasasaa")
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(LogTransform())
    }

}