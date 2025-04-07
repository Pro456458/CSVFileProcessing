package org.com.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{


    String filePath = "src/main/resources/data/actual_csv_record.csv";
    String actualModifiedFilePath = "src/main/resources/data/modified_csv_record.csv";
    String expectedModifiedFilePath = "src/test/resources/data/expected_modified_csv_record.csv";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     *
     */
    public void testApp()
    {

        App.main(new String[]{filePath, actualModifiedFilePath});

        // Compare the CSV files and print the result
        boolean result = compareCsvFiles(expectedModifiedFilePath, actualModifiedFilePath);
        System.out.println("expected_modifiedFile files actual_modifiedFile are identical: " + result);

    }
    public static boolean compareCsvFiles(String actualCsvPath, String modifiedCsvPath) {
        try (BufferedReader actualReader = new BufferedReader(new FileReader(actualCsvPath));
             BufferedReader modifiedReader = new BufferedReader(new FileReader(modifiedCsvPath))) {

            String actualLine;
            String modifiedLine;

            while ((actualLine = actualReader.readLine()) != null && (modifiedLine = modifiedReader.readLine()) != null) {
                String[] actualValues = actualLine.split(",");
                String[] modifiedValues = modifiedLine.split(",");

                if (!Arrays.equals(actualValues, modifiedValues)) {
                    assertEquals(Arrays.toString(actualValues), Arrays.toString(modifiedValues));
                    return false;
                }
            }

            // Check if both files have the same number of lines
            if ((actualReader.readLine() != null) || (modifiedReader.readLine() != null)) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
