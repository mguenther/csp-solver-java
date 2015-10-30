package com.mgu.csp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mgu.csp.VariableIdentity.id;

/**
 * This example is taken from the book 'Artificial Intelligence - A Modern Approach' by Stuart Russell
 * and Peter Norvig. It is used as an introductory example on how a k-colorability-problem can be stated
 * as a constraint satisfaction problem. With only seven nodes and nine edges, this example is limited in
 * its size and thus suitable for testing purposes.
 *
 * Note: The binary constraints between vertices of a k-colorability problem are represented using
 * the AllDiff constraint.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class ColoringCSP extends CSP<String> {

    @Override
    protected Assignment<String> initialAssignment() {
        final Map<VariableIdentity, Variable<String>> variableAssignments =
                variables()
                .stream()
                .collect(Collectors.toMap(Variable::identity, Function.identity()));
        return Assignment.initialAssignment(variableAssignments);
    }

    @Override
    protected Set<Constraint> constraints() {
        final Set<Constraint> constraints = new HashSet<>();
        constraints.add(AllDiff.on(id("WA"), id("NT")));
        constraints.add(AllDiff.on(id("WA"), id("SA")));
        constraints.add(AllDiff.on(id("NT"), id("QL")));
        constraints.add(AllDiff.on(id("NT"), id("SA")));
        constraints.add(AllDiff.on(id("SA"), id("QL")));
        constraints.add(AllDiff.on(id("QL"), id("NSW")));
        constraints.add(AllDiff.on(id("SA"), id("NSW")));
        constraints.add(AllDiff.on(id("SA"), id("VI")));
        constraints.add(AllDiff.on(id("NSW"), id("VI")));
        return Collections.unmodifiableSet(constraints);
    }

    private Set<Variable<String>> variables() {
        final Set<Variable<String>> variables = new HashSet<>();
        variables.add(Variable.unassignedVariable(id("WA"), initialDomain()));
        variables.add(Variable.unassignedVariable(id("NT"), initialDomain()));
        variables.add(Variable.unassignedVariable(id("SA"), initialDomain()));
        variables.add(Variable.unassignedVariable(id("QL"), initialDomain()));
        variables.add(Variable.unassignedVariable(id("NSW"), initialDomain()));
        variables.add(Variable.unassignedVariable(id("VI"), initialDomain()));
        variables.add(Variable.unassignedVariable(id("TS"), initialDomain()));
        return variables;
    }

    private Set<String> initialDomain() {
        return new HashSet<String>() {{ add("red"); add("green"); add("blue");}};
    }
}