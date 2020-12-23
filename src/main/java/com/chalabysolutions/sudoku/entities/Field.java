package com.chalabysolutions.sudoku.entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Field {
    private int rowIndex;
    private int columnIndex;

    private List<Integer> values;

    public Field(int rowIndex, int columnIndex, int value){
        this(rowIndex, columnIndex,Collections.singletonList(value));
    }

    public Field(int rowIndex, int columnIndex, List<Integer> values){
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.values = values;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void removeValue(int value){
        if (values.size() > 1){
            values.remove(Integer.valueOf(value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return getRowIndex() == field.getRowIndex() &&
                getColumnIndex() == field.getColumnIndex() &&
                getValues().containsAll(field.getValues()) &&
                field.getValues().containsAll(getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rowIndex, this.columnIndex, this.values);
    }

    @Override
    public String toString(){
        return "Field: ["+ this.rowIndex +","+ this.columnIndex +"] values: " + this.values;
    }
}
