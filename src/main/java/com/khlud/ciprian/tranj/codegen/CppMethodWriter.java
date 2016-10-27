package com.khlud.ciprian.tranj.codegen;

import com.khlud.ciprian.tranj.classesmodel.ClassModel;
import com.khlud.ciprian.tranj.classesmodel.Method;

/**
 *
 * @author cipriankhlud
 */
public class CppMethodWriter {

    private final TextGen _textGen;

    public CppMethodWriter(TextGen textGen) {
        _textGen = textGen;
    }

    public void writeMethod(ClassModel classModel, Method method) {

        String returnType = method.getCppReturnType();
        _textGen.write(returnType);
        _textGen.write(" ");
        _textGen.write(classModel.name);
        _textGen.write("::");
        _textGen.write(method.name);
        _textGen.write("(");
        _textGen.write(method.buildArgumentsText());
        _textGen.write(")");
        _textGen.indent();

        _textGen.unindent();
    }
}
