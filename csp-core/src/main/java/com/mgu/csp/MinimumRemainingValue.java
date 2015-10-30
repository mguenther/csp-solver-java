package com.mgu.csp;

/**
 * This {@code VariableOrdering} selects the unassigned {@link Variable} that has the fewest
 * legal values left. It is also known as the "most-constrained value" or "fail-first" heuristic,
 * because it select a variable that is most likely to cause a failure soon, thereby pruning the
 * search tree.
 *
 * @param <Type>
 *     parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class MinimumRemainingValue<Type> implements VariableOrdering<Type> {

    @Override
    public Variable<Type> selectUnassignedVariable(final Assignment<Type> assignment) {
        return assignment
                .unassignedVariables()
                .stream()
                .sorted((a, b) -> a.domain().size() - b.domain().size())
                .findFirst()
                .get();
    }
}