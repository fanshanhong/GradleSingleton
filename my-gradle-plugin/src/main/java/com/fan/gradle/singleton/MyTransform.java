package com.fan.gradle.singleton;

import com.android.build.api.transform.*;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class MyTransform extends Transform {
    Project project;

    public MyTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "MyTransform";
    }

    //设置输入类型，我们是针对class文件处理
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    //设置输入范围，我们选择整个项目
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
    //重点就是该方法，我们需要将修改字节码的逻辑就从这里开始

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        Collection<TransformInput> inputs = transformInvocation.getInputs();


        for (TransformInput input : inputs) {


            Collection<JarInput> jarInputs = input.getJarInputs();

            for (JarInput jarInput : jarInputs) {
                //处理Jar
                processJarInput(jarInput, outputProvider);
            }


            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs();
            for (DirectoryInput directoryInput : directoryInputs) {
                //处理源码文件
                processDirectoryInputs(directoryInput, outputProvider);
            }
        }

    }

    void processJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(
                jarInput.getFile().getAbsolutePath(),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR);
        //TODO do some transform
        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
//        FileUtils.copyFiley(jarInput.getFile(), dest);
    }

    void processDirectoryInputs(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(),
                Format.DIRECTORY);
        //建立文件夹
//        FileUtils.forceMkdir(dest)
        //TODO do some transform
        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        transformDirectoryInput(directoryInput, dest);

//        FileUtils.copyDirectory(directoryInput.getFile(), dest)
    }

    //真正执行文件修改的地方
    private void transformDirectoryInput(DirectoryInput directoryInput, File dest) {

////        directoryInput.forEach { directoryInput: DirectoryInput? ->
//        //是否是目录
//        if (directoryInput.getFile()!=null && directoryInput.getFile().isDirectory() == true){
//            val fileTreeWalk = directoryInput.file.walk()
//            fileTreeWalk.forEach {
//                file ->
//                        var name = file.name
//                //在这里进行代码处理
//                if (name.endsWith(".class") && !name.startsWith("R\$")
//                        && "R.class" != name && "BuildConfig.class" != name) {
//                    println 'name 不是 R, 进来了'
//                    val classReader = ClassReader(file.readBytes())
//                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//                    val className = name.split(".class")[0]
//                    val classVisitor = TraceVisitor(className, classWriter)
//                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
//                    val code = classWriter.toByteArray()
//                    val fos = FileOutputStream(file.parentFile.absoluteFile.toString() + File.separator + name)
//                    fos.write(code)
//                    fos.close()
//                }
//            }
//        }
//
//        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
//        FileUtils.copyDirectory(directoryInput.file, dest)
    }

}