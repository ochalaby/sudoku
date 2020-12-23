package com.chalabysolutions.sudoku.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RowDto<list> {
    private int id;
    private List<FieldDto> fields;

    public RowDto (int id){
        this.id = id;
        this.fields = new ArrayList<>();
    }
}
