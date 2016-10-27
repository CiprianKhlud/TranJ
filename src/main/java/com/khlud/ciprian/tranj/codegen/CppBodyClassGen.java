package com.khlud.ciprian.tranj.codegen;

import com.khlud.ciprian.tranj.classesmodel.ClassModel;
import com.khlud.ciprian.tranj.classesmodel.NameDefinition;
import com.khlud.ciprian.tranj.resolvers.ResolvedType;

import java.io.File;
import java.util.Set;

/**
 * @author cipriankhlud
 */
public class CppBodyClassGen {

    public TextGen textGen;
    CppMethodWriter cppMethodWriter;

    public CppBodyClassGen(File ouputDir) {
        textGen = new TextGen(ouputDir);
        cppMethodWriter = new CppMethodWriter(textGen);
    }

    public void writeCode(ClassModel classModel, String fileName) {
        textGen.fileName = fileName;

        String baseName = classModel.buildBaseName();
        textGen.writeLineFormat("#include \"{0}.hpp\"", baseName);
        textGen.writeLine();
        writeIncludeReferences(classModel.referencedTypes);

        textGen.writePackage(classModel);

        classModel.methods.
                forEach(method -> cppMethodWriter.writeMethod(classModel, method));

        textGen.writeEndPackage(classModel, textGen);

        textGen.writeToDisk();
    }

    private void writeIncludeReferences(Set<ResolvedType> referencedTypes) {
        referencedTypes.forEach(
                resolvedType -> {
                    String baseName = NameDefinition.buildBaseName(resolvedType.packageName, resolvedType.name);
                    textGen.writeLineFormat("#include \"{0}.hpp\"", baseName);
                }
        );
    }
}
