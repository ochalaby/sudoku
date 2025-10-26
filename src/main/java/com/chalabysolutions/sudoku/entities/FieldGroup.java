package com.chalabysolutions.sudoku.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class FieldGroup {
    protected final List<Field> fields = new ArrayList<>();

    public void addField(Field field) {
        fields.add(field);
    }

    public boolean containsValue(int value) {
        return fields.stream()
                .anyMatch(f -> f.getFinalValue() != null && f.getFinalValue() == value);
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
