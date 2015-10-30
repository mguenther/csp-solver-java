package com.mgu.csp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Performs a backtracking kind of search by progressing along the state space in a depth-first manner.
 * The solver can make use of user-supplied heuristics for picking the next unassigned variable
 * (cf. {@link VariableOrdering}) and ordering the set of domain values for such an unassigned variable
 * (cf. {@link ValueOrdering}).
 *
 * By default, this backtracking uses uninformed heuristics for {@code VariableOrdering} and
 * {@code ValueOrdering}.
 *
 * @param <Type>
 *     parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class DFSSolver<Type> {

    private final List<Consumer<Assignment<Type>>> listeners = new ArrayList<>();

    private final VariableOrdering<Type> variableOrdering;

    private final ValueOrdering<Type> valueOrdering;

    /**
     * Uses uninformed heuristics for {@link VariableOrdering} and {@link ValueOrdering}.
     */
    public DFSSolver() {
        this(new VariableOrdering<Type>() {}, new ValueOrdering<Type>() {});
    }

    /**
     * Uses the given {@link VariableOrdering} and the uninformed default implementation of
     * {@link ValueOrdering}.
     *
     * @param variableOrdering
     *      represents a heuristic for picking the next unassigned variable
     */
    public DFSSolver(final VariableOrdering<Type> variableOrdering) {
        this(variableOrdering, new ValueOrdering<Type>() {});
    }

    /**
     * Uses the given {@link ValueOrdering} and the uninformed default implementation of
     * {@link VariableOrdering}.
     *
     * @param valueOrdering
     *      represents a heuristic for ordering domain values
     */
    public DFSSolver(final ValueOrdering<Type> valueOrdering) {
        this(new VariableOrdering<Type>() {}, valueOrdering);
    }

    /**
     * Uses the given {@link VariableOrdering} and {@link ValueOrdering}.
     *
     * @param variableOrdering
     *      representts a heuristic for picking the next unassigned variable
     * @param valueOrdering
     *      represents a heuristic for ordering domain values
     */
    public DFSSolver(final VariableOrdering<Type> variableOrdering, final ValueOrdering<Type> valueOrdering) {
        this.variableOrdering = variableOrdering;
        this.valueOrdering = valueOrdering;
    }

    /**
     * Solves the given CSP by performing a depth-first search starting off from the initial state (the
     * initial assignment).
     *
     * @param csp
     *      represents the CSP to solve
     * @return
     *      an {@link Assignment} that is completed, or {@code null} if no such {@link Assignment} can be found
     */
    public Optional<Assignment<Type>> solve(final CSP<Type> csp) {
        return solve(csp, csp.initialAssignment());
    }

    private Optional<Assignment<Type>> solve(final CSP<Type> csp, final Assignment<Type> assignment) {

        if (csp.isSatisfied(assignment)) {
            return Optional.of(assignment);
        }

        final Variable<Type> unassignedVariable = variableOrdering.selectUnassignedVariable(assignment);
        final Set<Constraint> constraints = csp.constraints();

        return valueOrdering
                .orderedDomain(unassignedVariable, constraints)
                .stream()
                .map(value -> assignment.assign(unassignedVariable, value, constraints))
                .filter(csp::isConsistent)
                .peek(this::notifyListeners)
                .map(consistentAssignment -> solve(csp, consistentAssignment))
                .findFirst()
                .get();
    }

    private void notifyListeners(final Assignment<Type> assignment) {
        listeners.forEach(listener -> listener.accept(assignment));
    }

    public void addListener(final Consumer<Assignment<Type>> listener) {
        this.listeners.add(listener);
    }
}