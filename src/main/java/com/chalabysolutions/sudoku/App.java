package com.chalabysolutions.sudoku;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Square;
import com.chalabysolutions.sudoku.services.SquareService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args )
    {
        String sudokuFile = "sudoku4.txt";
        String inputDir = System.getProperty("user.dir") + "\\input\\";
        String sudokuFilePath = inputDir + sudokuFile;
        logger.info("input file = " + sudokuFilePath);

        try {
            Square startSudoku = SquareService.fileToSquare(sudokuFilePath);
            Square solvedSudoku = SquareService.copySquare(startSudoku);

            SudokuSolver solver = new SudokuSolver(solvedSudoku);
            for (int i = 1; i <= 3; i++) {
                solver.solve();
            }

            logger.info("Resulting sudoku square:\n" + SquareService.squareToString(solvedSudoku));

            for (Field field : solvedSudoku.getFields().values()){
                if (field.getValues().size() > 1){
                    logger.info(field);
                }
            }

            SquareService.squaresToFile(startSudoku, solvedSudoku, sudokuFilePath);
        } catch (IOException ioe) {
            logger.error("IOException: %s%n", ioe);
        }
    }


}