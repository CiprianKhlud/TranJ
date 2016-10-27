package com.khlud.ciprian.tranj.classesmodel;

import com.github.javaparser.ast.type.Type;
import com.khlud.ciprian.tranj.resolvers.ResolvedType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author cipriankhlud
 */
public class ClassModel extends NameDefinition {

    public NameDefinition baseClass;
    public List<NameDefinition> interfaces = new ArrayList<>();
    public Set<ResolvedType> referencedTypes = new HashSet<>();

    public List<Variable> variables = new ArrayList<>();

    public List<Method> methods = new ArrayList<>();

    public Variable addVariable(String name, Type type, FileModel fileModel) {
        Variable result = new Variable();
        result.name = name;
        result.type = fileModel.resolvedType(type);
        variables.add(result);
        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Method createMethod() {

        Method result = new Method();
        methods.add(result);
        return result;
    }

    public void addReferencedType(ResolvedType resolvedByModule) {
        if ("".equals(resolvedByModule.packageName)) {
            return;
        }
        referencedTypes.add(resolvedByModule);
    }

    public void buildReferencedTypesTable() {
        variables.forEach(variable -> addReferencedType(variable.type));
        methods.forEach(
                method -> {
                    if (method.returnType != null) {
                        addReferencedType(method.returnType);
                    }
                    method.parameters.forEach(par -> {
                        addReferencedType(par.type);
                    });
                }
        );
    }
}
