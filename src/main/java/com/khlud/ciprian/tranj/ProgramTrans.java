package com.khlud.ciprian.tranj;

import com.github.javaparser.ParseException;
import com.khlud.ciprian.tranj.builders.ModelBuilder;
import com.khlud.ciprian.tranj.classesmodel.Module;
import com.khlud.ciprian.tranj.codegen.HeaderInterfaceGen;
import com.khlud.ciprian.tranj.codegen.QtProjectWriter;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author cipriankhlud
 */
public class ProgramTrans {

    public static void main(String[] args)
            throws IOException, ParseException {
        long start = System.currentTimeMillis();
        ModelBuilder modelBuilder = new ModelBuilder("bootstrap");
        modelBuilder.process();

        String outputSrc = "OutputSrc";
        File ouputDir = prepareOutputFolder(outputSrc);
        Module module = modelBuilder.getModule();
        writeCode(ouputDir, module);
        long endTime = System.currentTimeMillis();
        System.out.println("Time to process: "+(endTime-start) + " ms");
    }

    @NotNull
    public static File prepareOutputFolder(String outputSrc) throws IOException {
        FileUtils.deleteDirectory(new File(outputSrc));
        File ouputDir = new File(outputSrc);
        ouputDir.mkdir();
        return ouputDir;
    }

    private static void writeCode(File ouputDir, Module module) {
  /*      HeaderClassGen headerClassGen = new HeaderClassGen(ouputDir);
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
        */
        HeaderInterfaceGen headerClassGen = new HeaderInterfaceGen(ouputDir);
        //CppBodyClassGen cppBodyClassGen = new CppBodyClassGen(ouputDir);
        QtProjectWriter qtProjectWriter = new QtProjectWriter(ouputDir);
        module.interfaces.forEach(cls -> {
            //cls.buildReferencedTypesTable();
            String targetPath = cls.buildBaseName() + ".hpp";
            headerClassGen.writeCode(cls, targetPath);

            String targetPathCpp = cls.buildBaseName() + ".cpp";
            //cppBodyClassGen.writeCode(cls, targetPathCpp);

            qtProjectWriter.writeCode(cls);

        });
        qtProjectWriter.writeToDiskInFile("tranj.pro");
    }
}
