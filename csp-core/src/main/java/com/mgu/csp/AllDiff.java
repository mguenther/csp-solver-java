package com.mgu.csp;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code AllDiff} constraint ensures that assigned variables the constraint relies on hold a unique value
 * with regard to each other. It also ensures that unassigned variables are not in a conflicting state. Suppose
 * variables X and Y are unassigned and share the same restricted domain of values D(X) = D(Y) = { c }. Both X
 * and Y are in a conflicted state and it is impossible to satisfy the constraint by further variable assignments.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class AllDiff implements Constraint {

    private final Set<VariableIdentity> reliesOn;

    public AllDiff(final Set<VariableIdentity> reliesOn) {
        this.reliesOn = reliesOn;
    }

    @Override
    public <Type> boolean isConsistent(final Set<Variable<Type>> dependentVariables) {
        return
                noConflictingUnassigned(dependentVariables) &&
                noDuplicatesAssigned(dependentVariables) &&
                unassignedCanBeAssigned(dependentVariables);
    }

    private <Type> boolean noDuplicatesAssigned(final Set<Variable<Type>> dependentVariables) {
        return dependentVariables
                .stream()
                .filter(variable -> variable.isAssigned())
                .map(variable -> variable.valueOf())
                .allMatch(new HashSet<>()::add);
    }

    private <Type> boolean unassignedCanBeAssigned(final Set<Variable<Type>> dependentVariables) {
        final List<Type> assignedValues = dependentVariables
                .stream()
                .filter(variable -> variable.isAssigned())
                .map(variable -> variable.valueOf())
                .collect(Collectors.toList());
        return dependentVariables
                .stream()
                .filter(variable -> !variable.isAssigned())
                .map(variable -> variable.domain())
                .allMatch(candidates -> candidates.stream().anyMatch(candidate -> !assignedValues.contains(candidate)));
    }

    private <Type> boolean noConflictingUnassigned(final Set<Variable<Type>> dependentVariables) {
        return dependentVariables
                .stream()
                .filter(variable -> !variable.isAssigned())
                .filter(variable -> variable.domain().size() == 1)
                .map(variable -> variable.domain().stream().findFirst().get()) // guaranteed to be present
                .allMatch(new HashSet<>()::add);
    }

    @Override
    public <Type> boolean isSatisfied(final Set<Variable<Type>> dependentVariables) {

        boolean allVariablesAssigned = dependentVariables
                .stream()
                .map(variable -> variable.valueOf())
                .allMatch(value -> value != null);

        return allVariablesAssigned && isConsistent(dependentVariables);
    }

    @Override
    public Set<VariableIdentity> reliesOn() {
        return Collections.unmodifiableSet(reliesOn);
    }

    public static AllDiff on(final VariableIdentity... identities) {
        final Set<VariableIdentity> reliesOn = new HashSet<>();
        for (VariableIdentity identity : identities) {
            reliesOn.add(identity);
        }
        return new AllDiff(reliesOn);
    }
}