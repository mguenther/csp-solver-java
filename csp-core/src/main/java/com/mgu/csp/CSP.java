package com.mgu.csp;

import java.util.Set;

abstract public class CSP<Type> {

    final public boolean isConsistent(final Assignment<Type> assignment) {
        return assignment.isConsistent(constraints());
    }

    final public boolean isSatisfied(final Assignment<Type> assignment) {
        return assignment.isSatisfied(constraints());
    }

    abstract protected Assignment<Type> initialAssignment();

    abstract protected Set<Constraint> constraints();
}