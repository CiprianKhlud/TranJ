package com.khlud.ciprian.tranj.builders;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author cipriankhlud
 */
public class FolderUtils {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String[] getDirectoryFiles(String path, Predicate<File> isValidFile) {

        File file = new File(path);
        if (!file.exists()) {
            return EMPTY_STRING_ARRAY;
        }

        List<File> resultList = new ArrayList<>();

        try {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    String filePathName = filePath.toString();
                    resultList.add(new File(filePathName));
                }
            });
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return getStringsOfArray(resultList, isValidFile);
    }

    private static String[] getStringsOfArray(List<File> resultList, Predicate<File> isValidFile) {

        List<String> items = resultList.stream()
                .filter(isValidFile)
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
        String[] result = new String[items.size()];
        items.toArray(result);
        return result;
    }
}
