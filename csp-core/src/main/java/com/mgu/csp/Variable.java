package com.mgu.csp;

import java.util.HashSet;
import java.util.Set;

/**
 * Each variable X_i in a CSP has a non-empty domain D_i of possible values. Domain values are discrete and finite.
 * Variables can be part of a partial assignment.
 *
 * This class is immutable.
 *
 * @param <Type>
 *     parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Variable<Type> {

    private final VariableIdentity identity;

    private final Type assignedValue;

    private final Set<Type> domain;

    private Variable(final VariableIdentity identity, final Type assignedValue, final Set<Type> domain) {
        this.identity = identity;
        this.assignedValue = assignedValue;
        this.domain = domain;
    }

    public VariableIdentity identity() {
        return this.identity;
    }

    public Set<Type> domain() {
        return domain;
    }

    public Type valueOf() {
        return assignedValue;
    }

    public boolean isAssigned() {
        return assignedValue != null;
    }

    /**
     * Removes the given value from the domain of possible values. This operation is only admissible if the
     * variable is still unassigned. This method will immediately return if the domain does not contain the
     * given value, since we are already in a converged state with regard to this operation.
     *
     * @param restrictByValue
     *      the value that ought to be removed from the set of possible domain values
     * @throws IllegalStateException
     *      if the variable has already been assigned a value
     * @return
     *      copy of this {@code Variable} with an updated set of remaining domain values
     */
    public Variable<Type> restrict(final Type restrictByValue) {
        if (isAssigned()) {
            throw new IllegalStateException("Unable to restrict domain values since the variable has already been " +
                                            "assigned a value.");
        }
        if (!this.domain.contains(restrictByValue)) {
            // do nothing, since we already are in converged state
            return this;
        }
        final Set<Type> shallowCopyOfDomain = new HashSet<>(domain);
        shallowCopyOfDomain.remove(restrictByValue);
        return new Variable<>(identity, assignedValue, shallowCopyOfDomain);
    }

    /**
     * Assigns the given value to the variable and clears its list of domain values.
     *
     * @param value
     *      the value that is assigned to this {@code Variable}
     * @throws IllegalStateException
     *      if the value is not in the set of possible domain values
     * @return
     *      copy of this {@code Variable} with an assigned value and cleared domain values
     */
    public Variable<Type> assign(final Type value) {
        if (!this.domain.contains(value)) {
            throw new IllegalStateException("Unable to assign value " + value + " to variable " + identity + ". " +
                                            "Value is not in the set of remaining domain values.");
        }
        return new Variable<>(identity, value, new HashSet<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable<?> variable = (Variable<?>) o;

        return identity.equals(variable.identity);
    }

    @Override
    public int hashCode() {
        return identity.hashCode();
    }

    public static <Type> Variable<Type> unassignedVariable(
            final VariableIdentity identity,
            final Set<Type> initialDomain) {
        return new Variable<>(identity, null, initialDomain);
    }

    public static <Type> Variable<Type> assignedVariable(
            final VariableIdentity identity,
            final Type assignedValue) {
        return new Variable<>(identity, assignedValue, new HashSet<>());
    }
}