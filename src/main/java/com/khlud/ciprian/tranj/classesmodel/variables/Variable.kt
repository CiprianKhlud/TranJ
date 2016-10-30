package com.khlud.ciprian.tranj.classesmodel.variables

import com.github.javaparser.ast.expr.Expression
import com.khlud.ciprian.tranj.resolvers.ResolvedType

/**
 * Created by Ciprian on 10/31/2016.
 */
open class NamedVariable {

    var name = ""
    var type: ResolvedType? = null
}

class Variable : NamedVariable() {

    var isStatic: Boolean = false
    var initialValue: Expression? = null
}

class Argument : NamedVariable()
