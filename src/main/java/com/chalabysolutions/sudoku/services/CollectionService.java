package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CollectionService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

    private CollectionService() {}

    public static void removeFixedValues(List<Field> collection) {
        // Get fields with a fixed value
        List<Field> fieldsWithFixedValue = new ArrayList<>();
        List<Field> fieldsWithMultipleValues = new ArrayList<>();
        for (Field field : collection) {
            if (field.getFinalValue() != null) {
                fieldsWithFixedValue.add(field);
            } else {
                fieldsWithMultipleValues.add(field);
            }
        }

        // Remove the fixed value from the other field values list
        for (Field target : fieldsWithMultipleValues) {
            for (Field fixed : fieldsWithFixedValue) {
                int val = fixed.getFinalValue();
                if (target.getPossibleValues().contains(val)) {
                    target.removePossibleValue(val);
                }
            }
        }
    }

    public static void selectUniqueValues(List<Field> collection) {
        // Check fields that uniquely have a certain value,
        // if only one field contains that value, fill it in as only value in the list.
        for (int i = 1; i<= collection.size(); i++ ){
            // Collect fields that have the specified value in their values list
            List<Field> fieldsWithValue = new ArrayList<>();
            for (Field field : collection) {
                 if (field.getPossibleValues().contains(i)) {
                    fieldsWithValue.add(field);
                }
            }

            // If there's only one field that contains the specified value in its value list,
            // then fill in this value as the only value of the field value list.
            if (fieldsWithValue.size() == 1){
                Field field = fieldsWithValue.get(0);
                if (field.getFinalValue() == null) {
                    logger.debug("Setting field {},{} with single value: {} (old values: {})", field.getRowIndex(), field.getColumnIndex(), i, field.getPossibleValues());
                    field.setFinalValue(i);
                }
            }
        }
    }

    public static void removeDoubleValues(List<Field> collection){
        // If there are one or more sets of only two fields in the list that only contain the same two values, then these
        // values can be removed from the other fields

        // Create a list of fields with just two values, and a list of the remaining fields
        List<Field> doubleValueFields = new ArrayList<>();
        List<Field> remainingFields = new ArrayList<>();
        for (Field field: collection){
            if (field.getPossibleValues().size() == 2){
                doubleValueFields.add(field);
            }

            if (field.getPossibleValues().size() > 2){
                remainingFields.add(field);
            }
        }

        if (doubleValueFields.size() < 2) return;

        // Collect the values that are double in other fields as well
        Set<Integer> doubleValues = new HashSet<>();
        for (Field field1 : doubleValueFields){
            for (Field field2 : doubleValueFields){
                if (!(field1.getRowIndex() == field2.getRowIndex()) && !(field1.getColumnIndex() == field2.getColumnIndex())
                        && field1.getPossibleValues().equals(field2.getPossibleValues())){
                    doubleValues.addAll(field2.getPossibleValues());
                }
            }
        }

        if (!doubleValues.isEmpty()){
            for (Field field: remainingFields){
                for (Integer value : doubleValues){
                    field.removePossibleValue(value);
                }
            }
        }
    }
}
