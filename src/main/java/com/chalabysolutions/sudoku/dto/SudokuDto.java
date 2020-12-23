package com.chalabysolutions.sudoku.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SudokuDto {
    private List<RowDto> rows;
}
