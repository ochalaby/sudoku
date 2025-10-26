package com.chalabysolutions.sudoku.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowDto {
    private int id;
    private List<FieldDto> fields = new ArrayList<>();
}
