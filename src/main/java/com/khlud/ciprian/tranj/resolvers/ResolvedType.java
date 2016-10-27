package com.khlud.ciprian.tranj.resolvers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cipriankhlud
 */
public class ResolvedType {

    public final String name;
    public final String packageName;
    private final TypeCode typeCode;
    public List<ResolvedType> dependantTypes = new ArrayList<>();
    public List<ResolvedType> genericTypes = new ArrayList<>();

    public ResolvedType(String name, String packageName, TypeCode typeCode) {
        this.packageName = packageName;
        this.typeCode = typeCode;
        this.name = name;
    }

    public String generateCppDefinition() {
        switch (typeCode) {
            case Array:
                return getArrayCppDefinition(dependantTypes.get(0));
            case Object:
                return getSharedPtr();
        }
        return name;
    }

    @NotNull
    public static String buildReferenceName(String packageName, String name) {
        if ("".equals(packageName)) {
            return name;
        }
        String result = packageName.replace(".", "::") + "::" + name;
        return result;
    }

    private String getSharedPtr() {
        String baseName = buildReferenceName(packageName, name);

        return "sptr<" + baseName + ">";
    }

    private String getArrayCppDefinition(ResolvedType arrayType) {
        return "sptr<ArrayOf<" + arrayType.generateCppDefinition() + " > >";
    }

    @Override
    public int hashCode() {
        String fullName = packageName + "." + name;
        return fullName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResolvedType)) {
            return false;
        }
        ResolvedType other = (ResolvedType) o;
        return getSharedPtr().equals(other.getSharedPtr());
    }

    @Override
    public String toString() {
        return generateCppDefinition();
    }

}
