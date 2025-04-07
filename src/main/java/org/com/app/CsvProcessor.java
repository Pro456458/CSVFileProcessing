package org.com.app;

import java.io.*;
import java.util.*;

public class CsvProcessor {

    private static final String UNKNOWN = "unknown";

    public void readAndProcessCsv(String filePath, String modifiedFilePath) {

        // Step 1: Detect Column Types
        Map<String, String> columnTypes = csvColumnTypeDetector(filePath);

        System.out.println("Detected column types: " + columnTypes);

        // Step 2: Modify Irregular Columns and Update CSV
        modifyIrregularColumns(filePath, columnTypes, modifiedFilePath);

    }

    /**
     * Detect the column types in the CSV file
     * @return Map of column names and their detected types
     */
    private Map<String, String> csvColumnTypeDetector(String filePath) {

        String line;
        String csvSplitBy = ",";
        Map<String, String> headerColumnsWithType = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String[] headers = br.readLine().split(csvSplitBy);

            headerColumnsWithType= setDefaultColumnType(headers);

            int rowCount = 0;

            // Process each row of the CSV file to detect column types
            while ((line = br.readLine()) != null) {

                String[] values = line.split(csvSplitBy, -1);

                boolean allColumnsDetected = true;

                // Inspect each value in the row
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    String currentType = headerColumnsWithType.get(headers[i]);

                    // Skip empty values
                    if (value.isEmpty()) {
                        allColumnsDetected = isAllColumnsDetected(headerColumnsWithType, headers, i, allColumnsDetected);
                        continue;
                    }

                    // Detect the type of the current value
                    if (currentType.equals(UNKNOWN)) {
                        headerColumnsWithType.put(headers[i], detectType(value));
                    } else if (!currentType.equals(detectType(value))) {
                        headerColumnsWithType.put(headers[i], "mixed");
                    }

                    // Check if all columns have been detected
                    allColumnsDetected = isAllColumnsDetected(headerColumnsWithType, headers, i, allColumnsDetected);
                }


                rowCount++;
                // Exit the loop if all column types are detected
                if (allColumnsDetected) {
                    System.out.println("All column types detected after " + rowCount + " rows count.");
                    break;
                }


            }

        } catch (IOException e) {
        e.printStackTrace();
        }

        return headerColumnsWithType;



    }

    /**
     * Modify irregular columns in the CSV file and write to a new file
     */
    private static void modifyIrregularColumns(String csvFile, Map<String, String> columnTypes, String modifiedCsvFile) {
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             FileWriter fw = new FileWriter(modifiedCsvFile)) {

            // Read the header line
            String[] headers = br.readLine().split(csvSplitBy);
            fw.write(String.join(csvSplitBy, headers) + "\n");

            // Process each row in the CSV file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy,-1);

                // Modify values based on detected column types
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    String columnType = columnTypes.get(headers[i]);

                    if (value.isEmpty() || value.equals("null")) {
                        values[i] = getDefaultValue(columnType);
                    }
                }

                // Write the modified row to the new CSV file
                fw.write(String.join(csvSplitBy, values) + "\n");
            }

            System.out.println("Modified CSV file has been created.");
            System.out.println("Modified CSV file path: " + modifiedCsvFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Check if all columns have been detected
     **/
    private boolean isAllColumnsDetected(Map<String, String> headerColumnsWithType, String[] headers, int i, boolean allColumnsDetected) {

        if (headerColumnsWithType.get(headers[i]).equals(UNKNOWN)) {
            allColumnsDetected = false;
        }
        return allColumnsDetected;
    }

    /**
     * Set default column type to "UNKNOWN" for all columns
     */
    private Map<String, String> setDefaultColumnType(String[] headers) {
        Map<String, String> columnTypes = new LinkedHashMap<>();

        for (String header : headers) {
            columnTypes.put(header, UNKNOWN);
        }
        return columnTypes;
    }

    /**
    Method to detect the data type of  given value
    **/
    private static String detectType(String value) {
        if (isInteger(value)) {
            return "integer";
        } else if (isFloat(value)) {
            return "float";
        } else {
            return "string";
        }
    }

    /**
     * Method to check if a value is an integer
     **/
    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method to check if a value is a float
     * **/
    private static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method to get a default value based on the column type
     * **/
    private static String getDefaultValue(String columnType) {
        switch (columnType) {
            case "integer":
                return "0";
            case "float":
                return "0.0";
            case "string":
                return "_";
            default:
                return "";
        }
    }
}


