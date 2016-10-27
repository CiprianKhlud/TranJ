package com.khlud.ciprian.tranj.builders;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.khlud.ciprian.tranj.classesmodel.FileModel;
import com.khlud.ciprian.tranj.classesmodel.FileModelBuilder;
import com.khlud.ciprian.tranj.classesmodel.Module;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        String[] files = FolderUtils.getDirectoryFiles(pathToScan, file -> file.getName().endsWith(".java"));

        List<CompilationUnit> compilationUnits
                = Arrays.stream(files)
                        .map(this::fileToCompilationUnit)
                        .collect(Collectors.toList());
        compilationUnits.forEach(this::preRegisterTypes);
        compilationUnits.forEach(
                compilationUnit -> processFile(compilationUnit)
        );
    }

    private void preRegisterTypes(CompilationUnit compilationUnit) {
        compilationUnit.getChildrenNodes()
                .stream()
                .filter(node -> node instanceof ClassOrInterfaceDeclaration)
                .map(node -> (ClassOrInterfaceDeclaration) node)
                .forEach(decl -> {
                    String packageName = getPackageName(compilationUnit);
                    module.moduleTypeResolver.registerClassType(packageName,
                            decl.getName());
                });
        ;
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
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processFile(CompilationUnit cu) {
        FileModel fileModel = FileModelBuilder.build(cu, module);
    }

    public Module getModule() {
        return module;
    }
}
