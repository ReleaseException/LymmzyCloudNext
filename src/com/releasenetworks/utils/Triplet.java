package com.releasenetworks.utils;

public class Triplet<F, S, T> {
    private F first;
    private S second;
    private T third;

    public Triplet() {
        this.first = null;
        this.second = null;
        this.third = null;
    }

    public Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public Triplet<F, S, T> setFirst(F first) {
        this.first = first;
        return this;
    }

    public S getSecond() {
        return this.second;
    }

    public Triplet<F, S, T> setSecond(S second) {
        this.second = second;
        return this;
    }

    public T getThird() {
        return this.third;
    }

    public Triplet<F, S, T> setThird(T third) {
        this.third = third;
        return this;
    }

    public boolean equals(Triplet<F, S, T> triplet) {
        return this.first.toString().equalsIgnoreCase(triplet.getFirst().toString()) && this.second.toString().equalsIgnoreCase(triplet.getSecond().toString());
    }

}
