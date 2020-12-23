package com.chalabysolutions.sudoku.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldDto {
    private int id;
    private String value;

    public FieldDto (int id, String value){
        this.id = id;
        this.value = value;
    }
}
