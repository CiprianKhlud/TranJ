package com.khlud.ciprian.tranj.classesmodel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cipriankhlud
 */
public class NameDefinition {

    public String name;
    public String packageName;
    public String module;
    public List<String> imports = new ArrayList<>();

    @NotNull
    public static String buildBaseName(String packageName, String name) {
        if ("".equals(packageName)) {
            return name;
        }
        String result = packageName.replace('.', '_') + "_" + name;
        return result;
    }

    @Contract(pure = true)
    public String buildBaseName() {
        return buildBaseName(packageName, name);
    }

}
