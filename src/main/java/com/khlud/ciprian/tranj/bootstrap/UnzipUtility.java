package com.khlud.ciprian.tranj.bootstrap;

import java.io.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Ciprian on 10/28/2016.
 */
public class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @param isJava
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String destDirectory, Predicate<String> isJava) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();

            if(!entry.isDirectory() && isJava.test(entry.getName())){
                createPath(filePath);
                copyJavaFile(zipIn, entry, filePath);

            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void createPath(String filePath) {
        File fileTarget = new File(filePath);
        File parent = fileTarget.getParentFile();
        if(!parent.exists()){
            createPath(parent.getAbsolutePath());
        }
        parent.mkdir();
    }

    public static void copyJavaFile(ZipInputStream zipIn, ZipEntry entry, String filePath) throws IOException {
        if (!entry.isDirectory()) {
            // if the entry is a file, extracts it
            extractFile(zipIn, filePath);
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
