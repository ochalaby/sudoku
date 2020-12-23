package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Square;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SquareServiceTest {

    @BeforeClass
    public static void setup() {
        //Generic setup
    }

    @Test
    public void testReadWriteFile() throws IOException {
        String sudokuFile = "testSudoku1.txt";
        String testDir = System.getProperty("user.dir") + "\\src\\test\\resources\\";
        String sudokuFilePath = testDir + sudokuFile;

        // Create file with empty squares
        Square square1 = new Square(9);
        Square square2 = new Square(9);
        SquareService.squaresToFile(square1, square2, sudokuFilePath);

        // Read and verify initial file
        Square square = SquareService.fileToSquare(sudokuFilePath);
        assertEquals(9, square.getSize());
        assertEquals(1, square.getField(5,1).getValues().get(0).intValue());

        // Update the square
        square1.setField(new Field(5,1,6));

        // Write and read the updated file again
        SquareService.squaresToFile(square1, square2, sudokuFilePath);
        square = SquareService.fileToSquare(sudokuFilePath);
        assertEquals(6, square.getField(5,1).getValues().get(0).intValue());
    }

    @Test
    public void testCopySquare() {
        Square square = SquareService.createSquare(9);
        square.setField(new Field(4,7,8));

        Square squareCopy = SquareService.copySquare(square);
        squareCopy.setField(new Field(2,5,4));

        assertEquals(1, square.getField(4, 7).getValues().size());
        assertEquals(8, square.getField(4, 7).getValues().get(0).intValue());
        assertEquals(9, square.getField(2, 5).getValues().size());

        assertEquals(1, squareCopy.getField(4, 7).getValues().size());
        assertEquals(8, squareCopy.getField(4, 7).getValues().get(0).intValue());
        assertEquals(1, squareCopy.getField(2, 5).getValues().size());
        assertEquals(4, squareCopy.getField(2, 5).getValues().get(0).intValue());
    }

    @Test
    public void testCountOpenFieldValues() {
        Square square = SquareService.createSquare(9);
        square.setField(new Field(4,7,8));
        square.setField(new Field(2,5,4));

        assertEquals(713, SquareService.countOpenFieldValues(square));

        square.setField(new Field(1,6,3));
        assertEquals(705, SquareService.countOpenFieldValues(square));
    }

    @Test
    public void testSquareContent() {
        Square square = SquareService.createSquare(9);

        int squareSize = square.getSize();
        assertNotNull(square.getField(squareSize, squareSize));
    }

    @Test
    public void testSetValue() {
        Square square = SquareService.createSquare(9);

        int xPos = 2;
        int yPos = 4;
        square.setField(new Field(xPos,yPos,3));
        assertEquals(1, square.getField(xPos, yPos).getValues().size());
        assertEquals(Integer.valueOf(3), square.getField(xPos, yPos).getValues().get(0));
    }

    @Test
    public void testUpdateValues() {
        Square square = SquareService.createSquare(9);

        int xPos = 2;
        int yPos = 4;
        List<Integer> values = new ArrayList<>();
        values.add(3);
        values.add(4);
        values.add(8);
        values.add(9);

        square.setField(new Field(xPos, yPos, values));

        assertEquals(4, square.getField(xPos, yPos).getValues().size());
        assertEquals(Integer.valueOf(8), square.getField(xPos, yPos).getValues().get(2));
    }

    @Test
    public void testColumnContent() {
        Square square = SquareService.createSquare(9);

        int columnIndex = 2;
        List<Field> column = square.getColumn(columnIndex);
        assertEquals(square.getSize(), column.size());
        assertEquals(columnIndex, column.get(4).getColumnIndex());
    }

    @Test
    public void testRowContent() {
        Square square = SquareService.createSquare(9);

        int rowIndex = 2;
        List<Field> row = square.getRow(rowIndex);
        assertEquals(square.getSize(), row.size());
        assertEquals(rowIndex, row.get(4).getRowIndex());
    }

    @Test
    public void testSubSquareContent() {
        Square square = SquareService.createSquare(9);

        int rowIndex = 4;
        int colIndex = 7;
        List<Integer> values = new ArrayList<>();
        values.add(7);
        square.setField(new Field(rowIndex, colIndex, values));

        // field 4-7 is in subSquare 2-3
        int sqRowIndex = 2;
        int sqColIndex = 3;
        List<Field> subSquare = square.getSubSquare(sqRowIndex, sqColIndex);

        Field testField = null;
        for (Field field: subSquare){
            if (field.getValues().size() == 1){
                testField = field;
                break;
            }
        }
        assertEquals(square.getSize(), subSquare.size());
        assertNotNull(testField);
        assertEquals(7, testField.getValues().get(0).intValue());
    }
}
