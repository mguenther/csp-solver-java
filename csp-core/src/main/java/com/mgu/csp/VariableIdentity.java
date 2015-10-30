package com.mgu.csp;

/**
 * Typed identity for {@code Variable}s which uniquely identifies a {@code Variable} of a CSP.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class VariableIdentity {

    private final String identity;

    private VariableIdentity(final String identity) {
        this.identity = identity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableIdentity that = (VariableIdentity) o;

        return !(identity != null ? !identity.equals(that.identity) : that.identity != null);
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return identity;
    }

    public static VariableIdentity id(final String identity) {
        return new VariableIdentity(identity);
    }
}