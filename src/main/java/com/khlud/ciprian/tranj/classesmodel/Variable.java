package com.khlud.ciprian.tranj.classesmodel;

import com.github.javaparser.ast.expr.Expression;

/**
 * Created by cipriankhlud on 19/10/2016.
 */
public class Variable extends NamedVariable {

    public boolean isStatic;
    public Expression initialValue;
}
