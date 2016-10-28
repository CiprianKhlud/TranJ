package com.khlud.ciprian.tranj.codegen;

import com.khlud.ciprian.tranj.classesmodel.NameDefinition;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

/**
 *
 * @author Ciprian
 */
public class TextGen {

    public File targetPath;
    String fileName;
    StringBuilder sb = new StringBuilder();
    int indentCount = 0;
    //4 spaces
    String indentText = "   ";

    public TextGen(File target) {
        targetPath = target;
    }

    @Contract(pure = true)
    public static List<String> splitTextByTokens(String text, char splitChar) {
        StringTokenizer tokenizer = new StringTokenizer(text, "" + splitChar);
        List<String> result = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }
        return result;
    }

    public static void writeEndPackage(NameDefinition classModel, TextGen writer) {
        List<String> namespaces = splitTextByTokens(classModel.packageName, '.');
        namespaces.forEach(ns -> {
            writer.write("} ");
        });
        writer.writeLine("");
    }

    public void writeToDisk() {
        String text = sb.toString();
        String fileNameBuilt = fileName;
        if (targetPath != null) {
            fileNameBuilt = targetPath.getAbsolutePath() + "/" + fileName;
        }
        try (PrintWriter out = new PrintWriter(fileNameBuilt)) {
            out.println(text);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        sb = new StringBuilder();
    }

    protected void indent() {
        writeLine("{ ");
        indentCount++;
    }

    protected void unindent() {
        unindent("}");
    }

    protected void unindent(String text) {
        indentCount--;
        writeTabs();
        writeLine(text);
    }

    protected void writeLine(String text) {
        sb.append(text)
                .append("\n");
    }

    TextGen write(String text) {
        sb.append(text);
        return this;
    }

    TextGen writeFormat(String text, Object... objects) {
        return write(MessageFormat.format(text, objects));
    }

    void writeLineFormat(String text, Object... objects) {
        writeTabs();
        write(MessageFormat.format(text, objects));
        writeLine();
    }

    protected void writeLine() {
        writeLine("");
    }

    public TextGen writeTabs() {
        IntStream.range(0, indentCount)
                .forEach(i -> {
                    write(indentText);
                });
        return this;
    }

    public void writePackage(NameDefinition classModel) {

        List<String> namespaces = TextGen.splitTextByTokens(classModel.packageName, '.');
        namespaces.forEach(ns -> {
            writeFormat("namespace {0} ", ns);
            write("{ ");
        });
        writeLine();
    }
}
