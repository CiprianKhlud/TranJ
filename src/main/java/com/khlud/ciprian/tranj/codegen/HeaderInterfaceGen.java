package com.khlud.ciprian.tranj.codegen;

import com.khlud.ciprian.tranj.classesmodel.InterfaceModel;
import com.khlud.ciprian.tranj.classesmodel.Method;
import com.khlud.ciprian.tranj.classesmodel.Variable;
import com.khlud.ciprian.tranj.resolvers.ResolvedType;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by Ciprian on 10/28/2016.
 */
public class HeaderInterfaceGen {
    TextGen textGen;

    public HeaderInterfaceGen(File ouputDir) {
        textGen = new TextGen(ouputDir);
    }

    public void writeCode(InterfaceModel classModel, String fileName) {
        textGen.fileName = fileName;

        writeIfndefCode(classModel);

        writeReferencedTypes(classModel.referencedTypes);

        writeClassHeader(classModel);

        textGen.writeLine("#endif");

        textGen.writeToDisk();
    }

    private void writeClassHeader(InterfaceModel classModel) {

        textGen.writePackage(classModel);
        textGen.write(textGen.indentText);
        textGen.indentCount++;

        textGen.write("struct ");
        textGen.write(classModel.name);
        textGen.indent();

        writeMethods(classModel.methods);

        textGen.unindent("};");
        textGen.unindent("");

        textGen.writeEndPackage(classModel, textGen);
    }

    private void writeReferencedTypes(Set<ResolvedType> referencedTypes) {
        referencedTypes.forEach(refType -> {
            writeReferencedType(refType);
        });
        textGen.writeLine();
    }

    private void writeReferencedType(ResolvedType refType) {
        List<String> packageNames = textGen.splitTextByTokens(refType.packageName, '.');
        packageNames.forEach(p -> {
            textGen.writeFormat("namespace {0} ", p);
            textGen.write("{");
        });
        textGen.writeFormat("class {0};", refType.name);
        packageNames.forEach(p -> {
            textGen.write(" }");
        });
        textGen.writeLine();
    }

    private void writeMethods(List<Method> methods) {
        methods.forEach(method -> writeMethod(method));
    }

    private void writeMethod(Method method) {
        textGen.writeTabs();
            textGen.write("virtual ");

        String returnTypeName = getCppReturnType(method);
        textGen.writeFormat("{0} {1} (", returnTypeName, method.name);
        String argText = method.buildArgumentsText();
        textGen.write(argText);
        textGen.writeLine(") = 0;");
    }

    public String getCppReturnType(Method method) {
        String returnTypeName = "void";
        if (method.returnType != null) {
            returnTypeName = method.returnType.generateCppDefinition();
        }
        return returnTypeName;
    }

    private void writeIfndefCode(InterfaceModel classModel) {

        String defineItem = definitionHeader(classModel);

        textGen.writeLineFormat("#ifndef {0}", defineItem);
        textGen.writeLineFormat("#define {0}", defineItem);
        textGen.writeLine();
        textGen.writeLine("#include \"../tranj.hpp\"");
        textGen.writeLine("using namespace std;");

        textGen.writeLine();
    }

    public String definitionHeader(InterfaceModel classModel) {
        return classModel.buildBaseName() + "_H";
    }

    private void writeVariables(List<Variable> variables) {
        variables.forEach(v -> {
            textGen.writeLineFormat("{0} {1};",
                    v.type.generateCppDefinition(),
                    v.name);
        });

    }
}
