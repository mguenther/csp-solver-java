package com.mgu.csp;

/**
 * A {@code VariableOrdering} implements a strategy to fetch the next unassigned
 * {@link Variable}.
 *
 * @param <Type>
 *     parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface VariableOrdering<Type> {

    /**
     * Selects the next unassigned variable of the given {@link Assignment} with
     * regard to the implemented strategy.
     *
     * The default implementation simply selects the next unassigned variable.
     *
     * @param assignment
     *      represents the current state of a CSP
     * @return
     *      unassigned variable
     */
    default Variable<Type> selectUnassignedVariable(Assignment<Type> assignment) {
        return assignment.unassignedVariables().stream().findFirst().get();
    }
}