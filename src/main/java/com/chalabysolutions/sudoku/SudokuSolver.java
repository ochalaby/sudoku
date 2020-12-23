package com.chalabysolutions.sudoku;

import com.chalabysolutions.sudoku.entities.Square;
import com.chalabysolutions.sudoku.services.CollectionService;
import com.chalabysolutions.sudoku.services.SquareService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SudokuSolver {

    private static final Logger logger = LogManager.getLogger(SudokuSolver.class);

    private Square sudokuSquare;

    public SudokuSolver(Square sudokuSquare){
        this.sudokuSquare = sudokuSquare;
    }

    public void solve() {

        int count = 1;
        int newCount = 0;
        while (newCount < count) {
            count = SquareService.countOpenFieldValues(sudokuSquare);
            logger.info(count + " open values to solve to " + sudokuSquare.getSize() * sudokuSquare.getSize());

            // Loop all rows
            for (int i = 1; i <= sudokuSquare.getSize(); i++){
                CollectionService.removeFixedValues(sudokuSquare, sudokuSquare.getRow(i));
                CollectionService.selectUniqueValues(sudokuSquare, sudokuSquare.getRow(i));
                CollectionService.removeDoubleValues(sudokuSquare, sudokuSquare.getRow(i));
                CollectionService.removeValuesFromOtherFieldsInSubSquares(sudokuSquare, sudokuSquare.getRow(i));
            }

            // Loop all columns
            for (int i = 1; i <= sudokuSquare.getSize(); i++){
                CollectionService.removeFixedValues(sudokuSquare, sudokuSquare.getColumn(i));
                CollectionService.selectUniqueValues(sudokuSquare, sudokuSquare.getColumn(i));
                CollectionService.removeDoubleValues(sudokuSquare, sudokuSquare.getColumn(i));
                CollectionService.removeValuesFromOtherFieldsInSubSquares(sudokuSquare, sudokuSquare.getColumn(i));
            }

            // Loop all subSquares
            int subSize = (int) Math.sqrt(sudokuSquare.getSize());
            for (int i = 1; i <= subSize; i++){
                for (int j = 1; j <= subSize; j++) {
                    CollectionService.removeFixedValues(sudokuSquare, sudokuSquare.getSubSquare(i, j));
                    CollectionService.selectUniqueValues(sudokuSquare, sudokuSquare.getSubSquare(i, j));
                    CollectionService.removeDoubleValues(sudokuSquare, sudokuSquare.getSubSquare(i, j));
                    CollectionService.removeValuesFromOtherSubSquares(sudokuSquare, sudokuSquare.getSubSquare(i, j));
                }
            }

            newCount = SquareService.countOpenFieldValues(sudokuSquare);
        }

    }

}
