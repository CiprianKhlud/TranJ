package com.khlud.ciprian.tranj.classesmodel;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.google.common.base.Joiner;
import com.khlud.ciprian.tranj.classesmodel.statements.MethodBody;
import com.khlud.ciprian.tranj.resolvers.ResolvedType;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ciprian on 10/20/2016.
 */
public class Method {

    public List<Argument> parameters = new ArrayList<>();
    public String name;
    public ResolvedType returnType;
    public boolean isStatic;
    private MethodBody methodBody;

    public void addParameter(Parameter p, FileModel typeResolver) {
        Argument argument = new Argument();
        argument.name = p.getId().getName();
        argument.type = typeResolver.resolvedType(p.getType());
        parameters.add(argument);
    }

    public String getCppReturnType() {
        String returnTypeName = "void";
        if (returnType != null) {
            returnTypeName = returnType.generateCppDefinition();
        }
        return returnTypeName;
    }

    public void fillMethodProperties(MethodDeclaration member, FileModel typeResolver) {
        List<Parameter> params = member.getParameters();
        if (params != null) {
            params.forEach(p -> {
                addParameter(p, typeResolver);
            });
        }
        name = member.getName();
        returnType = typeResolver.resolvedType(member.getType());
        isStatic = ModifierSet.isStatic(member.getModifiers());
        MethodBody methodBody = buildMethodBody(member.getBody());
        this.methodBody = methodBody;
    }

    private MethodBody buildMethodBody(BlockStmt body) {
        if (body == null) {
            return null;
        }
        MethodBody result = new MethodBody();
        result.statementRoot = body;
        return result;
    }

    @NotNull
    public String buildArgumentsText() {
        List<String> argumentTexts = parameters
                .stream()
                .map(p -> MessageFormat.format("{0} {1}", p.type.generateCppDefinition(), p.name))
                .collect(Collectors.toList());
        return Joiner.on(", ").join(argumentTexts);
    }

    @Override
    public String toString() {
        return name + ":" + returnType;
    }
}
