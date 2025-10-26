package com.chalabysolutions.sudoku.entities;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Field {
    private final int rowIndex;
    private final int columnIndex;

    private final Row row;
    private final Column column;
    private final SubSquare subSquare;

    private Integer finalValue = null;
    private final Set<Integer> possibleValues = new HashSet<>();

    public Field(int rowIndex, int columnIndex, Row row, Column column, SubSquare subSquare) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.row = row;
        this.column = column;
        this.subSquare = subSquare;
    }

    public Field (int rowIndex, int columnIndex, List<Integer> possibleValues) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.row = null;
        this.column = null;
        this.subSquare = null;
        this.possibleValues.addAll(possibleValues);
    }

    // Wordt aangeroepen door Sudoku tijdens initialisatie
    public void initializePossibleValues(int size) {
        possibleValues.clear();
        for (int i = 1; i <= size; i++) {
            possibleValues.add(i);
        }
    }

    public boolean isSolved() {
        return finalValue != null;
    }

    public void setFinalValue(Integer value) {
        this.finalValue = value;
        possibleValues.clear();
        if (value != null) {
            possibleValues.add(value);
        }
    }

    /** Verwijder een mogelijke waarde */
    public void removePossibleValue(int value) {
        if (!isSolved()) {
            possibleValues.remove(value);
            if (possibleValues.size() == 1){
                setFinalValue(possibleValues.iterator().next());
            }
        }
    }

    @Override
    public String toString(){
        return "Field: ["+ this.rowIndex +","+ this.columnIndex +"] values: " + this.possibleValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return rowIndex == field.rowIndex &&
                columnIndex == field.columnIndex;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(rowIndex, columnIndex);
    }
}
