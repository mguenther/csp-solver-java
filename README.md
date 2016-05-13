# Constraint Satisfaction Problem Solver

[![Build Status](https://travis-ci.org/mguenther/csp-solver-java.svg?branch=master)](https://travis-ci.org/mguenther/csp-solver-java.svg)

This repository contains a tiny framework for solving constraint satisfaction problems (CSP) that have both
discrete and finite domains. Although this framework is fully functional, it is not meant to be production-ready,
as it is primarily a showcase for myself on how to use the functional capabilities of Java 8 to a large extent.

The implementation is largely inspired from the splendid introductory text "Artificial Intelligence - A Modern Approach"
by Stuart Russell and Peter Norvig.

# Module Overview

* `csp-core`: Contains a functional approach on CSP-framework design for CSPs that are both discrete and finite.
* `csp-sudoku`: Contains an example application which expresses Sudoku as a CSP and solves it using `csp-core`.

# Design

The framework builds upon a set of a few abstractions. Class `CSP` is the abstract base class for all domain-specific
CSPs. It provides the means to construct the initial assignment, which is comprised of all the `Variable`s of the CSP
and their initial state. A variable can be either *assigned* or *unassigned*. In the first case, its domain has been
reduced to a single fixed value, while in the latter case it has no assigned value, but a - possibly reduced - set
of admissible domain values. Class `CSP` also provides the means to construct the full set of `Constraint`s for
the CSP. A `Variable` of the CSP takes part in one or multiple `Constraint`s. A `Constraint` involves some subset
of the variables of a CSP and specifies the allowable combinations of values for that subset. The `Constraint`
class provides the means to determine whether a constraint is *consistent* and *satisfied* given the set of
dependent variables.

The current state of a CSP is represented using an `Assignment`. An assignment always contains the full set of
variables of the CSP. An `Assignment` is *partial*, if it still contains `Variable`s that are unassigned. An assignment
that does not violate any constraints is called *consistent* or *legal*. A *complete* assignment is one in which
every variable is assigned. A solution to the CSP is a *complete* assignment which does not violate any constraints.
Class `Assignment` implements *forward checking*. This is a technique that eliminates the value assigned to a variable
from all other variables that participate in the same `Constraint`s, thus further decreasing the search space of CSP.

Class `DFSSolver` provides a generic way to operate on instances of `CSP` using depth-first search. It is able to apply 
heuristics for both variable ordering and value ordering that can dramatically decrease the search space. By default, it
uses an uninformed approach that simply selects the next unassigned variable and the preserves the original ordering of 
domain values for that variable. The `DFSSolver` progresses from `Assignment` to its successor until a complete 
assignment has been found or the search space is exhausted.

## Constraints

The framework currently only provides the `AllDiff` constraint. This constraint is satisfied if each of its variables is
assigned to a different value.

## Variable Ordering

The `DFSSolver` uses an uninformed approach by default which simply selects the next unassigned variable. However,
it is also possible to use the `MinimumRemainingValue` heuristic, which selects the variable that is most constrained 
given the current state of the CSP. Thus, the variable that has the fewest choices for domain values left will be
chosen. Using this heuristic can dramatically reduce the runtime of the solver, since the search space is pruned
efficiently.

## Value Ordering

The `DFSSolver` uses an uninformed approach by default which simply preserves the original ordering of domain values
for a given unassigned `Variable`. Currently, there is no informed implementation of `ValueOrdering`.

# Example Application: Sudoku as CSP

Module `csp-sudoku` formulates Sudoku as a constraint satisfaction problem. The current implementation is able to parse
a Sudoku puzzle as line-delimited string like the one shown underneath.
 
    003020600
    900305001
    001806400
    008102900
    700000008
    006708200
    002609500
    800203009
    005010300
    
Each cell of the Sudoku puzzle is represented as a variable of the CSP, where
`0` denotes an unassigned variable with domain values ranging from 1 to 9 and where every other number represents an
assigned variable. There are three kinds of constraints, which are all represented using `AllDiff` on their dependent
variables:

* Row constraints: The assigned values to every variable in a row of the puzzle must be all different.
* Column constraints: The assigned values to every variable in a column of the puzzle must be all different.
* Grid constraints: The assigned values to every variable within a grid must be all different.

In total there are 27 constraints and 81 variables.

# License

This software is released under the terms of the MIT license.