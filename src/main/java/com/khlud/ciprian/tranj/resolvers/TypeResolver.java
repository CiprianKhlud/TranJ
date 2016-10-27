package com.khlud.ciprian.tranj.resolvers;

import com.github.javaparser.ast.type.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.System.out;

/**
 *
 * @author cipriankhlud
 */
public class TypeResolver {

    ReflectionResolver reflectionResolver;

    public void buildReflectionResolver(List<String> imports) {
        reflectionResolver = new ReflectionResolver(imports);
    }

    public ResolvedType resolve(Type typeToResolve) {
        String typeName = typeToResolve.toStringWithoutComments();

        ResolvedType result = resolveTypeSlow(typeToResolve);

        return result;
    }

    @Nullable
    private ResolvedType resolveTypeSlow(Type typeToResolve) {
        if (typeToResolve instanceof ReferenceType) {
            return resolveReferenceType((ReferenceType) typeToResolve);
        }
        if (typeToResolve instanceof ClassOrInterfaceType) {
            return resolveClassType((ClassOrInterfaceType) typeToResolve);
        }
        if (typeToResolve instanceof PrimitiveType) {
            return resolvePrimitiveType((PrimitiveType) typeToResolve);
        }
        if (typeToResolve instanceof WildcardType) {
            return resolveWildcardType((WildcardType) typeToResolve);
        }
        if (typeToResolve instanceof VoidType) {
            return null;
        }
        out.println("Should never be here for type: " + typeToResolve);
        return null;
    }

    private ResolvedType resolveWildcardType(WildcardType typeToResolve) {
        return new ResolvedType("?", "", TypeCode.Generic);
    }

    static String primitiveTypeToCppMapping(String javaType) {
        switch (javaType) {
            case "Boolean":
                return "bool";
            default:
                return javaType.toLowerCase();
        }
    }

    private ResolvedType resolvePrimitiveType(PrimitiveType typeToResolve) {
        String cppPrimitiveType = primitiveTypeToCppMapping(typeToResolve.getType().name());
        ResolvedType resolvedType = new ResolvedType(cppPrimitiveType, "", TypeCode.Value);
        return resolvedType;
    }

    private ResolvedType resolveReferenceType(ReferenceType referenceType) {
        if (referenceType.toStringWithoutComments().endsWith("[]")) {
            ResolvedType arrayTarget = resolve(referenceType.getType());
            ResolvedType classTypeResolved = new ResolvedType("",
                    "", TypeCode.Array);
            classTypeResolved.dependantTypes.add(arrayTarget);
            return classTypeResolved;
        }
        ResolvedType includedType = resolve(referenceType.getType());
        return includedType;
    }

    private ResolvedType resolveClassType(ClassOrInterfaceType classOrInterfaceType) {
        String typeName = classOrInterfaceType.getName();
        Class resolvedTypeReflection = reflectionResolver.resolve(typeName);

        ResolvedType classTypeResolved = new ResolvedType(classOrInterfaceType.getName(),
                "", TypeCode.Object);
        if (resolvedTypeReflection != null) {

            classTypeResolved = new ResolvedType(resolvedTypeReflection.getSimpleName(),
                    resolvedTypeReflection.getPackage().getName(), TypeCode.Object);
        }
        final List<Type> typeArgs = classOrInterfaceType.getTypeArgs();
        if (typeArgs != null) {
            ResolvedType finalClassTypeResolved = classTypeResolved;
            typeArgs
                    .forEach(typeArg -> {
                        ResolvedType genericResolvedType = resolve(typeArg);
                        finalClassTypeResolved.genericTypes.add(genericResolvedType);
                    });
        }
        return classTypeResolved;
    }
}
