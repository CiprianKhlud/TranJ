package com.khlud.ciprian.tranj.classesmodel;

import com.khlud.ciprian.tranj.resolvers.CachedTypeResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cipriankhlud
 */
public class Module {

    public CachedTypeResolver moduleTypeResolver = new CachedTypeResolver();
    public String version;
    public String name;
    public List<ClassModel> classes = new ArrayList<>();
    public List<InterfaceModel> interfaces = new ArrayList<>();

    public List<FileModel> files = new ArrayList<>();

    public ClassModel createClass(String name, String packageName) {
        ClassModel result = new ClassModel();
        result.name = name;
        result.packageName = packageName;
        classes.add(result);
        return result;
    }

    public InterfaceModel createInterface(String name, String packageName) {
        InterfaceModel result = new InterfaceModel();
        result.name = name;
        result.packageName = packageName;
        interfaces.add(result);
        return result;
    }

    public FileModel createFileModel() {
        FileModel result = new FileModel(moduleTypeResolver);
        result.module = this;
        files.add(result);
        return result;
    }
}
