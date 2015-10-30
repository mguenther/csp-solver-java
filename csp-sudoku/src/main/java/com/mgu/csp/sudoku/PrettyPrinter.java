package com.mgu.csp.sudoku;

import com.mgu.csp.Assignment;
import com.mgu.csp.VariableIdentity;

public class PrettyPrinter {

    public static void printBoard(final Assignment<Integer> assignment) {
        final StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final VariableIdentity id = IdGenerator.identityOfVariableAt(row, col);
                final String assignedValue = assignment.valueOf(id) == null ? "." : String.valueOf(assignment.valueOf(id));
                sb.append(assignedValue);
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
}