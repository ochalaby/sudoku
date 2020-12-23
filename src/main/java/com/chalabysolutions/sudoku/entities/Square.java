package com.chalabysolutions.sudoku.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Square {

    private Map<String, Field> fields;

    public Square(int size){
        fields = new HashMap<>();

        for (int rowIndex = 1; rowIndex <= size; rowIndex ++) {
            for (int colIndex = 1; colIndex <= size; colIndex++) {
                List<Integer> values = new ArrayList<>();
                for (int i = 1; i <= size; i++){
                    values.add(i);
                }
                setField(new Field(rowIndex, colIndex, values));
            }
        }
    }

    public int getSize() {
        return (int) Math.sqrt(fields.size());
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public Field getField(int rowIndex, int colIndex){
        return fields.get(createKey(rowIndex, colIndex));
    }

    public void setField(Field field) {
        fields.put(createKey(field.getRowIndex(),field.getColumnIndex()), field);
    }

    public List<Field> getRow(int rowIndex) {
        List<Field> fields = new ArrayList<>();
        for (Field field : this.getFields().values()) {
            if (field.getRowIndex() == rowIndex) {
                fields.add(field);
            }
        }
        return fields;
    }

    public List<Field> getColumn(int columnIndex){
        List<Field> fields = new ArrayList<>();
        for (Field field : this.getFields().values()){
            if (field.getColumnIndex() == columnIndex){
                fields.add(field);
            }
        }
        return fields;
    }

    public List<Field> getSubSquare(Field matchField) {
        return this.getSubSquare(getSubSquareIndex(matchField.getRowIndex(), this.getSize()),
                getSubSquareIndex(matchField.getColumnIndex(), this.getSize()));
    }

    public List<Field> getSubSquare(int sqRowIndex, int sqColumnIndex) {
        List<Field> fields = new ArrayList<>();
        for (Field field : this.getFields().values()) {
            if (getSubSquareIndex(field.getRowIndex(), this.getSize()) == sqRowIndex &&
                    getSubSquareIndex(field.getColumnIndex(), this.getSize()) == sqColumnIndex) {
                fields.add(field);
            }
        }
        return fields;
    }

    private int getSubSquareIndex(int index, int squareSize){
        return (int) (Math.floor((index - 1) / Math.sqrt(squareSize)) + 1);
    }

    private String createKey(int rowIndex, int colIndex){
        return rowIndex + "-" + colIndex;
    }
}
