package com.mgu.csp;

import java.util.Set;

/**
 * A {@code ValueOrdering} implements a strategy to order the domain of any given
 * {@link Variable}.
 *
 * @param <Type>
 *     parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface ValueOrdering<Type> {

    /**
     * Orders the remaining set of domain values of the given {@link Variable} with regard
     * to the implemented strategy. Implementing classes can make use of the set of
     * {@link Constraint}s to make better decisions on how to order the domain.
     *
     * The default implementation preserves the original order of domain values from the
     * given {@link Variable}.
     *
     * @param variable
     *      this is the variablle whose domain shall be ordered with regard to the
     *      implemented strategy
     * @param constraints
     *      {@code Set} of {@link Constraint}s of a CSP that this value ordering strategy
     *      can make use of to make informed decisions on how to order the domain of the
     *      given {@link Variable}
     * @return
     *      ordered {@code Set} of {@link Variable}s
     */
    default Set<Type> orderedDomain(Variable<Type> variable, Set<Constraint> constraints) {
        return variable.domain();
    }
}