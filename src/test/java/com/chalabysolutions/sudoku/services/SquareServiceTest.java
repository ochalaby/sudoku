package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Sudoku;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquareServiceTest {

    @BeforeAll
    public static void setup() {
        //Generic setup
    }

    @Test
    public void testReadWriteFile() throws IOException {
        String sudokuFile = "testSudoku1.txt";
        String testDir = System.getProperty("user.dir") + "\\src\\test\\resources\\";
        String sudokuFilePath = testDir + sudokuFile;

        // Create file with empty squares
        Sudoku sudoku1 = new Sudoku(9);
        Sudoku sudoku2 = new Sudoku(9);
        SudokuService.sudokusToFile(sudoku1, sudoku2, sudokuFilePath);

        // Read and verify initial file
        Sudoku sudoku = SudokuService.fileToSudoku(sudokuFilePath);
        assertEquals(9, sudoku.getSize());
        assertTrue(sudoku.getField(5,1).getPossibleValues().contains(1));

        // Update the square
        sudoku1.getField(5,1).setFinalValue(6);

        // Write and read the updated file again
        SudokuService.sudokusToFile(sudoku1, sudoku2, sudokuFilePath);
        sudoku = SudokuService.fileToSudoku(sudokuFilePath);
        assertEquals(6, sudoku.getField(5,1).getFinalValue().intValue());
    }

    @Test
    public void testCopySudoku() {
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.getField(4,7).setFinalValue(8);

        Sudoku sudokuCopy = SudokuService.copySudoku(sudoku);
        sudokuCopy.getField(2,5).setFinalValue(4);

        assertEquals(1, sudoku.getField(4, 7).getPossibleValues().size());
        assertEquals(8, sudoku.getField(4, 7).getFinalValue().intValue());
        assertEquals(9, sudoku.getField(2, 5).getPossibleValues().size());

        assertEquals(1, sudokuCopy.getField(4, 7).getPossibleValues().size());
        assertEquals(8, sudokuCopy.getField(4, 7).getFinalValue().intValue());
        assertEquals(1, sudokuCopy.getField(2, 5).getPossibleValues().size());
        assertEquals(4, sudokuCopy.getField(2, 5).getFinalValue().intValue());
    }

    @Test
    public void testCountOpenFieldValues() {
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.getField(4,7).setFinalValue(8);
        sudoku.getField(2,5).setFinalValue(4);

        int totalFields = sudoku.getSize() * sudoku.getSize();
        assertEquals(totalFields - 2, SudokuService.countUnsolvedFields(sudoku));

        sudoku.getField(1,6).setFinalValue(3);
        assertEquals(totalFields - 3, SudokuService.countUnsolvedFields(sudoku));
    }

    @Test
    public void testSquareContent() {
        Sudoku sudoku = SudokuService.createSudoku(9);

        int squareSize = sudoku.getSize();
        assertNotNull(sudoku.getField(squareSize - 1, squareSize - 1));
    }

    @Test
    public void testSetValue() {
        Sudoku sudoku = SudokuService.createSudoku(9);

        int xPos = 2;
        int yPos = 4;
        sudoku.getField(xPos,yPos).setFinalValue(3);
        assertEquals(1, sudoku.getField(xPos, yPos).getPossibleValues().size());
        assertEquals(Integer.valueOf(3), sudoku.getField(xPos, yPos).getFinalValue().intValue());
    }

    @Test
    public void testUpdateValues() {
        Sudoku sudoku = SudokuService.createSudoku(9);

        int xPos = 2;
        int yPos = 4;

        Field field = sudoku.getField(xPos, yPos);
        field.removePossibleValue(1);
        field.removePossibleValue(2);
        field.removePossibleValue(5);
        field.removePossibleValue(6);
        field.removePossibleValue(7);

        assertEquals(4, sudoku.getField(xPos, yPos).getPossibleValues().size());
        assertTrue(sudoku.getField(xPos, yPos).getPossibleValues().contains(8));
    }

    @Test
    public void testColumnContent() {
        Sudoku sudoku = SudokuService.createSudoku(9);

        int columnIndex = 2;
        List<Field> column = sudoku.getColumns()[columnIndex].getFields();
        assertEquals(sudoku.getSize(), column.size());
        assertEquals(columnIndex, column.get(4).getColumnIndex());
    }

    @Test
    public void testRowContent() {
        Sudoku sudoku = SudokuService.createSudoku(9);

        int rowIndex = 2;
        List<Field> row = sudoku.getRows()[rowIndex].getFields();
        assertEquals(sudoku.getSize(), row.size());
        assertEquals(rowIndex, row.get(4).getRowIndex());
    }

    @Test
    public void testSubSquareContent() {
        Sudoku sudoku = SudokuService.createSudoku(9);

        int rowIndex = 4;
        int colIndex = 7;
        sudoku.getField(rowIndex, colIndex).setFinalValue(7);

        List<Field> subSquare = sudoku.getSubSquares()[sudoku.getField(4,7)
                .getSubSquare().getIndex()].getFields();

        List<Field> testFields = new ArrayList<>();
        for (Field field: subSquare){
            if (field.getPossibleValues().size() == 1){
                testFields.add(field);
            }
        }
        assertEquals(sudoku.getSize(), subSquare.size());
        assertEquals(1, testFields.size() );
        assertEquals(7, testFields.get(0).getFinalValue().intValue());
    }

    @Test
    public void testIsSelectionValid_square_1_1() {
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.getField(3,3).setFinalValue(6);
        sudoku.getField(3,4).setFinalValue(1);
        sudoku.getField(3,5).setFinalValue(4);
        sudoku.getField(4,3).setFinalValue(3);
        sudoku.getField(4,4).setFinalValue(6);
        sudoku.getField(4,5).setFinalValue(2);
        sudoku.getField(5,3).setFinalValue(8);
        sudoku.getField(5,4).setFinalValue(7);
        sudoku.getField(5,5).setFinalValue(5);

        Field field = sudoku.getField(4, 3);
        List<Field> subSquare = field.getSubSquare().getFields();
        assertFalse(SudokuService.isSelectionValid(subSquare));

    }

    @Test
    public void testIsSelectionValid_square_0_0() {
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.getField(0,0).setFinalValue(1);
        sudoku.getField(0,1).setFinalValue(9);
        sudoku.getField(0,2).setFinalValue(9);
        sudoku.getField(1,0).setFinalValue(8);
        sudoku.getField(1,1).setFinalValue(6);
        sudoku.getField(1,2).setFinalValue(7);
        sudoku.getField(2,0).setFinalValue(5);
        sudoku.getField(2,1).setFinalValue(5);
        sudoku.getField(2,2).setFinalValue(4);

        Field field = sudoku.getField(1, 2);
        List<Field> subSquare = sudoku.getSubSquares()[field.getSubSquare().getIndex()].getFields();
        assertFalse(SudokuService.isSelectionValid(subSquare));
    }

    @Test
    public void testIsSelectionValid_square_0_0_correct() {
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.getField(0,0).setFinalValue(1);
        sudoku.getField(0,1).setFinalValue(9);
        sudoku.getField(0,2).setFinalValue(2);
        sudoku.getField(1,0).setFinalValue(8);
        sudoku.getField(1,1).setFinalValue(6);
        sudoku.getField(1,2).setFinalValue(7);
        sudoku.getField(2,0).setFinalValue(3);
        sudoku.getField(2,1).setFinalValue(5);
        sudoku.getField(2,2).setFinalValue(4);

        Field field = sudoku.getField(1, 2);
        List<Field> subSquare = sudoku.getSubSquares()[field.getSubSquare().getIndex()].getFields();
        assertTrue(SudokuService.isSelectionValid(subSquare));
    }
}
