package com.chalabysolutions.sudoku.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.chalabysolutions.sudoku.SudokuSolver;
import com.chalabysolutions.sudoku.dto.FieldDto;
import com.chalabysolutions.sudoku.dto.RowDto;
import com.chalabysolutions.sudoku.dto.SudokuDto;
import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Square;
import com.chalabysolutions.sudoku.services.SquareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/")
public class SudokuController {

    @CrossOrigin
    @PostMapping(path = "solve")
    public ResponseEntity<?> solve(@RequestBody Object input) {
        log.info("SudokuController:  solve sudoku");
        log.info(input.toString());
        List<Object> inputRows = (ArrayList<Object>) input;
        Square startSudoku = toSquare(inputRows);

        //TODO: replace with actual solving
//        Field newField1 = new Field(1,1, 5);
//        startSudoku.setField(newField1);
//        Field newField2 = new Field(1,9, 5);
//        startSudoku.setField(newField2);

        Square solvedSudoku = SquareService.copySquare(startSudoku);

        SudokuSolver solver = new SudokuSolver(solvedSudoku);
        for (int i = 1; i <= 3; i++) {
            solver.solve();
        }

        List<RowDto> rows = toRows(solvedSudoku);
        return ResponseEntity.ok(rows);
    }

    private Square toSquare(List<Object> rows){
        Square square = SquareService.createSquare(rows.size());
        for (Object row : rows){
//            RowDto rowDto = row;
            LinkedHashMap rowMap = (LinkedHashMap) row;
            List<Object> fields = (ArrayList<Object>)rowMap.get("fields");
            for (Object field : fields){
                LinkedHashMap fieldMap = (LinkedHashMap) field;
                int fieldId = (Integer) fieldMap.get("id");
                String fieldValue = (String) fieldMap.get("value");

                if (!fieldValue.isEmpty()) {
                    Field newField = new Field((Integer) rowMap.get("id"), fieldId, Integer.valueOf(fieldValue));
                    square.setField(newField);
                }
            }
        }

        return square;
    }

    private List<RowDto> toRows(Square square){
        int size = square.getSize();
        List<RowDto> rows = new ArrayList<>();
        for (int x = 1; x <= size; x++){
            RowDto rowDto = new RowDto(x);
            for (int y = 1; y <= size; y++){
                Field field = square.getField(x, y);
                String value = field.getValues().size() > 1 ? "" : String.valueOf(field.getValues().get(0));
                FieldDto fieldDto = new FieldDto(y, value);
                rowDto.getFields().add(fieldDto);
            }
            rows.add(rowDto);
        }

        return rows;
    }
}
