package com.chalabysolutions.sudoku.entities;

import lombok.Getter;

@Getter
public class SubSquare extends FieldGroup {
    private final int index;
    public SubSquare(int index) { this.index = index; }
}
