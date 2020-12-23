package com.chalabysolutions.sudoku.services;

import com.chalabysolutions.sudoku.entities.Field;
import com.chalabysolutions.sudoku.entities.Square;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class SquareService {

    private static final Logger logger = LogManager.getLogger(SquareService.class);

    private SquareService(){
        //Private constructor because this is a static class
    }

    public static Square createSquare(int size){
        logger.debug("Creating new square with size: " + size);
        return new Square(size);
    }

    public static Square copySquare(Square square){
        Square squareCopy = createSquare(square.getSize());
        for (Field field : square.getFields().values()){
            List<Integer> newValues = new ArrayList<>(field.getValues());
            squareCopy.setField(new Field(field.getRowIndex(), field.getColumnIndex(), newValues));
        }
        return squareCopy;
    }

    public static int countOpenFieldValues(Square square){
        int count = 0;
        for (Field field : square.getFields().values()){
            count += field.getValues().size();
        }
        return count;
    }

    public static Square fileToSquare(String filePath) throws IOException {
        int squareSize = 9;

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Square square = createSquare(squareSize);

        for (int rowIndex = 1; rowIndex <= squareSize; rowIndex++){
            String line = lines.get(rowIndex);
            String[] lineValues = line.split("]\\[");
            for (int colIndex = 1; colIndex <= squareSize; colIndex++){
                String value = lineValues[colIndex-1];
                if (colIndex == 1){
                    value = value.substring(3);
                }
                if (colIndex == squareSize){
                    value = value.substring(0,1);
                }
                if (!" ".equals(value)){
                    int iValue = Integer.parseInt(value);
                    square.setField(new Field(rowIndex, colIndex, iValue));
                }
            }
        }

        logger.info("Square created from file: \n" + squareToString(square));
        return square;
    }

    public static void squaresToFile(Square orgSquare, Square newSquare, String filePath){
        String sOrgSquare = squareToString(orgSquare);
        String sNewSquare = squareToString(newSquare);

        StringBuilder content = new StringBuilder();
        content.append(sOrgSquare).append("\n\n").append(sNewSquare);

        // If the file doesn't exists, create and write to it
        // If the file exists, truncate (remove all content) and write to it
        try (FileWriter writer = new FileWriter(filePath);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(content.toString());
        } catch (IOException e) {
            logger.error("IOException: %s%n", e);
        }
    }

    public static String squareToString(Square square){
        StringBuilder result = new StringBuilder("  ");
        int size = square.getSize();
        for (int colIndex = 1; colIndex <= size; colIndex++){
            result.append(" ").append(colIndex).append(" ");
        }
        result.append("\n");
        for (int rowIndex = 1; rowIndex <= size; rowIndex++){
            result.append(rowIndex).append(" ");
            for (int colIndex = 1; colIndex <= size; colIndex++){
                Field field = square.getField(rowIndex, colIndex);
                if (field.getValues().size() == 1){
                    result.append("[").append(field.getValues().get(0)).append("]");
                } else {
                    result.append("[ ]");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }
}
