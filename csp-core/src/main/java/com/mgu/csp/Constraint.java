package com.mgu.csp;

import java.util.Set;

/**
 * A {@code Constraint} involves some subset of the variables of a CSP and specifies the allowable
 * combinations of values for that subset.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface Constraint {

    /**
     * Determines whether this {@code Constraint} is consistent with the current state of its dependent variables.
     *
     * @param dependentVariables
     *      Subset of variables of the CSP that this {@code Constraint} relies on (cf. {@link Constraint#reliesOn()}.
     *      The variables within this subset represent a subset of the current state (cf. {@code Assignment}) of
     *      the CSP.
     * @param <Type>
     *      represents the type of the domain values of the dependent {@code Variable}s
     * @return
     *      {@code true} if this {@code Constraint} is consistent with the given {@code Variable}s,
     *      {@code false} otherwise
     */
    <Type> boolean isConsistent(Set<Variable<Type>> dependentVariables);

    /**
     * Determines whether this {@code Constraint} is satisfied with the current state of its dependent variables.
     *
     * @param dependentVariables
     *      Subset of variables of the CSP that this {@code Constraint} relies on (cf. {@link Constraint#reliesOn()}.
     *      The variables within this subset represent a subset of the current state (cf. {@code Assignment}) of
     *      the CSP.
     * @param <Type>
     *      represents the type of the domain values of the dependent {@code Variable}s
     * @return
     *      {@code true} if this {@code Constraint} is satisfied with the given {@code Variable}s,
     *      {@code false} otherwise
     */
    <Type> boolean isSatisfied(Set<Variable<Type>> dependentVariables);

    /**
     * @return
     *      unmodifiable set of {@code VariableIdentity} that identifies the set of variables this particular
     *      {@code Constraint} relies on
     */
    Set<VariableIdentity> reliesOn();
}