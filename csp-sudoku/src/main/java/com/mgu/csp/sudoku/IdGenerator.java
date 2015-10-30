package com.mgu.csp.sudoku;

import com.mgu.csp.VariableIdentity;

import static com.mgu.csp.VariableIdentity.id;

public class IdGenerator {

    public static VariableIdentity identityOfVariableAt(final int row, final int col) {
        return id(String.format("C%s%s", String.valueOf(row), String.valueOf(col)));
    }
}