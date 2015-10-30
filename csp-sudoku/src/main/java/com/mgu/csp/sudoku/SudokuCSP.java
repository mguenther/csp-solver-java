package com.mgu.csp.sudoku;

import com.mgu.csp.AllDiff;
import com.mgu.csp.Assignment;
import com.mgu.csp.CSP;
import com.mgu.csp.Constraint;
import com.mgu.csp.Variable;
import com.mgu.csp.VariableIdentity;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mgu.csp.Variable.unassignedVariable;
import static com.mgu.csp.sudoku.IdGenerator.identityOfVariableAt;

public class SudokuCSP extends CSP<Integer> {

    private static final int BOARD_SIZE = 9;

    private final String sudokuPuzzle;

    public SudokuCSP(final String sudokuPuzzle) {
        this.sudokuPuzzle = sudokuPuzzle;
    }

    @Override
    public Assignment<Integer> initialAssignment() {
        final Map<VariableIdentity, Variable<Integer>> initial = new HashMap<>();
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int colIndex = 0; colIndex < 9; colIndex++) {
                final VariableIdentity identity = identityOfVariableAt(rowIndex, colIndex);
                initial.put(identity, unassignedVariable(identity, initialDomain()));
            }
        }
        final Set<Constraint> constraints = constraints();
        Assignment<Integer> assignment = Assignment.initialAssignment(initial);
        final String[] rows = sudokuPuzzle.split("\n");
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            final String[] cols = rows[rowIndex].split("");
            for (int colIndex = 0; colIndex < cols.length; colIndex++) {
                final int cellValue = Integer.parseInt(cols[colIndex]);
                if (cellValue != 0) {
                    final VariableIdentity identity = identityOfVariableAt(rowIndex, colIndex);
                    final Variable<Integer> unassignedVariable = initial.get(identity);
                    assignment = assignment.assign(unassignedVariable, cellValue, constraints);
                }
            }
        }
        return assignment;
    }

    @Override
    public Set<Constraint> constraints() {
        final Set<Constraint> constraints = new HashSet<>();
        constraints.addAll(constraintsOnRows());
        constraints.addAll(constraintsOnColumns());
        constraints.addAll(constraintsOnGrids());
        return Collections.unmodifiableSet(constraints);
    }

    private Set<Constraint> constraintsOnRows() {
        final Set<Constraint> rowConstraints = new HashSet<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            final Set<VariableIdentity> rowConstraintReliesOn = new HashSet<>();
            for (int col = 0; col < BOARD_SIZE; col++) {
                rowConstraintReliesOn.add(identityOfVariableAt(row, col));
            }
            rowConstraints.add(new AllDiff(rowConstraintReliesOn));
        }
        return rowConstraints;
    }

    private Set<Constraint> constraintsOnColumns() {
        final Set<Constraint> colConstraints = new HashSet<>();
        for (int col = 0; col < BOARD_SIZE; col++) {
            final Set<VariableIdentity> colConstraintReliesOn = new HashSet<>();
            for (int row = 0; row < BOARD_SIZE; row++) {
                colConstraintReliesOn.add(identityOfVariableAt(row, col));
            }
            colConstraints.add(new AllDiff(colConstraintReliesOn));
        }
        return colConstraints;
    }

    private Set<Constraint> constraintsOnGrids() {
        final Set<Constraint> gridConstraints = new HashSet<>();
        for (int gridRow = 0; gridRow < 3; gridRow++) {
            for (int gridCol = 0; gridCol < 3; gridCol++) {
                final Set<VariableIdentity> gridConstraintReliesOn = new HashSet<>();
                for (int rowIndex = gridRow * 3; rowIndex < gridRow * 3 + 3; rowIndex++) {
                    for (int colIndex = gridCol * 3; colIndex < gridCol * 3 + 3; colIndex++) {
                        gridConstraintReliesOn.add(identityOfVariableAt(rowIndex, colIndex));
                    }
                }
                gridConstraints.add(new AllDiff(gridConstraintReliesOn));
            }
        }
        return gridConstraints;
    }

    private Set<Integer> initialDomain() {
        return new HashSet<Integer>() {{ add(1); add(2); add(3); add(4); add(5); add(6); add(7); add(8); add(9); }};
    }
}