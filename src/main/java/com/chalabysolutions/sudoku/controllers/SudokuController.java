package com.chalabysolutions.sudoku.controllers;

import java.util.List;

import com.chalabysolutions.sudoku.SudokuSolver;
import com.chalabysolutions.sudoku.dto.RowDto;
import com.chalabysolutions.sudoku.dto.SudokuDto;
import com.chalabysolutions.sudoku.entities.Sudoku;
import com.chalabysolutions.sudoku.services.SudokuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class SudokuController {

    @CrossOrigin
    @PostMapping(path = "/solve")
    public ResponseEntity<SudokuDto> solve(@RequestBody SudokuDto sudokuDto) {
        log.info("SudokuController:  solve sudoku");

        List<RowDto> inputRows = sudokuDto.getRows();
        Sudoku startSudoku = SudokuService.rowsToSudoku(inputRows);
        log.info("Start sudoku: \n {} ", startSudoku);

        SudokuSolver solver = new SudokuSolver();
        Sudoku solvedSudoku = solver.solve(SudokuService.copySudoku(startSudoku));

        log.info("Solved sudoku: \n {} ", solvedSudoku);

        List<RowDto> rows = SudokuService.sudokuToRows(solvedSudoku);
        SudokuDto result = new SudokuDto();
        result.setRows(rows);

        return ResponseEntity.ok(result);
    }
}
