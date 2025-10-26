package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Sudoku;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionServiceTest {

    @BeforeAll
    public static void setup() {
        //Generic setup
    }

    @Test
    public void testRemoveFixedValues() {
        // setup
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.getField(2,1).setFinalValue(4);
        sudoku.getField(2,4).setFinalValue(5);
        sudoku.getField(2,7).setFinalValue(8);

        // action
        List<Field> collection = sudoku.getRows()[2].getFields();
        CollectionService.removeFixedValues(collection);

        // verification
        Sudoku expectedSudoku = SudokuService.createSudoku(9);
        for (int i = 0; i < 9; i++) {
            Field field = expectedSudoku.getField(2,i);
            field.removePossibleValue(4);
            field.removePossibleValue(5);
            field.removePossibleValue(8);
            if (i == 1){
                field.setFinalValue(4);
            } else if (i == 4){
                field.setFinalValue(5);
            } else if (i == 7){
                field.setFinalValue(8);
            }
        }

        assertEquals(expectedSudoku.getRows()[2].getFields(), sudoku.getRows()[2].getFields());
    }



    @Test
    public void testSelectUniqueValues() {
        // setup
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.setField(new Field(2, 0, new ArrayList<>(Arrays.asList(1,3,6))));
        sudoku.setField(new Field(2, 1, new ArrayList<>(Arrays.asList(1,3,4,6,9))));
        sudoku.setField(new Field(2, 2, new ArrayList<>(Arrays.asList(1,5,9))));
        sudoku.setField(new Field(2, 3, new ArrayList<>(Arrays.asList(1,3,6,7,9))));
        sudoku.setField(new Field(2, 4, new ArrayList<>(Arrays.asList(1,5,6,9))));
        sudoku.setField(new Field(2, 5, new ArrayList<>(Collections.singletonList(2))));
        sudoku.setField(new Field(2, 6, new ArrayList<>(Arrays.asList(1,9))));
        sudoku.setField(new Field(2, 7, new ArrayList<>(Arrays.asList(1,3,6,9))));
        sudoku.setField(new Field(2, 8, new ArrayList<>(Arrays.asList(1,9))));

        // action
        List<Field> collection = sudoku.getRows()[2].getFields();
        CollectionService.selectUniqueValues(collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(2, 0, new ArrayList<>(Arrays.asList(1,3,6))));
        expectedCollection.add(new Field(2, 1, new ArrayList<>(Collections.singletonList(4))));
        expectedCollection.add(new Field(2, 2, new ArrayList<>(Arrays.asList(1,5,9))));
        expectedCollection.add(new Field(2, 3, new ArrayList<>(Collections.singletonList(7))));
        expectedCollection.add(new Field(2, 4, new ArrayList<>(Arrays.asList(1,5,6,9))));
        expectedCollection.add(new Field(2, 5, new ArrayList<>(Collections.singletonList(2))));
        expectedCollection.add(new Field(2, 6, new ArrayList<>(Arrays.asList(1,9))));
        expectedCollection.add(new Field(2, 7, new ArrayList<>(Arrays.asList(1,3,6,9))));
        expectedCollection.add(new Field(2, 8, new ArrayList<>(Arrays.asList(1,9))));

        assertEquals(expectedCollection, sudoku.getRows()[2].getFields());
    }

    @Test
    public void testRemoveDoubleValues() {
        // setup
        Sudoku sudoku = SudokuService.createSudoku(9);
        sudoku.setField(new Field(2,1, new ArrayList<>(Arrays.asList(2,4))));
        sudoku.setField(new Field(2,2, new ArrayList<>(Arrays.asList(1,9))));
        sudoku.setField(new Field(2,3, new ArrayList<>(Arrays.asList(3,7))));
        sudoku.setField(new Field(2,7, new ArrayList<>(Arrays.asList(3,7))));
        sudoku.setField(new Field(2,8, new ArrayList<>(Arrays.asList(2,4))));

        // action
        List<Field> collection = sudoku.getRows()[2].getFields();
        CollectionService.removeDoubleValues(collection);

        // verification
        List<Field> expectedCollection = new ArrayList<>();
        expectedCollection.add(new Field(2,0, new ArrayList<>(Arrays.asList(1,5,6,8,9))));
        expectedCollection.add(new Field(2,1, new ArrayList<>(Arrays.asList(2,4))));
        expectedCollection.add(new Field(2,2, new ArrayList<>(Arrays.asList(1,9))));
        expectedCollection.add(new Field(2,3, new ArrayList<>(Arrays.asList(3,7))));
        expectedCollection.add(new Field(2,4, new ArrayList<>(Arrays.asList(1,5,6,8,9))));
        expectedCollection.add(new Field(2,5, new ArrayList<>(Arrays.asList(1,5,6,8,9))));
        expectedCollection.add(new Field(2,6, new ArrayList<>(Arrays.asList(1,5,6,8,9))));
        expectedCollection.add(new Field(2,7, new ArrayList<>(Arrays.asList(3,7))));
        expectedCollection.add(new Field(2,8, new ArrayList<>(Arrays.asList(2,4))));

        assertEquals(expectedCollection, sudoku.getRows()[2].getFields());
    }

}
