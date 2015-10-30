package com.mgu.csp.sudoku;

import com.mgu.csp.Assignment;
import com.mgu.csp.DFSSolver;
import com.mgu.csp.MinimumRemainingValue;

import java.util.Optional;

public class SudokuApp {

    public static final String TEST =
            "003020600\n" +
                    "900305001\n" +
                    "001806400\n" +
                    "008102900\n" +
                    "700000008\n" +
                    "006708200\n" +
                    "002609500\n" +
                    "800203009\n" +
                    "005010300";

    public static void main(String[] args) {
        long start = System.nanoTime();
        DFSSolver<Integer> solver = new DFSSolver<>(new MinimumRemainingValue<>());
        solver.addListener(state -> PrettyPrinter.printBoard(state));
        Optional<Assignment<Integer>> completeAssignment = solver.solve(new SudokuCSP(TEST));
        long end = System.nanoTime();
        long duration = (end -start) / 1000000;

        completeAssignment.orElseThrow(() -> new RuntimeException("Found no solution."));
        completeAssignment.ifPresent(assignment -> {
            System.out.println("Took " + duration + " ms.");
            System.out.println();
            PrettyPrinter.printBoard(assignment);
        });
    }
}