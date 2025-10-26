package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.dto.FieldDto;
import com.chalabysolutions.sudoku.dto.RowDto;
import com.chalabysolutions.sudoku.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SudokuService {

    private static final Logger logger = LoggerFactory.getLogger(SudokuService.class);

    private SudokuService(){
        //Private constructor because this is a static class
    }

    public static Sudoku createSudoku(int size){
        logger.debug("Creating new square with size: {}", size);
        return new Sudoku(size);
    }

    public static Sudoku copySudoku(Sudoku sudoku){
        return sudoku.deepCopy();
    }

    public static int countUnsolvedFields(Sudoku sudoku){
        int count = 0;
        for (Field field : sudoku.getFields()) {
            if (field.getPossibleValues().size() > 1){
                count += 1;
            }
        }
        logger.debug("{} unsolved fields remaining", count);
        return count;
    }

    public static Sudoku fileToSudoku(String filePath) throws IOException {
        int squareSize = 9;

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Sudoku sudoku = createSudoku(squareSize);

        // Start with row 1 because first row (row 0) contains column indexes)
        for (int rowIndex = 1; rowIndex <= squareSize; rowIndex++){
            String line = lines.get(rowIndex);
            String[] lineValues = line.split("]\\[");
            for (int colIndex = 0; colIndex < squareSize; colIndex++){
                String value = lineValues[colIndex];
                if (colIndex == 0){
                    value = value.substring(3);
                }
                if (colIndex == squareSize - 1){
                    value = value.substring(0,1);
                }
                if (!" ".equals(value)){
                    int iValue = Integer.parseInt(value);
                    sudoku.getField(rowIndex - 1, colIndex).setFinalValue(iValue);
                }
            }
        }

        return sudoku;
    }

    public static void sudokusToFile(Sudoku orgSudoku, Sudoku newSudoku, String filePath){
        String sOrgSudoku = orgSudoku.toString();
        String sNewSudoku = newSudoku.toString();

        StringBuilder content = new StringBuilder();
        content.append(sOrgSudoku).append("\n\n").append(sNewSudoku);

        // If the file doesn't exists, create and write to it
        // If the file exists, truncate (remove all content) and write to it
        try (FileWriter writer = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(content.toString());
        } catch (IOException e) {
            logger.error("IOException: %s%n", e);
        }
    }

    public static Sudoku rowsToSudoku(List<RowDto> rows){
        Sudoku sudoku = SudokuService.createSudoku(rows.size());

        for (RowDto row : rows) {
            for (FieldDto field : row.getFields()) {
                String fieldValue = field.getValue();
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    // The RowDto and FieldDto use 1-based indexes for frontend,
                    // So conversion is needed to the 0-based indexes for backend
                    sudoku.getField(row.getId() - 1, field.getId() - 1).setFinalValue(Integer.parseInt(fieldValue));
                }
            }
        }

        return sudoku;
    }

    public static List<RowDto> sudokuToRows(Sudoku sudoku){
        int size = sudoku.getSize();
        List<RowDto> rows = new ArrayList<>();

        for (int x = 0; x < size; x++){
            RowDto rowDto = new RowDto();
            // Convert 0-based index in backend to 1-based index for frontend
            rowDto.setId(x + 1);

            for (int y = 0; y < size; y++){
                Field field = sudoku.getField(x, y);
                String value = "";

                // Als er een final value gezet is, toon die
                if (field != null && field.getFinalValue() != null) {
                    value = String.valueOf(field.getFinalValue());
                }

                // Convert 0-based index in backend to 1-based index for frontend
                rowDto.getFields().add(new FieldDto(y + 1, value));
            }

            rows.add(rowDto);
        }

        return rows;
    }

    public static boolean isSolved(Sudoku sudoku) {
        for (Field field : sudoku.getFields()) {
            if (field.getFinalValue() == null) return false;
        }
        return true;
    }

    public static boolean isValid(Sudoku sudoku) {
        int size = sudoku.getSize();

        // Controleer alle rijen
        for (Row row : sudoku.getRows()) {
            if (!isSelectionValid(row.getFields())){
                return false;
            };
        }

        // Controleer alle kolommen
        for (Column column : sudoku.getColumns()) {
            if (!isSelectionValid(column.getFields())){
                return false;
            };
        }

        // Controleer alle sub-squares
        for (SubSquare subSquare : sudoku.getSubSquares()) {
            if (!isSelectionValid(subSquare.getFields())){
                return false;
            };
        }

        // Geen conflicten gevonden
        return true;
    }

    public static boolean isSelectionValid(List<Field> selection){
        Set<Integer> seen = new HashSet<>();
        for (Field field : selection) {
            if (field.getFinalValue() != null) {
                int value = field.getFinalValue();
                if (!seen.add(value)) {
                    // Dubbele waarde in selection
                    return false;
                }
            }
        }

        return true;
    }
}
