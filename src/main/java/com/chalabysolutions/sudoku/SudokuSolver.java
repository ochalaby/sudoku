package com.chalabysolutions.sudoku;

import com.chalabysolutions.sudoku.entities.*;
import com.chalabysolutions.sudoku.services.CollectionService;
import com.chalabysolutions.sudoku.services.SudokuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SudokuSolver die eerst logische eliminatie gebruikt (CollectionService)
 * en vervolgens backtracking toepast wanneer logisch redeneren niet meer genoeg is.
 */
public class SudokuSolver {

    private static final Logger logger = LoggerFactory.getLogger(SudokuSolver.class);

    public SudokuSolver() {}

    /**
     * Lost een sudoku op. Combineert logische eliminatie en backtracking.
     */
    public Sudoku solve(Sudoku sudoku) {
        logger.info("Starting Sudoku solver...");
        Sudoku workingCopy = SudokuService.copySudoku(sudoku);

        if (solveRecursive(workingCopy)) {
            logger.info("Sudoku solved successfully!");
            return workingCopy;
        } else {
            logger.warn("No valid solution found!");
            return sudoku;
        }
    }

    /**
     * Recursieve backtracking-oplossing met voorafgaande logische reductie.
     */
    private boolean solveRecursive(Sudoku sudoku) {
        // Eerst: probeer logische reductie
        applyLogic(sudoku);

        // Stopcondities
        if (SudokuService.isSolved(sudoku) && SudokuService.isValid(sudoku)) {
            return true;
        }

        // Zoek het veld met de minste mogelijke waarden (MRV-heuristiek)
        Field nextField = findFieldWithFewestPossibilities(sudoku);
        if (nextField == null) {
            // Geen velden meer met >1 waarde, maar nog niet opgelost → ongeldige staat
            return false;
        }

        // Probeer elk mogelijke waarde in dit veld
        for (int val : nextField.getPossibleValues()) {
            Sudoku copy = SudokuService.copySudoku(sudoku);
            copy.getField(nextField.getRowIndex(), nextField.getColumnIndex()).setFinalValue(val);

            if (solveRecursive(copy)) {
                // kopie terugschrijven
                copyBack(sudoku, copy);
                return true;
            }
        }

        // Geen van de waarden werkte → backtrack
        return false;
    }

    /**
     * Past alle logische regels toe tot er geen vooruitgang meer wordt geboekt.
     */
    private void applyLogic(Sudoku sudoku) {
        int previousUnsolved = Integer.MAX_VALUE;
        int currentUnsolved = SudokuService.countUnsolvedFields(sudoku);

        while (currentUnsolved < previousUnsolved) {
            previousUnsolved = currentUnsolved;

            // Remove fixed values from other fields per selection
            for (int i = 0; i < sudoku.getSize(); i++) {
                CollectionService.removeFixedValues(sudoku.getRows()[i].getFields());
                CollectionService.removeFixedValues(sudoku.getColumns()[i].getFields());
                CollectionService.removeFixedValues(sudoku.getSubSquares()[i].getFields());
            }

            // Set single value in field when it is unique per selection
            for (int i = 0; i < sudoku.getSize(); i++) {
                CollectionService.selectUniqueValues(sudoku.getRows()[i].getFields());
                CollectionService.selectUniqueValues(sudoku.getColumns()[i].getFields());
                CollectionService.selectUniqueValues(sudoku.getSubSquares()[i].getFields());
            }

            // When there are pairs, remove those values from other fields per selection
            for (int i = 0; i < sudoku.getSize(); i++) {
                CollectionService.removeDoubleValues(sudoku.getRows()[i].getFields());
                CollectionService.removeDoubleValues(sudoku.getColumns()[i].getFields());
                CollectionService.removeDoubleValues(sudoku.getSubSquares()[i].getFields());
            }

            currentUnsolved = SudokuService.countUnsolvedFields(sudoku);
        }
    }

    /**
     * Vind het veld met de minste mogelijke waarden (>1).
     */
    private Field findFieldWithFewestPossibilities(Sudoku sudoku) {
        Field best = null;
        int fewest = Integer.MAX_VALUE;

        for (Field field : sudoku.getFields()) {
            int size = field.getPossibleValues().size();
            if (size > 1 && size < fewest) {
                fewest = size;
                best = field;
            }
        }

        return best;
    }

    /**
     * Kopieer de waarden van een opgeloste kopie terug naar het originele Square.
     */
    private void copyBack(Sudoku target, Sudoku source) {
        target.restoreFrom(source);
    }
}
