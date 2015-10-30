package com.mgu.csp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A state of the CSP is defined by an assignment of values to some or all of the
 * variables. An assignment that does not violate any constraints is called a consistent
 * or legal assignment. A complete assignment is one in which every variable is mentioned.
 * A solution to the CSP is a complete assignment which does not violate any constraints.
 *
 * This {@code Assignment} applies forward checking. Thus, whenever a {@code Variable} is
 * assigned to a value, that value is removed from the domain of all dependent unassigned variables
 *
 * This class is immutable.
 *
 * @param <Type>
 *      parameterized type of domain values
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Assignment<Type> {

    private final Map<VariableIdentity, Variable<Type>> variableAssignments;

    private Assignment(final Map<VariableIdentity, Variable<Type>> variableAssignments) {
        this.variableAssignments = variableAssignments;
    }

    /**
     * An assignment that does not violate any constraint is called consistent.
     *
     * @param constraints
     *      {@code Set} of {@code Constraint}s of a CSP that this particular assignment
     *      should be checked against for consistency
     * @return
     *      {@code true} if this assignment is consistent with regard to the given
     *      {@code Constraint}s, {@code false} otherwise
     */
    public boolean isConsistent(final Set<Constraint> constraints) {
        return constraints
                .stream()
                .allMatch(constraint -> constraint.isConsistent(subsetOf(constraint.reliesOn())));
    }

    /**
     * A complete assignment is one in which every variable of the CSP is mentioned.
     *
     * @return
     *      {@code true} if this assignment is complete with regard to the given
     *      {@code Set} of {@code Variable}s, {@code false} otherwise
     */
    public boolean isComplete() {
        return variableAssignments
                .values()
                .stream()
                .allMatch(variable -> variable.isAssigned());
    }

    /**
     * A solution to a CSP is a complete assignment that satisfies all constraints.
     *
     * @param constraints
     *      {@code Set} of {@code Constraint}s of a CSP that this particular assignment
     *      should be checked against
     * @return
     *      {@code true} if this assignment is satisfied with regard to the given
     *      [@code Constraint}s, {@code false} otherwise
     */
    public boolean isSatisfied(final Set<Constraint> constraints) {
        return isComplete() && constraints
                .stream()
                .allMatch(constraint -> constraint.isSatisfied(subsetOf(constraint.reliesOn())));
    }

    /**
     * Assigns the value of type {@code Type} to the given variable. The value must be in the domain of that
     * variable, otherwise the assignment will fail.
     *
     * @param variable
     *      this is the variable that the given value will be assigned to
     * @param value
     *      this is the value that will be assigned to the variable
     * @return
     *      copy of this {@code Assignment} with the additional variable
     *      assignment based on the given parameters
     */
    public Assignment<Type> assign(final Variable<Type> variable, final Type value, final Set<Constraint> constraints) {
        final Variable<Type> assignedVariable = variable.assign(value);
        final Map<VariableIdentity, Variable<Type>> shallowCopyOfVariableAssignments = new HashMap<>(this.variableAssignments);
        shallowCopyOfVariableAssignments.put(assignedVariable.identity(), assignedVariable);
        Assignment<Type> assignment = new Assignment<>(shallowCopyOfVariableAssignments);
        // missing foldLeft here so that we could reduce + combine variable identities to assignments
        for (VariableIdentity variableIdentity : dependentVariables(variable, constraints)) {
            assignment = assignment.restrict(variableIdentity, value);
        }
        return assignment;
    }

    private Set<VariableIdentity> dependentVariables(final Variable<Type> variable, final Set<Constraint> constraints) {
        return constraints
                .stream()
                .filter(constraint -> constraint.reliesOn().contains(variable.identity()))
                .map(dependentConstraint -> dependentConstraint.reliesOn())
                .flatMap(Set::stream)
                .collect(Collectors.toSet())
                .stream()
                .filter(variableIdentity -> !variableIdentity.equals(variable.identity()))
                .filter(variableIdentity -> !variableAssignments.get(variableIdentity).isAssigned())
                .collect(Collectors.toSet());
    }

    /**
     * Restricts the domain of the given variable by setting it to the provided {@code Set} of restricted
     * domain values. This must be a subset of the set of admissible domain values for the variable.
     *
     * Please note that any variable that has a restricted set of possible domain values and not a fixed assignment
     * is an unassigned variable.
     *
     * @param variableIdentity
     *      uniquely identifies a variable within this {@code Assignment}
     * @param restrictByValue
     *      the value that ought to be removed from the domain of the variable identified by the given identity
     * @return
     *      copy of this {@code Assignment} with the updated domain for the referenced variable
     */
    public Assignment<Type> restrict(final VariableIdentity variableIdentity, final Type restrictByValue) {
        final Variable<Type> variable = variableAssignments.get(variableIdentity);
        final Map<VariableIdentity, Variable<Type>> shallowCopyOfVariableAssignments = new HashMap<>(this.variableAssignments);
        shallowCopyOfVariableAssignments.put(variableIdentity, variable.restrict(restrictByValue));
        return new Assignment<>(shallowCopyOfVariableAssignments);
    }

    private Set<Variable<Type>> subsetOf(final Set<VariableIdentity> variableIdentities) {
        return Collections.unmodifiableSet(variableIdentities
                .stream()
                .map(variableIdentity -> variableAssignments.get(variableIdentity))
                .collect(Collectors.toSet()));
    }

    /**
     * @return
     *      Unmodifiable {@link Set} of all {@link Variable}s that are not assigned.
     */
    public Set<Variable<Type>> unassignedVariables() {
        return Collections.unmodifiableSet(variableAssignments
                .values()
                .stream()
                .filter(variable -> !variable.isAssigned())
                .collect(Collectors.toSet()));
    }

    /**
     * Determines whether the given variable is assigned.
     *
     * @param variableIdentity
     *      Uniquely identifies a variable within the assignment
     * @return
     *      {@code true} if the variable has been assigned a value (restricted its domain
     *      to a single value), {@code false} otherwise
     */
    public boolean isAssigned(final VariableIdentity variableIdentity) {
        return this.variableAssignments.containsKey(variableIdentity) && this.variableAssignments.get(variableIdentity).isAssigned();
    }

    public Type valueOf(final VariableIdentity variableIdentity) {
        return this.variableAssignments.get(variableIdentity).valueOf();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Assigned variables:\n");
        variableAssignments
                .values()
                .stream()
                .filter(variable -> variable.isAssigned())
                .forEach(variable -> sb.append("\t" + variable.identity() + " = " + variable.valueOf()));
        sb.append("\nUnassigned variables:\n");
        for (Variable<Type> unassignedVariable : unassignedVariables()) {
            StringBuilder sbValues = new StringBuilder();
            unassignedVariable.domain().forEach(value -> sbValues.append(value + " "));
            sb.append("\t" + unassignedVariable.identity() + " = { " + sbValues.toString() + " }");
        }
        return sb.toString();
    }

    public static <Type> Assignment<Type> initialAssignment(final Map<VariableIdentity, Variable<Type>> variableAssignments) {
        return new Assignment<>(variableAssignments);
    }
}