package com.khlud.ciprian.tranj.codegen;

import com.khlud.ciprian.tranj.classesmodel.ClassModel;
import com.khlud.ciprian.tranj.classesmodel.InterfaceModel;

import java.io.File;

/**
 * Created by Ciprian on 10/22/2016.
 */
public class QtProjectWriter {

    TextGen textGen;

    public QtProjectWriter(File ouputDir) {
        textGen = new TextGen(ouputDir);
        textGen.writeLine("QT += core\n"
                + "QT -= gui\n"
                + "\n"
                + "CONFIG += c++11\n"
                + "\n"
                + "TARGET = TargetApp\n"
                + "CONFIG += console\n"
                + "CONFIG -= app_bundle\n"
                + "\n"
                + "TEMPLATE = app\n");
    }

    public void writeToDiskInFile(String fileName) {
        textGen.fileName = fileName;
        textGen.writeToDisk();
    }

    public void writeCode(ClassModel cls) {
        textGen.writeLineFormat("SOURCES += {0}.cpp", cls.buildBaseName());

    }

    public void writeCode(InterfaceModel cls) {
        textGen.writeLineFormat("SOURCES += {0}.cpp", cls.buildBaseName());

    }
}
