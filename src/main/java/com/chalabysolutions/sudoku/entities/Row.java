package com.chalabysolutions.sudoku.entities;

import lombok.Getter;

@Getter
public class Row extends FieldGroup {
    private final int index;
    public Row(int index) { this.index = index; }
}
