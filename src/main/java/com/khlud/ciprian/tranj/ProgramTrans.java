package com.khlud.ciprian.tranj;

import com.github.javaparser.ParseException;
import com.khlud.ciprian.tranj.builders.ModelBuilder;
import com.khlud.ciprian.tranj.classesmodel.Module;
import com.khlud.ciprian.tranj.codegen.CppBodyClassGen;
import com.khlud.ciprian.tranj.codegen.HeaderClassGen;
import com.khlud.ciprian.tranj.codegen.QtProjectWriter;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author cipriankhlud
 */
public class ProgramTrans {

    public static void main(String[] args)
            throws IOException, ParseException {

        ModelBuilder modelBuilder = new ModelBuilder(".");
        modelBuilder.process();

        File ouputDir = new File("OutputSrc");
        ouputDir.mkdir();
        Module module = modelBuilder.getModule();
        writeCode(ouputDir, module);
    }

    private static void writeCode(File ouputDir, Module module) {
        HeaderClassGen headerClassGen = new HeaderClassGen(ouputDir);
        CppBodyClassGen cppBodyClassGen = new CppBodyClassGen(ouputDir);
        QtProjectWriter qtProjectWriter = new QtProjectWriter(ouputDir);

        module.classes.forEach(cls -> {
            cls.buildReferencedTypesTable();
            String targetPath = cls.buildBaseName() + ".hpp";
            headerClassGen.writeCode(cls, targetPath);

            String targetPathCpp = cls.buildBaseName() + ".cpp";
            cppBodyClassGen.writeCode(cls, targetPathCpp);

            qtProjectWriter.writeCode(cls);

        });
        qtProjectWriter.writeToDiskInFile("tranj.pro");
    }
}
