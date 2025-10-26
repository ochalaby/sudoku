package com.chalabysolutions.sudoku.entities;

import lombok.Getter;

@Getter
public class Column extends FieldGroup {
    private final int index;
    public Column(int index) { this.index = index; }
}
