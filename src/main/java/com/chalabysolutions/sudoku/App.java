package com.chalabysolutions.sudoku;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Sudoku;
import com.chalabysolutions.sudoku.services.SudokuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        logger.info("Application started");

        String sudokuFile = "sudokuFailed6.txt";
        String inputDir = System.getProperty("user.dir") + "\\input\\";
        String sudokuFilePath = inputDir + sudokuFile;
        logger.info("input file = {}", sudokuFilePath);

        try {
            Sudoku startSudoku = SudokuService.fileToSudoku(sudokuFilePath);
            logger.info("Start sudoku:\n{}", startSudoku);

            SudokuSolver solver = new SudokuSolver();
            Sudoku solvedSudoku = solver.solve(SudokuService.copySudoku(startSudoku));

            logger.info("Resulting sudoku square:\n{}", solvedSudoku);

            for (int i = 0; i < solvedSudoku.getSize(); i++) {
                for (int j = 0; j < solvedSudoku.getSize(); j++) {
                    Field field = solvedSudoku.getField(i, j);
                    if (field.getPossibleValues().size() > 1){
                        logger.info(String.valueOf(field));
                    }
                }
            }

            SudokuService.sudokusToFile(startSudoku, solvedSudoku, sudokuFilePath);
        } catch (IOException ioe) {
            logger.error("IOException: %s%n", ioe);
        }
    }


}