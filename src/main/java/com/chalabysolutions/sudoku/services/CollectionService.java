package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Square;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CollectionService {

    private static final Logger logger = LogManager.getLogger(CollectionService.class);

    public static void selectUniqueValues(Square square, List<Field> collection) {
        // Check fields that uniquely have a certain value,
        // if only one field contains that value, fill it in as only value in the list.
        for (int i = 1; i<= collection.size(); i++ ){
            // Collect fields that have the specified value in their values list
            List<Field> fieldsWithValue = new ArrayList<>();
            for (Field field : collection) {
                 if (field.getValues().contains(i)) {
                    fieldsWithValue.add(field);
                }
            }

            // If there's only one field that contains the specified value in its value list,
            // then fill in this value as the only value of the field value list.
            if (fieldsWithValue.size() == 1){
                Field field = fieldsWithValue.get(0);
                if (field.getValues().size() > 1) {
                    logger.debug("Setting field " + field.getRowIndex() + field.getColumnIndex() +
                            " with single value: " + i + " (old values: " + field.getValues() + ")");
                    square.setField(new Field(field.getRowIndex(), field.getColumnIndex(), i));
                } else {
                    logger.debug("Skip Setting field " + field.getRowIndex() + field.getColumnIndex() +
                            " with single value: " + i + " (old values: " + field.getValues() + ")");
                }
            }
        }
    }

    public static void removeFixedValues(Square square, List<Field> collection) {
        // Get fields with a fixed value
        List<Field> fieldsWithValue = new ArrayList<>();
        for (Field field : collection) {
            if (field.getValues().size() == 1) {
                fieldsWithValue.add(field);
            }
        }

        // Remove the fixed value from the other field values list
        for (Field field : collection) {
            for (Field fieldWithValue : fieldsWithValue) {
                if (!(field.getRowIndex() == fieldWithValue.getRowIndex() &&
                        field.getColumnIndex() == fieldWithValue.getColumnIndex())) {
                    field.removeValue(fieldWithValue.getValues().get(0));
                    square.setField(field);
                }
            }
        }
    }

    public static void removeValuesFromOtherSubSquares(Square square, List<Field> collection){
        // If a value is only possible in one row/column of a subSquare,
        // then remove that value from the same row/column in the other subSquares on the same
        // square row/column.

        // Loop all possible values and check the given subSquareFields list
        for (int i = 1; i <= collection.size(); i++){
            // Collect fields from the field list with the selected value
            List<Field> fieldsWithValue = new ArrayList<>();
            for (Field field : collection) {
                if (field.getValues().size() > 1 && field.getValues().contains(i)) {
                    fieldsWithValue.add(field);
                }
            }

            if (fieldsWithValue.size() > 1){
                Field matchField = fieldsWithValue.get(0);
                if (sameRow(fieldsWithValue)){
                    // Remove value from same row in other subSquares
                    List<Field> rowFields = square.getRow(matchField.getRowIndex());
                    for (Field field : rowFields){
                        if (getSubSquareIndex(field.getColumnIndex(), square.getSize()) != getSubSquareIndex(matchField.getColumnIndex(), square.getSize())){
                            field.removeValue(i);
                            square.setField(field);
                        }
                    }
                }

                if (sameColumn(fieldsWithValue)){
                    // Remove value from same column in other subSquares
                    List<Field> colFields = square.getColumn(matchField.getColumnIndex());
                    for (Field field : colFields){
                        if (getSubSquareIndex(field.getRowIndex(), square.getSize()) != getSubSquareIndex(matchField.getRowIndex(), square.getSize())){
                            field.removeValue(i);
                            square.setField(field);
                        }
                    }
                }
            }
        }
    }

    public static void removeValuesFromOtherFieldsInSubSquares(Square square, List<Field> collection){
        // If a value in a row/column is only possible in the same subSquare,
        // then remove that value from the other fields in that subSquares.

        // Loop all possible values and check the given subSquareFields list
        for (int i = 1; i <= collection.size(); i++){
            // Collect fields from the field list with the selected value
            List<Field> fieldsWithValue = new ArrayList<>();
            for (Field field : collection) {
                if (field.getValues().size() > 1 && field.getValues().contains(i)) {
                    fieldsWithValue.add(field);
                }
            }

            if (fieldsWithValue.size() > 1){
                Field matchField = fieldsWithValue.get(0);
                if (sameSubSquare(fieldsWithValue, square.getSize())){
                    // Remove value from other fields in same subSquares
                    List<Field> subSquareFields = square.getSubSquare(matchField);
                    if (sameRow(fieldsWithValue)){
                        for (Field field : subSquareFields){
                            if (field.getRowIndex() != matchField.getRowIndex()){
                                field.removeValue(i);
                                square.setField(field);
                            }
                        }
                    }

                    if (sameColumn(fieldsWithValue)){
                        for (Field field : subSquareFields){
                            if (field.getColumnIndex() != matchField.getColumnIndex()){
                                field.removeValue(i);
                                square.setField(field);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeDoubleValues(Square square, List<Field> collection){
        List<Field> doubleValueFields = new ArrayList<>();
        List<Field> remainingFields = new ArrayList<>();
        for (Field field: collection){
            if (field.getValues().size() == 2){
                doubleValueFields.add(field);
            }

            if (field.getValues().size() > 2){
                remainingFields.add(field);
            }
        }

        boolean sameDoubles = false;
        if (doubleValueFields.size() == 2){
            if (doubleValueFields.get(0).getValues().contains(doubleValueFields.get(1).getValues().get(0)) &&
                    doubleValueFields.get(0).getValues().contains(doubleValueFields.get(1).getValues().get(1))){
                sameDoubles = true;
            }
        }

        if (sameDoubles) {
            for (Field field: remainingFields){
                field.getValues().removeAll(doubleValueFields.get(0).getValues());
                square.setField(field);
            }
        }
    }

    private static boolean sameRow(List<Field> fields){
        boolean result = true;
        int rowIndex = fields.get(0).getRowIndex();
        for (int i = 1; i < fields.size(); i++){
            if (fields.get(i).getRowIndex() != rowIndex){
                result = false;
                break;
            }
        }

        return result;
    }

    private static boolean sameColumn(List<Field> fields){
        boolean result = true;
        int colIndex = fields.get(0).getColumnIndex();
        for (int i = 1; i < fields.size(); i++){
            if (fields.get(i).getColumnIndex() != colIndex){
                result = false;
                break;
            }
        }

        return result;
    }

    private static int getSubSquareIndex(int index, int squareSize){
        return (int) (Math.floor((index - 1) / Math.sqrt(squareSize)) + 1);
    }

    private static boolean sameSubSquare(List<Field> fields, int squareSize){
        boolean result = true;

        int sqRowIndex = getSubSquareIndex(fields.get(0).getRowIndex(), squareSize);
        int sqColIndex = getSubSquareIndex(fields.get(0).getColumnIndex(), squareSize);
        for (int i = 1; i < fields.size(); i++){
            if (getSubSquareIndex(fields.get(i).getRowIndex(), squareSize) != sqRowIndex ||
                    getSubSquareIndex(fields.get(i).getColumnIndex(), squareSize) != sqColIndex){
                result = false;
                break;
            }
        }

        return result;
    }
}
