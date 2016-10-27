package com.khlud.ciprian.tranj.resolvers;

import com.github.javaparser.ast.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ciprian on 10/22/2016.
 */
public class CachedTypeResolver {

    Map<String, ResolvedType> _resolvedTypes = new HashMap<>();
    Map<String, ResolvedType> _resolvedSimpleTypes = new HashMap<>();

    public void registerClassType(String packageName, String className) {
        String typeName = getCombinedTypeName(packageName, className);
        ResolvedType resolvedType = new ResolvedType(className, packageName, TypeCode.Object);
        _resolvedSimpleTypes.put(className, resolvedType);
        registerType(typeName, resolvedType);
    }

    public String getCombinedTypeName(String packageName, String className) {
        String typeName = className;
        if (!"".equals(packageName)) {
            typeName = packageName + "." + className;
        }
        return typeName;
    }

    public ResolvedType resolve(Type typeToResolve) {
        String typeName = typeToResolve.toStringWithoutComments();
        if (_resolvedTypes.containsKey(typeName)) {
            return _resolvedTypes.get(typeName);
        }
        if (_resolvedSimpleTypes.containsKey(typeName)) {
            return _resolvedSimpleTypes.get(typeName);
        }
        return null;
    }

    public void registerType(String typeName, ResolvedType resolvedType) {
        _resolvedTypes.put(typeName, resolvedType);
    }
}
