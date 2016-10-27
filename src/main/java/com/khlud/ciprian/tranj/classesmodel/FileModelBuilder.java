package com.khlud.ciprian.tranj.classesmodel;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.*;
import com.khlud.ciprian.tranj.builders.ModelBuilder;

import java.util.List;

/**
 * @author cipriankhlud
 */
public class FileModelBuilder {

    public static FileModel build(CompilationUnit compilationUnit, Module module) {
        FileModel result = module.createFileModel();
        addImportsToFileModel(compilationUnit, result);

        result.packageName = ModelBuilder.getPackageName(compilationUnit);
        buildFileModel(result, compilationUnit);
        return result;
    }

    private static void addImportsToFileModel(CompilationUnit compilationUnit, FileModel result) {
        List<ImportDeclaration> imports = compilationUnit.getImports();
        if (imports == null) {
            return;
        }
        imports.forEach((it) -> {
            result.imports.add(it.getName().toStringWithoutComments());
        });
        result.setupTypeResolver();
    }

    private static void buildFileModel(FileModel result, CompilationUnit compilationUnit) {
        compilationUnit.getTypes()
                .forEach(typeDeclaration -> {
                    if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
                        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) typeDeclaration;
                        buildTypeToModel(result, compilationUnit, classOrInterfaceDeclaration);
                    }
                });

    }

    private static void buildTypeToModel(FileModel result, CompilationUnit compilationUnit, ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {

        if (!classOrInterfaceDeclaration.isInterface()) {
            processClass(result, classOrInterfaceDeclaration, result);

        } else {
            processInterface(result, classOrInterfaceDeclaration);

        }
    }

    private static void processClass(FileModel result, ClassOrInterfaceDeclaration classOrInterfaceDeclaration, FileModel fileModel) {
        ClassModel cls = result.module.createClass(classOrInterfaceDeclaration.getName(), result.packageName);
        classOrInterfaceDeclaration.getMembers()
                .forEach(member -> {
                    handleMembers(cls, member, fileModel);
                });

    }

    private static void handleMembers(ClassModel cls, BodyDeclaration member, FileModel fileModel) {
        if (member instanceof FieldDeclaration) {
            handleField(cls, (FieldDeclaration) member, fileModel);
        }
        if (member instanceof MethodDeclaration) {
            handleMethod(cls, (MethodDeclaration) member, fileModel);
        }
    }

    private static void handleMethod(ClassModel cls, MethodDeclaration member, FileModel typeResolver) {
        Method m = cls.createMethod();
        m.fillMethodProperties(member, typeResolver);
    }

    private static void handleField(ClassModel cls, FieldDeclaration member, FileModel fileModel) {
        int modifiers = member.getModifiers();
        boolean isStatic = ModifierSet.isStatic(modifiers);
        member.getVariables()
                .forEach(var -> {
                    Variable varVal = cls.addVariable(var.getId().getName(), member.getType(), fileModel);
                    varVal.initialValue = var.getInit();
                    varVal.isStatic = isStatic;
                });

    }

    private static void processInterface(FileModel result, ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        Interface anInterface = result.module.createInterface(classOrInterfaceDeclaration.getName(), result.packageName);
    }
}
