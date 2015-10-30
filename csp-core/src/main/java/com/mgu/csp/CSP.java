package com.mgu.csp;

import java.util.Set;

/**
 * This is the abstract representation of a CSP. A CSP is defined by a set of variables X_i (through
 * its initial assignment) and a set of constraints. It provides boolean accessors to determine whether
 * a given assignment is consistent or satisfied with its definition. A solution to the CSP is an
 * assignment that is both complete and satisfies all constraints.
 *
 * @param <Type>
 *     parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
abstract public class CSP<Type> {

    /**
     * Determines whether the given {@link Assignment} is consistent with regard to the {@link #constraints()}
     * of this CSP.
     *
     * @param assignment
     *      an {@link Assignment} represents the current state of this CSP
     * @return
     *      {@code true} if the given {@link Assignment} is consistent, {@code false} otherwise
     */
    final public boolean isConsistent(final Assignment<Type> assignment) {
        return assignment.isConsistent(constraints());
    }

    /**
     * Determines whether the given {@link Assignment} is satisfied with regard to the {@link #constraints()}
     * of this CSP.
     *
     * @param assignment
     *      an {@link Assignment} represents the current state of this CSP
     * @return
     *      {@code true} if the given {@link Assignment} is satisfied, {@code false} otherwise
     */
    final public boolean isSatisfied(final Assignment<Type> assignment) {
        return assignment.isSatisfied(constraints());
    }

    /**
     * @return
     *      Yields the initial assignment for this CSP
     */
    abstract protected Assignment<Type> initialAssignment();

    /**
     * @return
     *      Yields the set of constraints for this CSP
     */
    abstract protected Set<Constraint> constraints();
}