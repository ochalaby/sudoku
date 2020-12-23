package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Square;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionServiceTest {

    @BeforeClass
    public static void setup() {
        //Generic setup
    }

    @Test
    public void testSelectUniqueValues() {
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(2, 1, new ArrayList<>(Arrays.asList(1,3,6))));
        sudokuSquare.setField(new Field(2, 2, new ArrayList<>(Arrays.asList(1,3,4,6,9))));
        sudokuSquare.setField(new Field(2, 3, new ArrayList<>(Arrays.asList(1,5,9))));
        sudokuSquare.setField(new Field(2, 4, new ArrayList<>(Arrays.asList(1,3,6,7,9))));
        sudokuSquare.setField(new Field(2, 5, new ArrayList<>(Arrays.asList(1,5,6,9))));
        sudokuSquare.setField(new Field(2, 6, new ArrayList<>(Collections.singletonList(2))));
        sudokuSquare.setField(new Field(2, 7, new ArrayList<>(Arrays.asList(1,9))));
        sudokuSquare.setField(new Field(2, 8, new ArrayList<>(Arrays.asList(1,3,6,9))));
        sudokuSquare.setField(new Field(2, 9, new ArrayList<>(Arrays.asList(1,9))));

        List<Field> collection = sudokuSquare.getRow(2);

        // action
        CollectionService.selectUniqueValues(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(2, 1, new ArrayList<>(Arrays.asList(1,3,6))));
        expectedCollection.add(new Field(2, 2, new ArrayList<>(Collections.singletonList(4))));
        expectedCollection.add(new Field(2, 3, new ArrayList<>(Arrays.asList(1,5,9))));
        expectedCollection.add(new Field(2, 4, new ArrayList<>(Collections.singletonList(7))));
        expectedCollection.add(new Field(2, 5, new ArrayList<>(Arrays.asList(1,5,6,9))));
        expectedCollection.add(new Field(2, 6, new ArrayList<>(Collections.singletonList(2))));
        expectedCollection.add(new Field(2, 7, new ArrayList<>(Arrays.asList(1,9))));
        expectedCollection.add(new Field(2, 8, new ArrayList<>(Arrays.asList(1,3,6,9))));
        expectedCollection.add(new Field(2, 9, new ArrayList<>(Arrays.asList(1,9))));

        assertEquals(expectedCollection, sudokuSquare.getRow(2));
    }

    @Test
    public void testRemoveFixedValues() {
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(2,1,4));
        sudokuSquare.setField(new Field(2,4,5));
        sudokuSquare.setField(new Field(2,7,8));
        List<Field> collection = sudokuSquare.getRow(2);

        // action
        CollectionService.removeFixedValues(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(2,1, new ArrayList<>(Collections.singletonList(4))));
        expectedCollection.add(new Field(2,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(2,3, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(2,4, new ArrayList<>(Collections.singletonList(5))));
        expectedCollection.add(new Field(2,5, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(2,6, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(2,7, new ArrayList<>(Collections.singletonList(8))));
        expectedCollection.add(new Field(2,8, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(2,9, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));

        assertEquals(expectedCollection, sudokuSquare.getRow(2));
    }

    @Test
    public void testRemoveValuesFromOtherSubSquares_sameRow(){
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(1,4, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(1,5, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(1,6, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,4, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(2,5, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(2,6, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(3,4, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(3,5, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(3,6, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));

        List<Field> collection = sudokuSquare.getSubSquare(sudokuSquare.getField(3,4));

        // action
        CollectionService.removeValuesFromOtherSubSquares(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(2,1, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,3, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,4, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(2,5, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(2,6, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(2,7, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,8, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,9, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));

        assertTrue(expectedCollection.containsAll(sudokuSquare.getRow(2)));
    }

    @Test
    public void testRemoveValuesFromOtherSubSquares_sameColumn(){
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(4,1, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(5,1, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(6,1, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(4,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(5,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(6,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(4,3, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(5,3, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(6,3, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));

        List<Field> collection = sudokuSquare.getSubSquare(sudokuSquare.getField(6,1));

        // action
        CollectionService.removeValuesFromOtherSubSquares(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(1,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(3,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(4,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(5,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(6,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(7,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(8,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(9,2, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));

        assertTrue(expectedCollection.containsAll(sudokuSquare.getColumn(2)));
    }

    @Test
    public void testRemoveValuesFromOtherFieldsInSubSquares_sameRow(){
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(2,1, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,3, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,4, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(2,5, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,6, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(2,7, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,8, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,9, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));

        List<Field> collection = sudokuSquare.getRow(2);

        // action
        CollectionService.removeValuesFromOtherFieldsInSubSquares(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(1,4, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(1,5, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(1,6, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(2,4, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(2,5, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(2,6, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(3,4, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(3,5, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(3,6, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));

        assertTrue(expectedCollection.containsAll(sudokuSquare.getSubSquare(sudokuSquare.getField(2,4))));
    }

    @Test
    public void testRemoveValuesFromOtherFieldsInSubSquares_sameColumn(){
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(1,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(2,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(3,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(4,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(5,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(6,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        sudokuSquare.setField(new Field(7,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(8,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        sudokuSquare.setField(new Field(9,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));

        List<Field> collection = sudokuSquare.getColumn(2);

        // action
        CollectionService.removeValuesFromOtherFieldsInSubSquares(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(4,1, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(5,1, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(6,1, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(4,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(5,2, new ArrayList<>(Arrays.asList(1,2,3,6,7,9))));
        expectedCollection.add(new Field(6,2, new ArrayList<>(Arrays.asList(1,2,3,4,7,9))));
        expectedCollection.add(new Field(4,3, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(5,3, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));
        expectedCollection.add(new Field(6,3, new ArrayList<>(Arrays.asList(1,2,3,5,6,7,8,9))));

        assertTrue(expectedCollection.containsAll(sudokuSquare.getSubSquare(sudokuSquare.getField(4,2))));
    }

    @Test
    public void testRemoveDoubleValues() {
        // setup
        Square sudokuSquare = SquareService.createSquare(9);
        sudokuSquare.setField(new Field(2,3, new ArrayList<>(Arrays.asList(3,7))));
        sudokuSquare.setField(new Field(2,7, new ArrayList<>(Arrays.asList(3,7))));
        List<Field> collection = sudokuSquare.getRow(2);

        // action
        CollectionService.removeDoubleValues(sudokuSquare, collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(2,1, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));
        expectedCollection.add(new Field(2,2, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));
        expectedCollection.add(new Field(2,3, new ArrayList<>(Arrays.asList(3,7))));
        expectedCollection.add(new Field(2,4, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));
        expectedCollection.add(new Field(2,5, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));
        expectedCollection.add(new Field(2,6, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));
        expectedCollection.add(new Field(2,7, new ArrayList<>(Arrays.asList(3,7))));
        expectedCollection.add(new Field(2,8, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));
        expectedCollection.add(new Field(2,9, new ArrayList<>(Arrays.asList(1,2,4,5,6,8,9))));

        assertTrue(expectedCollection.containsAll(sudokuSquare.getRow(2)));
    }
}
