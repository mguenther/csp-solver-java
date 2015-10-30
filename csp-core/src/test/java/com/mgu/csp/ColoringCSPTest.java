package com.mgu.csp;

import org.junit.Test;

import java.util.Optional;

import static com.mgu.csp.VariableIdentity.id;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ColoringCSPTest {

    @Test
    public void solvingColoringCSPShouldYieldCompletedAssignmentWithMutuallyExclusiveVariableAssignments() {
        final DFSSolver<String> solver = new DFSSolver<>();
        final Optional<Assignment<String>> optionalCompletedAssignment = solver.solve(new ColoringCSP());

        assertTrue(optionalCompletedAssignment.isPresent());

        final Assignment<String> completedAssignment = optionalCompletedAssignment.get();

        assertTrue(completedAssignment.isComplete());

        assertNotEquals(
                completedAssignment.valueOf(id("WA")),
                completedAssignment.valueOf(id("NT")));
        assertNotEquals(
                completedAssignment.valueOf(id("WA")),
                completedAssignment.valueOf(id("SA")));
        assertNotEquals(
                completedAssignment.valueOf(id("NT")),
                completedAssignment.valueOf(id("QL")));
        assertNotEquals(
                completedAssignment.valueOf(id("NT")),
                completedAssignment.valueOf(id("SA")));
        assertNotEquals(
                completedAssignment.valueOf(id("SA")),
                completedAssignment.valueOf(id("QL")));
        assertNotEquals(
                completedAssignment.valueOf(id("QL")),
                completedAssignment.valueOf(id("NSW")));
        assertNotEquals(
                completedAssignment.valueOf(id("SA")),
                completedAssignment.valueOf(id("NSW")));
        assertNotEquals(
                completedAssignment.valueOf(id("SA")),
                completedAssignment.valueOf(id("VI")));
        assertNotEquals(
                completedAssignment.valueOf(id("NSW")),
                completedAssignment.valueOf(id("VI")));
    }
}