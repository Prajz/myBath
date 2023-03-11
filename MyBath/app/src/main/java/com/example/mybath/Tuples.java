package com.example.mybath;
import java.util.List;

public class Tuples {
    private String name;
    private List<String> values;

    public Tuples(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }
}