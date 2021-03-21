package com.technek.parrotnight.models;

public class PairValues<T> {

    T value1;
    T value2;

    public PairValues(final T val1, final T val2) {
        this.value1 = val1;
        this.value2 = val2;
    }

    public void setValue1(final T val) { value1 = val;}
    public void setValue2(final T val) { value2 = val;}
    public void setValues(final T val1, final T val2) { value1 = val1; value2 = val2; }

    public T getValue1() { return  value1; }
    public T getValue2() { return value2; }

    public String toString() {
        return "PairValues { value1: " + value1 + ", value2: " + value2 + " }";
    }
}
