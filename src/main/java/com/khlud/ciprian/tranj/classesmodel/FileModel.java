package com.khlud.ciprian.tranj.classesmodel;

import com.github.javaparser.ast.type.Type;
import com.khlud.ciprian.tranj.resolvers.CachedTypeResolver;
import com.khlud.ciprian.tranj.resolvers.ResolvedType;
import com.khlud.ciprian.tranj.resolvers.TypeResolver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cipriankhlud
 */
public class FileModel {

    private final CachedTypeResolver moduleTypeResolver;
    public List<String> imports = new ArrayList<>();
    public String packageName;
    public Module module;

    public TypeResolver typeResolver;

    public FileModel(CachedTypeResolver moduleTypeResolver) {

        typeResolver = new TypeResolver();
        this.moduleTypeResolver = moduleTypeResolver;
    }

    public void setupTypeResolver() {
        typeResolver.buildReflectionResolver(imports);
    }

    public ResolvedType resolvedType(Type typeToResolve) {
        ResolvedType resolvedByModule = moduleTypeResolver.resolve(typeToResolve);
        if (resolvedByModule != null) {
            return resolvedByModule;
        }
        ResolvedType resolvedByResolver = typeResolver.resolve(typeToResolve);
        moduleTypeResolver.registerType(typeToResolve.toStringWithoutComments(), resolvedByResolver);

        return resolvedByResolver;
    }

}
