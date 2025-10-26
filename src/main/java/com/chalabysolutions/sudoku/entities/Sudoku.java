package com.chalabysolutions.sudoku.entities;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Sudoku {
    private final int size;
    private final int boxSize;

    private final List<Field> fields = new ArrayList<>();
    private final Row[] rows;
    private final Column[] columns;
    private final SubSquare[] subSquares;

    public Sudoku(int size) {
        this.size = size;
        this.boxSize = (int) Math.sqrt(size);

        if (boxSize * boxSize != size) {
            throw new IllegalArgumentException("Size must be a perfect square (4, 9, 16, ...)");
        }

        rows = new Row[size];
        columns = new Column[size];
        subSquares = new SubSquare[size];

        // Initialiseer de rijen, kolommen en subsquares
        for (int i = 0; i < size; i++) {
            rows[i] = new Row(i);
            columns[i] = new Column(i);
            subSquares[i] = new SubSquare(i);
        }

        // Bouw velden op
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int subIndex = (r / boxSize) * boxSize + (c / boxSize);
                Field field = new Field(r, c, rows[r], columns[c], subSquares[subIndex]);
                field.initializePossibleValues(size);

                rows[r].addField(field);
                columns[c].addField(field);
                subSquares[subIndex].addField(field);
                fields.add(field);
            }
        }
    }

    public Sudoku deepCopy() {
        Sudoku copy = new Sudoku(this.size);
        copy.copyFieldsFrom(this);
        return copy;
    }

    public void restoreFrom(Sudoku other) {
        if (this.size != other.size) {
            throw new IllegalArgumentException("Cannot restore from Sudoku of different size");
        }

        this.copyFieldsFrom(other);
    }

    private void copyFieldsFrom(Sudoku source) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Field target = this.getField(r, c);
                Field original = source.getField(r, c);

                target.setFinalValue(original.getFinalValue());
                target.getPossibleValues().clear();
                target.getPossibleValues().addAll(original.getPossibleValues());
            }
        }
    }

    public Field getField(int row, int col) {
        return fields.stream()
                .filter(f -> f.getRowIndex() == row && f.getColumnIndex() == col)
                .findFirst()
                .orElseThrow();
    }

    public void setField(Field updatedField) {
        Field existing = getField(updatedField.getRowIndex(), updatedField.getColumnIndex());

        // Update finalValue
        existing.setFinalValue(updatedField.getFinalValue());

        // Update mogelijke waarden
        existing.getPossibleValues().clear();
        existing.getPossibleValues().addAll(updatedField.getPossibleValues());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("  ");
        int size = this.size;
        for (int colIndex = 0; colIndex < size; colIndex++){
            result.append(" ").append(colIndex).append(" ");
        }
        result.append("\n");
        for (int rowIndex = 0; rowIndex < size; rowIndex++){
            result.append(rowIndex).append(" ");
            for (int colIndex = 0; colIndex < size; colIndex++){
                Field field = this.getField(rowIndex, colIndex);
                if (field.getFinalValue()!= null){
                    result.append("[").append(field.getFinalValue()).append("]");
                } else {
                    result.append("[ ]");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }
}
