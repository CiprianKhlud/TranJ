package com.khlud.ciprian.tranj.builders;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.khlud.ciprian.tranj.classesmodel.FileModel;
import com.khlud.ciprian.tranj.classesmodel.FileModelBuilder;
import com.khlud.ciprian.tranj.classesmodel.Module;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cipriankhlud on 19/10/2016.
 */
public class ModelBuilder {

    private String pathToScan;

    Module module = new Module();

    public ModelBuilder(String pathToScan) {
        this.pathToScan = pathToScan;
    }

    public void process() {
        start = System.currentTimeMillis();

        String[] files = FolderUtils.getDirectoryFiles(pathToScan, file -> file.getName().endsWith(".java"));
        List<String> fileList = Arrays.asList(files);
        System.out.println("Preregister files");
        fileList.parallelStream()
                .map(this::fileToCompilationUnit)
                .forEach(this::preRegisterTypes);

        System.out.println("Process files");
        fileList.stream()
                .map(this::fileToCompilationUnit)
                .forEach(
                        compilationUnit -> processFile(compilationUnit)
                );
    }

    long start, current, countFiles;
    synchronized private void preRegisterTypes(CompilationUnit compilationUnit) {
        if(compilationUnit==null){
            return;
        }
        compilationUnit.getChildrenNodes()
                .stream()
                .filter(node -> node instanceof ClassOrInterfaceDeclaration)
                .map(node -> (ClassOrInterfaceDeclaration) node)
                .forEach(decl -> {
                    String packageName = getPackageName(compilationUnit);
                    module.moduleTypeResolver.registerClassType(packageName,
                            decl.getName());
                });
        countFiles++;
        current = System.currentTimeMillis();
        if(current-start>=1000){
            System.out.println("Files per second: "+countFiles);
            countFiles = 0;

            start = current;
        }
    }

    public static String getPackageName(CompilationUnit compilationUnit) {
        String packageName = "";
        if (compilationUnit.getPackage() != null) {
            packageName = compilationUnit.getPackage().getName().getName();
        }
        return packageName;
    }

    private CompilationUnit fileToCompilationUnit(String file) {
        // creates an input stream for the file to be parsed
        try (FileInputStream in = new FileInputStream(file)) {

            // parse the file
            CompilationUnit cu = JavaParser.parse(in);

            return cu;
        } catch (Throwable e) {
            System.out.println("Error parsing file: "+ file);
        }
        return null;
    }

    private void processFile(CompilationUnit cu) {
        if(cu==null){
            return;
        }
        FileModel fileModel = FileModelBuilder.build(cu, module);
    }

    public Module getModule() {
        return module;
    }
}
