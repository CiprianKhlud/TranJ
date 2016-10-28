package com.khlud.ciprian.tranj.classesmodel;

import com.khlud.ciprian.tranj.resolvers.ResolvedType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author cipriankhlud
 */
public class InterfaceModel extends NameDefinition {

    public Set<ResolvedType> referencedTypes = new HashSet<>();
    public List<Method> methods = new ArrayList<>();
}
